//package hello;
package cn.webank.demo;

import cn.webank.blockchain.api.accounting.AcquirerBankClient;
import cn.webank.blockchain.api.accounting.ClearingBankClient;
import cn.webank.blockchain.api.accounting.IssuingBankClient;
import cn.webank.blockchain.api.accounting.RegTechClient;
import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.constants.RecordAccountsPayableResultType;
import cn.webank.blockchain.constants.TradeType;
import cn.webank.blockchain.impl.accounting.AcquirerBankClientImpl;
import cn.webank.blockchain.impl.accounting.ClearingBankClientImpl;
import cn.webank.blockchain.impl.accounting.IssuingBankClientImpl;
import cn.webank.blockchain.impl.accounting.RegTechClientImpl;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.blockchain.spi.common.dto.IResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.utils.JsonMapper;
import cn.webank.common.conf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

//@SpringBootApplication
//@ComponentScan({"cn.webank.blockchain"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    //    private static final Integer checkCode = 18755;
    private static final Integer checkCode = 4317; // 非常大的对账文件
    private static final Integer checkCodeClearComplete = 112121;
    private static final Integer checkCodeIncludeSomeTrans = 15452;
    private static int interval = Config.getStressTestAllInterfaceInterval();
    private final String account = "0120382124301";
    private final String userName = "helloworld1";
    private final String accountIncludeTransList = "HF1";
    private final String orgId = "BOCHK";
    private String ibBizNo;
    private static String abBizNo;
    private String msBizNo;
    private String merchantSvcId = "2045840004";
    private String merchantSvcName = "test";
    private String merchantId = "204002850188276";
    private String merchantName = "merchantNameTest0001";
    private Integer rmbAmt = 1000;
    private Integer txAmt = 1210;
    private Integer txType = 13;
    private Integer isStrike = 0;
    private Integer fxRate = 880;
    private IssuingBankClient issuingBankClient;
    private AcquirerBankClient acquirerBankClient;
    private ClearingBankClient clearingBankClient;
    private RegTechClient regTechClient;

    private Map<String, Integer> testResultMap = new HashMap<String, Integer>();

    public Application() {
        this.issuingBankClient = new IssuingBankClientImpl();
        this.acquirerBankClient = new AcquirerBankClientImpl();
        this.clearingBankClient = new ClearingBankClientImpl();
        this.regTechClient = new RegTechClientImpl();
    }

    public static void main(String args[]) {

    	
    	try {
            Application application = new Application();
            if (args.length < 1)
            {
                System.out.println("Please input function name, for example: doTransaction or setExchangeRate");
                System.exit(0);
            }
            application.run(args[0]);

        } catch (Exception e) {
            log.error("Exception in main", e);
        }
    }

    //  @Override
    public void run(String... args) throws Exception {

        this.ibBizNo = String.valueOf(System.nanoTime());
        this.abBizNo = String.valueOf(System.nanoTime());
        System.out.println();
        if("doTransaction".equals(args[0]))
        {
            this.testDoTransaction();
            System.out.println();
            this.testGetOrderDetailByAbBizNo(ibBizNo);
        }
        else if("setExchangeRate".equals(args[0]))
        {
            this.testSetExchangeRate();
            System.out.println();
            this.testGetExchangeRate();
        }
        else
        {
            System.out.println("Please input function name, for example: doTransaction or setExchangeRate");
        }
        System.exit(0);
    }
    
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
        return ste[ste.length - 1 - depth].getMethodName();
    }

    private void printArrayList(ArrayList<LinkedHashMap> result) {
        log.info("tansaction list size: {}", result.size());

        Integer index = 0;
        for (Iterator<LinkedHashMap> iter = result.iterator(); iter.hasNext(); ) {
            LinkedHashMap<String, String> element = iter.next();
            log.info("tansaction index: {}", index);
            for (Map.Entry<String, String> entry : element.entrySet()) {
                log.info("key : {} | value : {} ", entry.getKey(), entry.getValue());
            }
            index++;
            log.info("-------------------------------------");
        }
    }

    private <T, F extends IResult> void printResponseStruct(String funcName, ResponseStruct<T> responseStruct, F result) {
        System.out.println(funcName);
        log.info(funcName);
        System.out.println("respObj body : " + responseStruct);
        log.info("respObj body : {}" + responseStruct);

        if (0 == responseStruct.getErrorCode()) {
            if (0 == result.getErrorCode()) {

                testResultMap.put(funcName, 0);
                System.out.println(funcName + " status : succ!");
                log.info(funcName + " status : succ!");
            } else {
                System.out.println(funcName + " status : failed!");
                log.info(funcName + " status : failed!");
                testResultMap.put(funcName, -10);
            }
        } else {
            System.out.println(funcName + " status : failed!");
            log.info(funcName + " status : failed!");
            testResultMap.put(funcName, -10);
        }
    }

    private <T, F extends IResult> void printResponseStruct(String funcName, ResponseStruct<T> responseStruct, boolean isSucc) {
        log.info(funcName);
        log.info("respObj body : {}" + responseStruct);
        System.out.println(funcName);
        System.out.println("respObj body : " + responseStruct);

        if (0 == responseStruct.getErrorCode() && isSucc) {
            testResultMap.put(funcName, 0);
            log.info(funcName + " status : succ!");
            System.out.println(funcName + " status : succ!");
        } else {
            log.info(funcName + " status : failed!");
            System.out.println(funcName + " status : failed!");
            testResultMap.put(funcName, -10);
        }
    }

    public void testCheckDirectRouteMsgHealth(String toOrgId) {

        log.info("testing direct msg to : " + toOrgId);
        CheckDirectRouteMsgHealthArgs arg = new CheckDirectRouteMsgHealthArgs();
        arg.setMessage("good");
        ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = clearingBankClient.checkDirectRouteMsgHealth(toOrgId, arg);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());
    }

//    private void testCreateVirtualAccount() {
//
//        CurrencyType currency = CurrencyType.CURRENCY_TYPE_HKD;
//        String curr_date = "20170909";
//        String first_name = "junqi";
//        String last_name = "zhang";
//        String id_type = "01";
//        String user_id = "510219";
//        String user_card = "41005016";
//
//        CreateVirtualAccountArgs transactionArgs = new CreateVirtualAccountArgs();
//        transactionArgs.setOrgId(this.orgId);
//        transactionArgs.setUserAccount(this.account);
//        transactionArgs.setCurrency(currency);
//        transactionArgs.setCurrDate(curr_date);
//        transactionArgs.setFirstName(first_name);
//        transactionArgs.setLastName(last_name);
//        transactionArgs.setIdType(id_type);
//        transactionArgs.setUserId(user_id);
//        transactionArgs.setUserCard(user_card);
//
//        ResponseStruct<CreateVirtualAccountStatus> responseStruct = issuingBankClient.createVirtualAccount(transactionArgs);
//        log.info("respObj body : {}", responseStruct);
//
//        if (0 == responseStruct.getErrorCode() && responseStruct.getResult().isCreateVirtualAccountResultSucc()) {
//            log.info("result : createVirtualAccount success");
//        } else {
//            log.info("result : createVirtualAccount failed");
//        }
//    }

    public void testGetExchangeRate() {

        CurrencyType currencyType = CurrencyType.CURRENCY_TYPE_HKD;

        long start = System.currentTimeMillis();
        log.info("getExchangeRate start : {}", start);
        System.out.println("getExchangeRate start : " + start);
        ResponseStruct<ExchangeRate> responseStruct = issuingBankClient.getExchangeRate(currencyType);

        long end = System.currentTimeMillis();
        log.info("getExchangeRate latency : {}", end - start);
        System.out.println("getExchangeRate latency : " + (end - start));

//        log.info("respObj body : {}", responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            log.info("status : getExchangeRate success");
//            log.info("exchange : {} ", responseStruct.getResult().getFxRate());
//            log.info("exTimestamp : {} ", responseStruct.getResult().getExTimestamp());
//
//        } else {
//            log.info("status : getExchangeRate failed");
//        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, true);
    }

    public void testupdateCheckCodeStatus() {

//        CurrencyType currencyType = CurrencyType.CURRENCY_TYPE_HKD;

        long start = System.currentTimeMillis();
        log.info("getExchangeRate start : {}", start);

        ResponseStruct<UpdateCheckCodeStatusResult> resp = acquirerBankClient.updateCheckCodeStatus(orgId, 2018100501);
//        ResponseStruct<ExchangeRate> responseStruct = issuingBankClient.getExchangeRate(currencyTsype);

        long end = System.currentTimeMillis();
        log.info("getExchangeRate latency : {}", end - start);

//        log.info("respObj body : {}", responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            log.info("status : getExchangeRate success");
//            log.info("exchange : {} ", responseStruct.getResult().getFxRate());
//            log.info("exTimestamp : {} ", responseStruct.getResult().getExTimestamp());
//
//        } else {
//            log.info("status : getExchangeRate failed");
//        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, resp, true);
    }
    
    public void testSetExchangeRate(){

        String toOrgId = "BOCHK";
        ExchangeRate arg = new ExchangeRate();
        arg.setCurrency(CurrencyType.CURRENCY_TYPE_HKD);
        arg.setFxRate(8778);
        arg.setRefundFxRate(8888);
        arg.setExTimestamp(1434343438);
        arg.setCurrencyUnit(10000);

        ResponseStruct<TxExecuteResult> responseStruct = clearingBankClient.setExchangeRate(toOrgId, arg);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult().isSucc());

//        log.info("respObj body : {}" + responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            if (responseStruct.getResult().isSucc()) {
//                log.info("status : setExchangeRate succ!");
//            } else {
//                log.info("status : setExchangeRate failed!");
//            }
//        } else {
//            log.info("status : setExchangeRate failed!");
//        }
    }

    private void testBochkCallback() {

        /**
         * 如果有消息通知，SDK会自动调用BochkDirectRouteCallback类里面实现的onPush函数
         */
        BochkDirectRouteCallback callback = new BochkDirectRouteCallback();
        issuingBankClient.registerCallback(callback);
    }

    private void testBocCallback() {

        /**
         * 如果有消息通知，SDK会自动调用BochkDirectRouteCallback类里面实现的onPush函数
         */
        BocDirectRouteCallback callback = new BocDirectRouteCallback();
        clearingBankClient.registerCallback(callback);
    }

    private void testWeBankCallback() {

        /**
         * 如果有消息通知，SDK会自动调用WbDirectRouteCallback类里面实现的onPush函数
         */
        WbDirectRouteCallback callback = new WbDirectRouteCallback();
        acquirerBankClient.registerCallback(callback);
    }

    public void testGeneratePaymentAuthCode() {

        log.info("testGeneratePaymentAuthCode : 111111");

        String paymentAuthCode = issuingBankClient.generatePaymentAuthCode(this.account);

        log.info("paymentAuthCode : {}", paymentAuthCode);
    }

    public void testGetClearingStatus() {
        String orgId = "BOCHK";
        ResponseStruct<GetClearingStatusResult> responseStruct = issuingBankClient.getClearingStatus(orgId, 19827);
        log.info("respObj body : {}" + responseStruct);
        if (0 == responseStruct.getErrorCode()
                && responseStruct.getResult().isFinish()) {
            log.info("status : Clearing finished");
        } else {
            log.info("status : Clearing have not finished");
        }

//        String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult().isFinish());
    }

    public void testGetMerchantInfoByQrCodeString() {

        long start = System.currentTimeMillis();
        log.info("getMerchantInfoByQrCodeString start : {}", start);

        String qrCodeStr = "https://openq.tlinx.com/?O=cbcdfc5414aac4b277b6c9857ef7ae40";
        ResponseStruct<GetMerchantInfoResult> responseStruct = issuingBankClient.getMerchantInfoByQrCodeString(qrCodeStr);
        long end = System.currentTimeMillis();
        log.info("getMerchantInfoByQrCodeString latency : {}", end - start);

        log.info("respObj body : {}" + responseStruct);
        if (0 == responseStruct.getErrorCode()) {
            if (0 == responseStruct.getResult().getErrorCode()) {
                log.info("status : getMerchantInfoByQrCodeString success");

                Application.this.testPlaceOrder(
                        ibBizNo,
                        responseStruct.getResult().getMerchantSvcId(),
                        responseStruct.getResult().getMerchantSvcName(),
                        responseStruct.getResult().getMerchantId(),
                        responseStruct.getResult().getMerchantName()
                );
            } else {
                log.info("status : qrCode is invalid");
            }
        } else {
            log.info("status : getMerchantInfoByQrCodeString failed!");
        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());
    }

    public void testNotifySetCheckStatus(Integer lastCheckCode, String lastCheckCodeDate) {

        String walletOwnerOrgId = "BOCHK";
        String toOrgId = "BOCHK";

        SetCheckCodeStatusNotifyArgs arg = new SetCheckCodeStatusNotifyArgs();
        arg.setSucc(true);
        arg.setWalletOwnerOrg(walletOwnerOrgId);
        arg.setLastCheckCode(checkCode);
        arg.setLastCheckCodeDate(lastCheckCodeDate);
        arg.setCheckRemark("");
        arg.setExtraData("set check code finished!");

        {
            ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = acquirerBankClient.notifySetCheckCodeStatus(toOrgId, arg);

            if (0 == responseStruct.getErrorCode()) {
                if (0 == responseStruct.getResult().getErrorCode()) {
                    log.info("------------------------------------------------");
                    log.info("status : notify WeBank succ!");
                    log.info("last check code : {} ", checkCode);
                    log.info("------------------------------------------------");
                } else {
                }
            } else {
            }

            String funcName = "testNotifySetCheckStatus -> " + toOrgId;
            printResponseStruct(funcName, responseStruct, responseStruct.getResult());
        }

        toOrgId = "BOC";
        {
//            long start = System.currentTimeMillis();
//            log.info("notifySetCheckSuccToIssuingBank start : {}", start);
//
//            ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = acquirerBankClient.notifySetCheckCodeStatus(toOrgId, arg);
//            long end = System.currentTimeMillis();
//
//            log.info("respObj body : {}" + responseStruct);
//            log.info("notifySetCheckSuccToIssuingBank latency : {}", end - start);
//
//            if (0 == responseStruct.getErrorCode()) {
//                if (0 == responseStruct.getResult().getErrorCode()) {
//                    log.info("------------------------------------------------");
//                    log.info("status : notify BOCHK succ!");
//                    log.info("last check code : {} ", checkCode);
//                    log.info("------------------------------------------------");
//                } else {
//                }
//            } else {
//            }
//
//            String funcName = "testNotifySetCheckStatus -> " + toOrgId;
//            printResponseStruct(funcName, responseStruct, responseStruct.getResult());
        }
    }

    public void testNotifyClearingStatus() {

        ClearingStatusNotifyArgs arg = new ClearingStatusNotifyArgs();
        arg.setSucc(true);
        arg.setCurrency(CurrencyType.CURRENCY_TYPE_HKD);
        arg.setTotalRmbAmt(2054);
        arg.setTotalTxAmt(2342);
        arg.setWbBalance(34343);
        arg.setWalletOwnerOrg("BOCHK");
        arg.setLastCheckCode(19827);
        arg.setExtraData("test : clearing finished!");

        String toOrgId = "WB";

//        {
//            ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = clearingBankClient.notifyClearingStatusToAcquirerBank(toOrgId, arg);
//
//            log.info("respObj body : {}" + responseStruct);
//            if (0 == responseStruct.getErrorCode()) {
//                if (0 == responseStruct.getResult().getErrorCode()) {
//                    log.info("status : notifyClearingStatus succ!");
//                } else {
//                    log.info("status : notifyClearingStatus failed!");
//                }
//            } else {
//                log.info("status : notifyClearingStatus failed!");
//            }
//
//            String funcName = "testNotifyClearingStatus -> " + toOrgId;
//            printResponseStruct(funcName, responseStruct, responseStruct.getResult());
//        }

        {
            toOrgId = "BOCHK";
            ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = clearingBankClient.notifyClearingStatusToIssuingBank(toOrgId, arg);

            log.info("respObj body : {}" + responseStruct);
            if (0 == responseStruct.getErrorCode()) {
                if (0 == responseStruct.getResult().getErrorCode()) {


        log.info("status : notifyClearingStatus succ!");
                } else {
                    log.info("status : notifyClearingStatus failed!");
                }
            } else {
                log.info("status : notifyClearingStatus failed!");
            }

            String funcName = "testNotifyClearingStatus -> " + toOrgId;
            printResponseStruct(funcName, responseStruct, responseStruct.getResult());
        }
    }

//    public void testSetCheckCodeTag(String requestId, String filePath) {
//
//        String forOrgId = "BOCHK";
//        SetCheckCodeTagArgs arg = new SetCheckCodeTagArgs();
//
//        arg.setReguestId(requestId);
//        arg.setRemark("20170918");
//        ResponseStruct<SetCheckCodeTagResult> responseStruct = acquirerBankClient.setCheckCodeTag(orgId, arg);
//
//        boolean isSucc = false;
//        log.info("respObj body : {}" + responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            if (responseStruct.getResult().isSucc()) {
//                Integer lastCheckCode = responseStruct.getResult().getLastCheckCode();
//
//                testNotifySetCheckStatus(lastCheckCode, "20170927");
//                isSucc = true;
//                log.info("------------------------------------------------");
//                log.info("utils status : setCheckCodeTag succ!");
//                log.info("last check code : {} ", lastCheckCode);
//                log.info("------------------------------------------------");
//
////                ResponseStruct<Integer> responseStructGene = testGenerateClearingTransFile(lastCheckCode, filePath);
////
////                if (0 == responseStructGene.getErrorCode() && 0 == responseStructGene.getResult()) {
////                    log.info("------------------------------------------------");
////                    log.info("utils status : testGenerateClearingTransFile succ!");
////                    log.info("------------------------------------------------");
////                }
//
////                return;
//            } else {
//                log.info("utils status : setCheckCodeTag failed!");
//            }
//        } else {
//            log.info("utils status : setCheckCodeTag failed!");
//        }
//
//        String funcName = new Object() {
//        }.getClass().getEnclosingMethod().getName();
//        printResponseStruct(funcName, responseStruct, isSucc);
//
////        log.info("------------------------------------------------");
////        log.info("utils status : setCheckCodeTag failed!");
////        log.info("------------------------------------------------");
//    }

    public ResponseStruct<Integer> testGenerateClearingTransFile(Integer lastCheckCode, String filePath) {

        String orgId = "BOCHK";
        if (null == filePath) {
            filePath = "/tmp/testlist6842";
        }
        ResponseStruct<Integer> responseStruct = issuingBankClient.generateClearingTransFile(orgId, lastCheckCode, filePath);

        boolean isSucc = false;
        if (0 == responseStruct.getErrorCode() && 0 == responseStruct.getResult()) {
            log.info("generateClearingTransFile succ!");
            isSucc = true;
        }
        log.info("responseStruct : ", responseStruct);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, isSucc);

        return responseStruct;
    }

    public void testSetClearingStatus() {

        String forOrgId = "BOCHK";
        SetClearingStatusArgs arg = new SetClearingStatusArgs();
        arg.setLastCheckCode(19827);
        arg.setStatus(1);
        arg.setCurrency(CurrencyType.CURRENCY_TYPE_HKD);
        arg.setTimestamp(1506322490000L);
        arg.setTotalRmbAmt(1980);
        arg.setTotalTxAmt(1981);
        arg.setWbBalance(1982);
        arg.setWalletOwnerOrg("BOCHK");

        ResponseStruct<TxExecuteResult> responseStruct = clearingBankClient.setClearingStatus(orgId, arg);

//        log.info("respObj body : {}" + responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            if (responseStruct.getResult().isSucc()) {
//                log.info("status : setClearingStatus succ!");
//            } else {
//                log.info("status : setClearingStatus failed!");
//            }
//        } else {
//            log.info("status : setClearingStatus failed!");
//        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult().isSucc());

    }

    public void testRequestPayment() {

        String toOrgId = "BOCHK";
        DeductMoneyNotifyArgs arg = new DeductMoneyNotifyArgs();
        arg.setPaymentAuthCode("paymentAuthCodeTest0001");
        arg.setAbBizNo("abBizNoTest0001");
        arg.setMerchantId("merchantIdTest0001");
        arg.setMerchantName("merchantNameTest0001");
        arg.setMerchantSvcId("merchantSvcIdTest0001");
        arg.setMerchantSvcName("merchantSvcNameTest0001");
        arg.setMsBizNo("msBizNoTest0001");
        arg.setRmbAmt(10000);
        
        ResponseStruct<DeductMoneyNotifyResult> responseStruct = acquirerBankClient.requestPayment(toOrgId, arg);

        log.info("respObj body : {}" + responseStruct);
        if (0 == responseStruct.getErrorCode()) {
            if (0 == responseStruct.getResult().getErrorCode()) {
                log.info("status : requestPayment succ!");
            } else {
                log.info("status : requestPayment failed!");
            }
        } else {
            log.info("status : requestPayment failed!");
        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());

    }

    public void testRecordAccountsPayable(String ibBizNo, String abBizNo, String msBizNo) {

        String date = "20170190";
        String fromUserName = "Aleex Zhang";
        String toOrg = "WB";
        String toUserId = "merchantIdTest0001";
        String toUserName = "0xe5beaee4bc97e4b98be59b9be58d81e4ba94";

        Long txTime = 1212323232L;
        String userAppIp = "119.29.57.154";
        TradeType tradeType = TradeType.TYPE_PAYMENT_AUTH_CODE;

        RecordAccountsPayableArgs transactionArgs = new RecordAccountsPayableArgs();
        transactionArgs.setDate(date);

        transactionArgs.setIbBizNo(ibBizNo);
        transactionArgs.setAbBizNo(abBizNo);
        transactionArgs.setMsBizNo(msBizNo);
        transactionArgs.setOrgBizNo("");

        transactionArgs.setUserAccount(this.account);
        transactionArgs.setUserName(this.userName);
//        transactionArgs.setFromOrg(fromOrg);
        //        transactionArgs.setToOrg(toOrg);
//        transactionArgs.setMerchantId(this.merchantId);
//        transactionArgs.setMerchantName(this.merchantName);
        transactionArgs.setMerchantId(this.merchantId);
        transactionArgs.setMerchantName(this.merchantName);

        transactionArgs.setTxType(txType);
        transactionArgs.setIsStrike(isStrike);
        transactionArgs.setRmbAmt(this.rmbAmt);
        transactionArgs.setFxRate(fxRate);
        transactionArgs.setTxAmt(this.txAmt);
        transactionArgs.setCurrency(CurrencyType.CURRENCY_TYPE_HKD);
//        transactionArgs.setCurrency(1);

        transactionArgs.setTxTime(txTime);
        transactionArgs.setUserAppIp(userAppIp);
        transactionArgs.setTradeType(tradeType);

        long start = System.currentTimeMillis();
        log.info("recordAccountsPayable start : {}", start);

        ResponseStruct<RecordAccountsPayableResult> responseStruct = issuingBankClient.recordAccountsPayable(transactionArgs);

        long end = System.currentTimeMillis();
        log.info("recordAccountsPayable latency : {}", end - start);

        log.info("respObj body : {}", responseStruct);

        boolean isSucc;
        if (RecordAccountsPayableResultType.SUCCESS == responseStruct.getResult().getResultType()) {

            log.info("status : recordAccountsPayable success");
            isSucc = true;

        } else if (RecordAccountsPayableResultType.UNKNOWN == responseStruct.getResult().getResultType()) {

            log.info("status : recordAccountsPayable unknown");
            isSucc = false;

        } else {
            log.info("status : recordAccountsPayable failed");
            isSucc = false;
        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, isSucc);
    }

    public void testPlaceOrder(String ibBizNo, String merchantSvcId, String merchantSvcName, String merchantId, String merchantName) {

        PlaceOrderArgs arg = new PlaceOrderArgs();
        arg.setMerchantSvcId(merchantSvcId);
        arg.setMerchantSvcName(merchantSvcName);
        arg.setMerchantId(merchantId);
        arg.setMerchantName(merchantName);

        arg.setIbBizNo(ibBizNo);
        arg.setUserAccount(this.account);
        arg.setUserName(this.userName);
        arg.setRmbAmt(this.rmbAmt);
        arg.setMessage("test interface!");

        long start = System.currentTimeMillis();
        log.info("placeOrder start : {}", start);
        
        ResponseStruct<PlaceOrderResult> responseStruct = issuingBankClient.placeOrder(arg);
        long end = System.currentTimeMillis();
        log.info("placeOrder latency : {}", end - start);

        if (0 == responseStruct.getErrorCode()) {
            if (0 == responseStruct.getResult().getErrorCode()) {

                String abBizNo = responseStruct.getResult().getAbBizNo();
                String msBizNo = responseStruct.getResult().getMsBizNo();
                this.testRecordAccountsPayable(ibBizNo, abBizNo, msBizNo);
            } else {
            }
        } else {
        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());
    }

    public void testReportFailedStatus(String abBizNo) {

        ReportFailedStatusArgs arg = new ReportFailedStatusArgs();
        arg.setErrorCode(-100);
        arg.setAbBizNo("3443");
//        arg.setTradeType(TradeType.TYPE_MERCHANT_ID_QRCODE);
        arg.setMessage("test interface!");

        ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = issuingBankClient.reportFailedStatus(arg);

//        log.info("respObj body : {}" + responseStruct);
//        if (0 == responseStruct.getErrorCode()) {
//            if (0 == responseStruct.getResult().getErrorCode()) {
//                log.info("status : succ!");
//            } else {
//                log.info("status : failed!");
//            }
//        } else {
//            log.info("status : failed!");
//        }

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());
    }

//    public void testGetTransList() {
//        GetTransListArgs transactionArgs = new GetTransListArgs();
//        transactionArgs.setOrgId(this.orgId);
//        transactionArgs.setUserAccount(this.accountIncludeTransList);
//        transactionArgs.setNum(20);
//        transactionArgs.setCurrPage(0);
//        ResponseStruct<TransactionQueryResult> response = issuingBankClient.getTransList(transactionArgs);
//
//        log.info("respObj body : {}", response);
//
//        if (0 == response.getErrorCode()) {
//            this.printArrayList(response.getResult());
//        }
//    }

    public void testGetClearingTrans() {
        GetClearingTransArgs transactionArgs = new GetClearingTransArgs();
        transactionArgs.setOrgId(this.orgId);
//        transactionArgs.setCheckCode(Application.checkCodeIncludeSomeTrans);//19643
        transactionArgs.setCheckCode(2018100401);
        transactionArgs.setNum(20);
        transactionArgs.setOffset(0);
        ResponseStruct<GetClearingTransResult> response = issuingBankClient.getClearingTrans(transactionArgs);

        log.info("respObj body : {}", response);

        if (0 == response.getErrorCode()) {
            System.out.println(response.getResult());
        }
    }

    private int stressTestDotransacion() {

        String content;
        String filePath = "/tmp/transactionList";

        FileWriter fw = null;
        BufferedWriter bw = null;
        JsonMapper mapper = JsonMapper.nonDefaultMapper();
        try {
            fw = new FileWriter(filePath, true);
            bw = new BufferedWriter(fw);

//            String stat = String.valueOf(arrTrans.size()) + "|" + checkCode + "|0";//统计信息
            bw.write("start");
            bw.newLine();

            String toOrgId = "BOCHK";
            for (int i = 0; i < 1000; i++ ) {
                DoTransactionArgs transactionArgs = constructDoTransactionArg();

                //5647
                ResponseStruct<DoTransactionResult> responseStruct = acquirerBankClient.doTransaction(toOrgId, transactionArgs);

                String result = transactionArgs.getStringArgsInner().getAbBizNo()
                        + " | "
                        + responseStruct.getErrorCode()
                        + " | "
                        + responseStruct.getResult().getErrorCode();
                bw.write(result);
                bw.newLine();
            }

            bw.write("end");

            bw.flush();
            fw.flush();
            bw.close();
            fw.close();
            System.exit(0);

            return 0;
        } catch (Exception e) {
            log.error("write local file fail. exception is={} {}", e.toString(), e.getStackTrace());
//            if (bw != null) {
////                bw.close();
//            }
//            if (fw != null) {
////                fw.close();
//            }
        }

        return 0;
    }

    private DoTransactionArgs constructDoTransactionArg() {
        String tempAbBizNo = String.valueOf(System.currentTimeMillis());
        System.out.println("XXXXXXXXXXXXXX abBizNo is "+abBizNo);
        abBizNo = tempAbBizNo;

        DoTransactionArgs transactionArgs = new DoTransactionArgs();
        transactionArgs.setDate("20171107");
        transactionArgs.setIbBizNo("2706061528440000004715");
        transactionArgs.setAbBizNo(abBizNo);
        transactionArgs.setMsBizNo("2706061528440000004716");
        transactionArgs.setOrgBizNo("BOCHK");
        transactionArgs.setUserAccount("6b59084d123");
        transactionArgs.setUserName("junqizhang");
        transactionArgs.setFromOrgId("BOCHK");
        transactionArgs.setToOrgId("WB");
        transactionArgs.setUserAppIp("1236546");
        transactionArgs.setReportCity("shenzhen1");
        transactionArgs.setMerchantSvcId("123456789");
        transactionArgs.setMerchantSvcName("BOCHKhaha");
        transactionArgs.setMerchantId("123654011");
        transactionArgs.setMerchantName("WeBank2");
        transactionArgs.setMerchantOrgCode("123456781111");
        transactionArgs.setMccCode("102232322232323");
        transactionArgs.setTxTime("150362807912");

        transactionArgs.setTxType(13);
        transactionArgs.setIsRefunded(0);
        transactionArgs.setRmbAmt(10001);
        transactionArgs.setTxAmt(8468);
        transactionArgs.setCurrency(13);
        transactionArgs.setFxRate(8467);
        transactionArgs.setExTimestamp(1503628079001L);
        transactionArgs.setTradeType(1);
        transactionArgs.setStoreId("");
        //        transactionArgs.setRemedyMarkWithCheckCode(4345);
        transactionArgs.setTxStatus(1);
        transactionArgs.setCheckCode(2018100501);

        return transactionArgs;
    }

    private DoTransactionArgs constructCancelTransactionArg() {
        String tempAbBizNo = String.valueOf(System.currentTimeMillis());
        System.out.println("XXXXXXXXXXXXXX abBizNo is "+abBizNo);
        abBizNo = tempAbBizNo;

        DoTransactionArgs transactionArgs = new DoTransactionArgs();
        transactionArgs.setDate("20171107");
        transactionArgs.setIbBizNo("2706061528440000004161");
        transactionArgs.setAbBizNo(abBizNo);
        transactionArgs.setMsBizNo("2706061528440000004161");
        transactionArgs.setOrgBizNo("BOCHK");
        transactionArgs.setUserAccount("6b59084d123");
        transactionArgs.setUserName("junqizhang");
        transactionArgs.setFromOrgId("BOCHK");
        transactionArgs.setToOrgId("WB");
        transactionArgs.setUserAppIp("1236546");
        transactionArgs.setReportCity("shenzhen1");
        transactionArgs.setMerchantSvcId("123456789");
        transactionArgs.setMerchantSvcName("BOCHKhaha");
        transactionArgs.setMerchantId("123654011");
        transactionArgs.setMerchantName("WeBank2");
        transactionArgs.setMerchantOrgCode("123456781111");
        transactionArgs.setMccCode("102232322232323");
        transactionArgs.setTxTime("150362807912");

        transactionArgs.setTxType(13);
        transactionArgs.setIsRefunded(0);
        transactionArgs.setRmbAmt(10001);
        transactionArgs.setTxAmt(8468);
        transactionArgs.setCurrency(13);
        transactionArgs.setFxRate(8467);
        transactionArgs.setExTimestamp(1503628079001L);
        transactionArgs.setTradeType(1);
        transactionArgs.setStoreId("");
        //        transactionArgs.setRemedyMarkWithCheckCode(4345);
        transactionArgs.setTxStatus(4);
        transactionArgs.setCheckCode(2018100501);

        return transactionArgs;
    }
    
    private DoTransactionArgs constructRefundTransactionArg() {
        String tempAbBizNo = String.valueOf(System.currentTimeMillis());
        System.out.println("XXXXXXXXXXXXXX abBizNo is "+abBizNo);
        abBizNo = tempAbBizNo;

        DoTransactionArgs transactionArgs = new DoTransactionArgs();
        transactionArgs.setDate("20171107");
        transactionArgs.setIbBizNo("2706061528440000004711");
        transactionArgs.setAbBizNo(abBizNo);
        transactionArgs.setMsBizNo("2706061528440000004711");
        transactionArgs.setOrgBizNo("BOCHK");
        transactionArgs.setUserAccount("6b59084d123");
        transactionArgs.setUserName("junqizhang");
        transactionArgs.setFromOrgId("BOCHK");
        transactionArgs.setToOrgId("WB");
        transactionArgs.setUserAppIp("1236546");
        transactionArgs.setReportCity("shenzhen1");
        transactionArgs.setMerchantSvcId("123456789");
        transactionArgs.setMerchantSvcName("BOCHKhaha");
        transactionArgs.setMerchantId("123654011");
        transactionArgs.setMerchantName("WeBank2");
        transactionArgs.setMerchantOrgCode("123456781111");
        transactionArgs.setMccCode("102232322232323");
        transactionArgs.setTxTime("150362807912");

        transactionArgs.setTxType(13);
        transactionArgs.setIsRefunded(1);
        transactionArgs.setRmbAmt(10001);
        transactionArgs.setTxAmt(8468);
        transactionArgs.setCurrency(13);
        transactionArgs.setFxRate(8467);
        transactionArgs.setExTimestamp(1503628079001L);
        transactionArgs.setTradeType(1);
        transactionArgs.setStoreId("");
        //        transactionArgs.setRemedyMarkWithCheckCode(4345);
        transactionArgs.setTxStatus(2);
        transactionArgs.setCheckCode(2017122901);

        return transactionArgs;
    }
    
    private String testDoTransaction() {
        String toOrgId = "AAAAAAAAAAAAAAAAAAAAAAAAAAABOCHK";

        String ibBizNo = String.valueOf(System.currentTimeMillis());

        DoTransactionArgs transactionArgs = constructDoTransactionArg();

        long start = System.currentTimeMillis();
        System.out.println("testDoTransaction start : " + start);
        log.info("testDoTransaction start : {}", start);

        ResponseStruct<DoTransactionResult> responseStruct = acquirerBankClient.doTransaction(toOrgId, transactionArgs);

        long end = System.currentTimeMillis();
        System.out.println("testDoTransaction latency : " + (end - start));
        log.info("testDoTransaction latency : {}", end - start);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());

        return ibBizNo;
    }

    private String testCancelTransaction() {
        String toOrgId = "BOCHK";

        String ibBizNo = String.valueOf(System.currentTimeMillis());

        DoTransactionArgs transactionArgs = constructCancelTransactionArg();

        long start = System.currentTimeMillis();
        log.info("testDoTransaction start : {}", start);

        ResponseStruct<DoTransactionResult> responseStruct = acquirerBankClient.cancelTransaction(toOrgId, transactionArgs);

        long end = System.currentTimeMillis();
        log.info("testDoTransaction latency : {}", end - start);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());

        return ibBizNo;
    }
    
    private String testRefundTransaction() {
        String toOrgId = "BOCHK";

        String ibBizNo = String.valueOf(System.currentTimeMillis());

        DoTransactionArgs transactionArgs = constructRefundTransactionArg();

        long start = System.currentTimeMillis();
        log.info("testDoTransaction start : {}", start);

        ResponseStruct<DoTransactionResult> responseStruct = acquirerBankClient.refundedTransaction(toOrgId, transactionArgs);

        long end = System.currentTimeMillis();
        log.info("testDoTransaction latency : {}", end - start);

        String funcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        printResponseStruct(funcName, responseStruct, responseStruct.getResult());

        return ibBizNo;
    }
    
    public void testGetOrderDetailByAbBizNo(String ibBizNo) {
        GetOrderDetailByAbBizNoArgs args = new GetOrderDetailByAbBizNoArgs();
        args.setAbBizNo(abBizNo);
        ResponseStruct<GetOrderDetailByAbBizResult> response = acquirerBankClient.getOrderDetailByAbBizNo(args);
        System.out.println(response);
        log.info("respObj body : {}", response);
    }

    public void testGetCCTransList() {
        GetCCTransListArgs getCCTransListArgs = new GetCCTransListArgs();
        getCCTransListArgs.setStartOffSet(0);
        getCCTransListArgs.setTransNums(20);
        getCCTransListArgs.setOrgId("BOCHK");
        ResponseStruct<GetCCTransListResult> response = regTechClient.getCCTransList(getCCTransListArgs);

        log.info("respObj body : {}", response);

        if (0 == response.getErrorCode()) {
            System.out.println(response.getResult());
        }
    }

    public void testGetBlockNumber() {
        ResponseStruct<GetBlockNumberResult> response = regTechClient.getBlockNumber("BOCHK");
        log.info("respObj body : {}", response);
    }

    public void testGetBlockByNumber() {
        ResponseStruct<GetBlockByNumberResult> response = regTechClient.getBlockByNumber("BOCHK", 19827);
        log.info("respObj body : {}", response);
    }

    public void runAcquirerBankTestCase() {

//        while (true) {
//            String ibBizNo = this.testDoTransaction();
//        }

        //5647, 5802, 6842
//        this.testSetCheckCodeTag("071106018", "");

//        this.testNotifySetCheckStatus(checkCode, "20170927");
////
//        this.testGenerateClearingTransFile(6842, null);
//
//        this.testGetOrderDetailByAbBizNo(ibBizNo);
    }

    public void runIssuingBankStressTestCase() throws InterruptedException {

        while (true) {
            long start = System.currentTimeMillis();
            log.info("stress test start : {}", start);

            MyRunnable my = new MyRunnable();

            Thread thread = new Thread(my);
            thread.setDaemon(true);
            thread.start();

            int interval = Config.getStressTestAllInterfaceInterval();
            Thread.sleep(interval);

            long end = System.currentTimeMillis();
            log.info("stress test latency : {}", end - start);
            break;
        }
//
//        this.testGetClearingStatus();

//        this.testGenerateClearingTransFile(checkCode, null);
    }

    public void runIssuingBankTestCase() throws InterruptedException {

        long start = System.currentTimeMillis();
        log.info("stress test start : {}", start);

        Application.this.testGetMerchantInfoByQrCodeString();
        Application.this.testGetExchangeRate();

        long end = System.currentTimeMillis();
        log.info("stress test latency : {}", end - start);

        this.testGetClearingStatus();

//        this.testReportFailedStatus("dfdfd");

        this.testGenerateClearingTransFile(checkCode, null);
    }

    public void runClearingBankTestCase() {

        this.testBocCallback();

        this.testSetExchangeRate();
////
        this.testSetClearingStatus();
//
        this.testGetClearingStatus();
//
        this.testNotifyClearingStatus();

        this.testGenerateClearingTransFile(checkCode, null);
    }

    private void testGetTransInfo()
    {
    	String address = "0xcf5c849de6e1b7a5e3aa64805d8703ff2efbc497";
    	
    }
    private void printTestCaseResult() {

        log.info("--------------------------- TEST RESULT ---------------------------");

        for (Map.Entry<String, Integer> entry : testResultMap.entrySet()) {

            String testResult;
            if (0 == entry.getValue()) {
                testResult = "success";
            } else {
                testResult = "failed";
            }
            log.info("{} : {}", entry.getKey(), testResult);
        }
//        testResultMap.forEach((k,v)->{
//
//            String testResult;
//            if (0 == v) {
//                testResult = "success";
//            } else {
//                testResult = "failed";
//            }
//            log.info("{} : {}", k, testResult);
//        });
        log.info("--------------------------- TEST RESULT END ---------------------------");
    }

    public class MyRunnable implements Runnable {
        public void run() {

            long start = System.currentTimeMillis();
            log.info("total start : {}", start);
            //Code
            Application.this.testGetMerchantInfoByQrCodeString();
            Application.this.testGetExchangeRate();

            String ibBizNo = String.valueOf(System.nanoTime());

            long end = System.currentTimeMillis();
            log.info("total latency : {}", end - start);
        }
    }

    public class MyRunnableDoTransaction implements Runnable {
        public void run() {
            //Code
            String ibBizNo = String.valueOf(System.currentTimeMillis());

            long start = System.currentTimeMillis();
            log.info("total start : {}", start);

            Application.this.testDoTransaction();

            long end = System.currentTimeMillis();
            log.info("total latency : {}", end - start);
        }
    }
}
