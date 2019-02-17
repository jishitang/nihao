package cn.webank.blockchain.impl.base;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.contracts.web3j.AcquirerBank;
import cn.webank.blockchain.contracts.web3j.AcquirerBank.TransRetLogEventResponse;
import cn.webank.blockchain.contracts.web3j.ClearCenter;
import cn.webank.blockchain.contracts.web3j.IssueBank;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.spi.common.dto.BlockChainAddress;
import cn.webank.blockchain.spi.common.dto.args.IssuingBankUserAccountArgs;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionReceiptResult;
import cn.webank.blockchain.utils.Utils;
import cn.webank.common.conf.Config;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple7;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
import java.security.interfaces.ECPrivateKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by junqizhang on 2017/5/19.
 */
@Component
public class BaseIssuingBankClientImpl extends BaseClientImpl {

    protected static final Logger logger = LoggerFactory.getLogger(BaseIssuingBankClientImpl.class);
    protected static final Integer queryBigDataRetryNum = 20;
    protected static final Integer sleepTime = 3000;
    protected static final String configOrgId = Config.getOrgId();
    protected static final String clearingOrgId = Config.getClearingOrgId();
    protected static final String issuingOrgId = Config.getIssuingOrgId();
    protected static final String acquirerOrgId = Config.getAcquirerOrgId();

    protected BlockChainAddress GetClearingTransAddress;
    protected BlockChainAddress CreateVirtualAccountsAddress;
    protected BlockChainAddress DoAccountTransactionArgsAddress;
    protected BlockChainAddress GetAccountBalanceArgsAddress;
    protected BlockChainAddress IsAccountExistsArgsAddress;
    protected BlockChainAddress GetTransListArgsAddress;
    protected BlockChainAddress GetClearingStatusArgsAddress;
    protected BlockChainAddress DoTransactionArgsAddress;
    protected BlockChainAddress GetExchangeRateAddress;
    protected BlockChainAddress SetExchangeRateAddress;
    protected BlockChainAddress SetCheckCodeAddress;
    protected BlockChainAddress GetCCTransListAddress;
    protected BlockChainAddress SetClearingStatusAddress;
    protected BlockChainAddress GetCheckCodeListAddress;
    protected BlockChainAddress GetOrderDetailByAbBizNoAddress;
    protected BlockChainAddress GetBlockNumberAddress;
    protected BlockChainAddress GetBlockByNumberAddress;

    
    private static BigInteger gasPrice = new BigInteger("99999999999");
    private static BigInteger gasLimited = new BigInteger("9999999999999");
//    private static BigInteger initialValue = new BigInteger("0");
    
	
	private static String keyStoreFileName ="user.jks";
	private static String keyStorePassword = "123456"; 
	private static String keyPassword ="123456";
    
	protected static Web3j web3j;
	private static Credentials credentials;
	protected static ClearCenter clearCenter;
	
	protected static  AcquirerBank acquirerBank;
	
	protected static IssueBank issueBank;
	
	static
	{
		logger.info("[BaseIssuingBankClientImpl]begin to init sdk data.");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        try 
		{
			Service service = context.getBean(Service.class);
			service.run();
			ChannelEthereumService channelEthereumService = new ChannelEthereumService();
			channelEthereumService.setChannelService(service);
			web3j = Web3j.build(channelEthereumService);
			//等待初始化
			Thread.sleep(2000);
		} 
		catch (Exception e1)
		{
			logger.error("[BaseIssuingBankClientImpl]init web3j failed.");
		}
        
        try {
			credentials = loadkey(keyStoreFileName,keyStorePassword,keyPassword);
		} catch (Exception e) {
			logger.error("[loadkey]  failed.");
			web3j = null;
		}
        logger.info("[BaseIssuingBankClientImpl]ClearCenter init data :[clearingBankContractAddress:{},web3j:{},credentials:{}]",clearingBankContractAddress,web3j,credentials);
        clearCenter = ClearCenter.load(clearingBankContractAddress, web3j, credentials, gasPrice, gasLimited);
        acquirerBank = AcquirerBank.load(acquirerBankContractAddress, web3j, credentials, gasPrice, gasLimited);
        issueBank = IssueBank.load(issuingBankContractAddress, web3j, credentials, gasPrice, gasLimited);
	}
	
	public static Credentials loadkey(String keyStoreFileName,String keyStorePassword, String keyPassword) throws Exception{
    	InputStream ksInputStream = null;
    	try {
    		 KeyStore ks = KeyStore.getInstance("JKS");
    		 ksInputStream =  BaseIssuingBankClientImpl.class.getClassLoader().getResourceAsStream(keyStoreFileName);
    		 ks.load(ksInputStream, keyStorePassword.toCharArray());
    		 Key key = ks.getKey("ec", keyPassword.toCharArray());
    		 ECKeyPair keyPair = ECKeyPair.create(((ECPrivateKey) key).getS());
    		 Credentials credentials = Credentials.create(keyPair);	
    		 if(credentials!=null){
    		    return credentials;
    		 }else{
    			 logger.error("[loadkey]  invaild input.");
    		 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			ksInputStream.close();
		}
	    return null;
    }
	
//    AuthGenerateService authGenerateService = new AuthGenerateServiceImpl();

    public BaseIssuingBankClientImpl() {
//        this.CreateVirtualAccountsAddress = BlockChainAddress.of(
//                this.issuingBankContractAddress,
//                "callContract",
//                "createVirtualAccount"
//        );
//        this.DoAccountTransactionArgsAddress = BlockChainAddress.of(
//                this.issuingBankContractAddress,
//                "callContract",
//                "doAccountTransaction"
//        );
//        this.GetAccountBalanceArgsAddress = BlockChainAddress.of(
//                this.issuingBankContractAddress,
//                "callContract",
//                "getAccountBalance"
//        );
//        this.IsAccountExistsArgsAddress = BlockChainAddress.of(
//                this.issuingBankContractAddress,
//                "callContract",
//                "isAccountExists"
//        );
//        this.GetTransListArgsAddress = BlockChainAddress.of(
//                this.issuingBankContractAddress,
//                "getTransList",
//                "getTransList"
//        );
//        /*
//         * 需要使用清算行的合约地址。
//         */
//        this.GetClearingStatusArgsAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "callContract",
//                "getClearingStatus"
//        );
//        /*
//         * 发卡行获取场切流水
//         */
//        this.GetClearingTransAddress = BlockChainAddress.of(
//                this.acquirerBankContractAddress,
//                "getClearingTrans",
//                "getClearingTrans"
//        );
//        /*
//         * 记账模式下，发卡行交易记账
//         */
//        this.DoTransactionArgsAddress = BlockChainAddress.of(
//                this.acquirerBankContractAddress,
//                "callContract",
//                "doTransaction"
//        );
//        /*
//         * 从合约拉取汇率
//         */
//        this.GetExchangeRateAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "callContract",
//                "getExchangeRate"
//        );
//        /*
//         * 汇率上链
//         */
//        this.SetExchangeRateAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "callContract",
//                "setExchangeRate"
//        );
//        /*
//         * 设置场切
//         */
//        this.SetCheckCodeAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "callContract",
//                "setCheckCodeTag"
//        );
//
//        this.GetCCTransListAddress = BlockChainAddress.of(
//                this.acquirerBankContractAddress,
//                "getCCTransList",
//                "getCCTransList"
//        );
//
//        /**
//         * 设置清算状态
//         */
//        this.SetClearingStatusAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "callContract",
//                "setClearingStatus"
//        );
//
//        /*
//         * 拉取指定场切码的状态
//         */
//        this.GetCheckCodeListAddress = BlockChainAddress.of(
//                this.acquirerBankContractAddress,
//                "callContract",
//                "getCheckCodeList"
//        );
//
//        /**
//         * 根据WeBank订单号获取订单
//         */
//        this.GetOrderDetailByAbBizNoAddress = BlockChainAddress.of(
//                this.acquirerBankContractAddress,
//                "callContract",
//                "getOrderDetailByAbBizNo"
//        );
//
//        /**
//         * 获取快高
//         */
//        this.GetBlockNumberAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "blockNumber",
//                "blockNumber");
//
//        this.GetBlockByNumberAddress = BlockChainAddress.of(
//                this.clearingBankContractAddress,
//                "blockInfo",
//                "blockInfo");

    }

    public BaseIssuingBankClientImpl(BlockChainAddress createVirtualAccountsAddress) {
        CreateVirtualAccountsAddress = createVirtualAccountsAddress;
    }

    public ResponseStruct<GetClearingStatusResult> getClearingStatus(String orgId, Integer checkCode){

		RemoteCall<Tuple7<BigInteger, byte[], BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> f = clearCenter.getClearingStatus(BigInteger.valueOf(checkCode));
		Tuple7<BigInteger, byte[], BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> resultList = null;
    	try {
    		resultList = f.send();
		} catch ( Exception e) {
			
			logger.error("Method [getClearingStatus] execute with exception:{}",e);
			return null;
		}
    	    
    	GetClearingStatusResult result = new GetClearingStatusResult();
    	result.setStatus(resultList.getValue1().intValue());
    	result.setWalletOwnerOrg(new String(resultList.getValue2()));
    	result.setTotalRmbAmt(resultList.getValue3().intValue());
    	result.setTotalTxAmt(resultList.getValue4().intValue());
    	result.setCurrency(resultList.getValue5().intValue());
    	result.setWbBalance(resultList.getValue6().intValue());
    	result.setTimestamp(resultList.getValue7().longValue());
    	
    	ResponseStruct<GetClearingStatusResult> responseStruct = new ResponseStruct<GetClearingStatusResult>();
    	
    	GetClearingStatusArgs transactionArgs = new GetClearingStatusArgs();
        transactionArgs.setCheckCode(checkCode);
        responseStruct.setErrorCode(0);
        responseStruct.setResult(result);
        
        if(GetClearingStatusResult.ClearingStatusType.CHECK_CODE_CLEARING_COMPLETE.getValue() == result.getStatus())
        {
        	responseStruct.setMessage("Clearing finished.");
        }
        else
        {
        	responseStruct.setMessage("Clearing not finished yet.");
        }

        return responseStruct;

    }

    public ResponseStruct<CreateVirtualAccountStatus> createVirtualAccount(CreateVirtualAccountArgs transactionArgs) {

        ResponseStruct<TransactionReceiptResult> responseStruct = this.writeBlockChainAndGetResult(
                CreateVirtualAccountsAddress,
                CreateVirtualAccountArgs.class,
                transactionArgs
        );
        CreateVirtualAccountStatus status = null;

        if (0 == responseStruct.getErrorCode()) {
            status = new CreateVirtualAccountStatus();
            if (responseStruct.getResult().getStatus().equals(CreateVirtualAccountStatus.STATUS_CREATE_VIRTUAL_ACCOUNT_SUCC)) {
                status.setCreateVirtualAccountResultSucc(true);
            } else {
                status.setCreateVirtualAccountResultSucc(false);
            }
        }
        ResponseStruct<CreateVirtualAccountStatus> specificResponseStruct = new ResponseStruct<>(responseStruct);
        specificResponseStruct.setResult(status);
        return specificResponseStruct;
    }

    public ResponseStruct<IsAccountExistResult> isAccountExists(IssuingBankUserAccountArgs transactionArgs) {

        ResponseStruct<String> responseStruct = this.query(
                IsAccountExistsArgsAddress,
                IssuingBankUserAccountArgs.class,
                transactionArgs,
                String.class
        );

        IsAccountExistResult result = null;

        if (0 == responseStruct.getErrorCode()) {
            result = new IsAccountExistResult();
            if (responseStruct.getResult().equals(IsAccountExistResult.STATUS_ACCOUNT_EXIST)) {
                result.setAccountExist(true);
            } else {
                result.setAccountExist(false);
            }
        }
        ResponseStruct<IsAccountExistResult> specificResponseStruct = new ResponseStruct<>(responseStruct);
        specificResponseStruct.setResult(result);
        return specificResponseStruct;
    }

    public ResponseStruct<GetClearingTransResult> getClearingTrans(GetClearingTransArgs transactionArgs) {

    	GetClearingTransResult result = getClearingTransResult(transactionArgs);
    	ResponseStruct<GetClearingTransResult> response = new ResponseStruct<GetClearingTransResult>();
    	if(null == result)
    	{
    		response.setErrorCode(ErrorCode.BLOCK_CHAIN_READ_TX_RETURN_NULL);
    		response.setMessage("No reponse found from blockchian.");
    		return response;
    	}
    	response.setResult(result);
    	response.setErrorCode(result.getErrcode());
//    	switch (result.getErrcode())
//    	{
//    	case ErrorCode.:
//    	}
    	if(result.getErrcode() == 0)
    	{
    		response.setMessage("success.");
    	}
        logger.info("response :" + response);
        return response;
    }
    

    private GetClearingTransResult getClearingTransResult(GetClearingTransArgs transactionArgs)
    {
    	//调用合约，返回一个错误码和地址列表
		RemoteCall<Tuple3<BigInteger, BigInteger, List<String>>> f = acquirerBank.getClearingTrans(transactionArgs.getOrgId().getBytes(), BigInteger.valueOf(transactionArgs.getCheckCode()),
				BigInteger.valueOf(transactionArgs.getOffset()), BigInteger.valueOf(transactionArgs.getNum()));
		Tuple3<BigInteger, BigInteger, List<String>> resultList = null;
    	try {
    		resultList = f.send();
		} catch (Exception e) {

			logger.error("Method [getClearingTransResult] execute with exception:{}",e);
			return null;
		}
    	
    	

    		GetClearingTransResult result = new GetClearingTransResult();
			int  status = resultList.getValue1().intValue();
			int errorCode = resultList.getValue2().intValue();
			List<Address> addresses = new ArrayList<Address>();
			for(int i = 0; i < resultList.getValue3().size(); i++) {
				addresses.add(i, new Address(resultList.getValue3().get(i)));
			}
			result.setAddressList(addresses);
			result.setErrcode(errorCode);
			result.setStatus(status);
			return result;
    }
    
    public ResponseStruct<GetTransListResult> getTransList(GetTransListArgs transactionArgs) {

        //Bytes32 org_id, Bytes32 account, Uint256 num, Uint256 curr_page
    	
//    	Bytes32 org_id = DataConvertUtils.stringToBytes32(transactionArgs.getOrgId());
//    	Bytes32 account = DataConvertUtils.stringToBytes32(transactionArgs.getUserAccount());
//    	Uint256 num = new Uint256(transactionArgs.getNum());
//    	Uint256 curr_page = new Uint256(transactionArgs.getCurrPage());
//    	
//    	Future<DynamicArray<Address>>  f = issueBank.getTransList(org_id, account, num, curr_page);
//    	
//    	DynamicArray<Address>addresses = null;
//    	try {
//    		addresses = f.get();
//		} catch (InterruptedException | ExecutionException e) {
//			logger.error("Method [getTransList] execute with exception:{}",e);
//			return null;
//		}
//    	
//    	List<Address>addressList = addresses.getValue();
//    	GetTransListResult result = new GetTransListResult();
//    	result.setAddressList(addressList);
//    	
    	ResponseStruct<GetTransListResult> respData = new ResponseStruct<GetTransListResult>();
//    	respData.setResult(result);
    	respData.setErrorCode(0);
    	
        logger.info("response :" + respData);
        return respData;
    }


    /*
     * 常见错误，如果bank.blockchain.orgId配置出错会导致取不到数据，区块链返回null,请确认是否应该填BOCHK
     */
    @SuppressWarnings("rawtypes")
	public ResponseStruct<ExchangeRate> getExchangeRate(CurrencyType currencyType){


		Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, List<BigInteger>, List<byte[]>> result =null;
    	try {
			result = clearCenter.getExchangeRate(BigInteger.valueOf(currencyType.getValue())).send();
		} catch (Exception e) {
			logger.error("Method [getExchangeRate] execute with exception:{}",e);
			return null;
		}
    	
		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setCurrency(currencyType);
		exchangeRate.setFxRate(result.getValue2().intValue());
		exchangeRate.setRefundFxRate(result.getValue3().intValue());
		exchangeRate.setExTimestamp(result.getValue4().intValue());
		exchangeRate.setCurrencyUnit(result.getValue5().intValue());
		List<BigInteger>intArray = result.getValue6();
		List<byte[]>strArray = result.getValue7();
		IntegerThreeExtArray intExtArray = new IntegerThreeExtArray();
		intExtArray.setExtData0(intArray.get(0).intValue());
		intExtArray.setExtData1(intArray.get(1).intValue());
		intExtArray.setExtData2(intArray.get(2).intValue());
		exchangeRate.setIntegerThreeExtArray(intExtArray);
		StringThreeExtArray strExtArray = new StringThreeExtArray();
		strExtArray.setExtData0(new String(strArray.get(0)));
		strExtArray.setExtData1(new String(strArray.get(1)));
		strExtArray.setExtData2(new String(strArray.get(2)));
		exchangeRate.setStringThreeExtArray(strExtArray);
		ResponseStruct<ExchangeRate>responseStruct = new ResponseStruct<ExchangeRate>();
		
		responseStruct.setResult(exchangeRate);
		responseStruct.setErrorCode(0);
		responseStruct.setMessage("Finished");
		responseStruct.setRawResponse("Success.");
//    	
    	return responseStruct;
    }

    public ResponseStruct<DoTransactionResult> doTransaction(String toOrgId, DoTransactionArgs transactionArgs){
    	
    	logger.debug("[SDK:doTransaction] --> begin to do transaction...");
		System.out.println("[SDK:doTransaction] --> begin to do transaction...");

    	List<byte[]> bytesArgs = transactionArgs.getStringArgsInner().transToList();
    	List<BigInteger> intArgs = transactionArgs.getIntArgsInner().transToList();
		List<byte[]> bytesArgs32 = new ArrayList<>();
		for (byte[] b: bytesArgs) {
			bytesArgs32.add(stringToBytes32(new String(b)));
		}
		byte[] date32 = stringToBytes32(transactionArgs.getDate());
		RemoteCall<TransactionReceipt> f = acquirerBank.doTransaction(date32,
				bytesArgs32, intArgs);
    	TransactionReceipt receipt =null;
    	try {
    		receipt = f.send();
		} catch (Exception e) {
			System.out.println("Method [doTransaction] execute with exception:{}" + e);
			logger.error("Method [doTransaction] execute with exception:{}",e);
		}
    	List<TransRetLogEventResponse> responseList = acquirerBank.getTransRetLogEvents(receipt);
    	
    	TransRetLogEventResponse response = responseList.get(0);
    	int status = response.status.intValue();
    	DoTransactionResult result = new DoTransactionResult();
    	ResponseStruct<DoTransactionResult> specificResponseStruct = new ResponseStruct<>(0,result,"","");
    	
    	result.setErrorCode(status);
    	specificResponseStruct.setErrorCode(status);
    	if(status == 0 || status == ErrorCode.ContractError.TRANSACTION_DOES_EXIST.getErrorCode())
    	{
    		result.setSucc(true);
    		System.out.println("[doTransaction]transaction [{}] has already successfully written to blockchain." + transactionArgs.getStringArgsInner().getAbBizNo());
    		logger.debug("[doTransaction]transaction [{}] has already successfully written to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    		specificResponseStruct.setMessage("[doTransaction]transaction has already successfully written to blockchain.");
    	}
    	else if (status == ErrorCode.ContractError.TRANSACTION_RMBAMT_WRONG.getErrorCode())
    	{
    		result.setSucc(false);
    		System.out.println("[doTransaction]transaction [{}] failed write to blockchain." + transactionArgs.getStringArgsInner().getAbBizNo());
    		logger.debug("[doTransaction]transaction [{}] failed write to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    		specificResponseStruct.setMessage(ErrorCode.ContractError.TRANSACTION_RMBAMT_WRONG.getMsg());
    	}
    	else
    	{
    		result.setSucc(false);
			System.out.println("[doTransaction]transaction [{}] failed write to blockchain." + transactionArgs.getStringArgsInner().getAbBizNo());
    		logger.debug("[doTransaction]transaction [{}] failed write to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    		specificResponseStruct.setMessage("[doTransaction]transaction failed to  write to blockchain.");
    	}
    	result.setTxHash(receipt.getTransactionHash());
    	
        return specificResponseStruct;
    }

	public static  byte[] stringToBytes32(String str) {
		byte[] byteValue = str.getBytes();
		byte[] byteValue32 = new byte[32];
		System.arraycopy(byteValue, 0, byteValue32, 0, byteValue.length);
		return byteValue32;
	}


	public ResponseStruct<DoTransactionResult> cancelTransaction(String walletOwnerOrgId, DoTransactionArgs transactionArgs){
    	List<byte[]>bytesArgs = transactionArgs.getStringArgsInner().transToList();
    	List<BigInteger>intArgs = transactionArgs.getIntArgsInner().transToList();
    	RemoteCall<TransactionReceipt> f = acquirerBank.cancelTransaction(transactionArgs.getDate().getBytes(),
				bytesArgs, intArgs);
    	
    	TransactionReceipt receipt =null;
    	try {
    		receipt = f.send();
		} catch (Exception e) {
			
			logger.error("Method [doTransaction] execute with exception:{}",e);
		}
    	List<TransRetLogEventResponse> responseList = acquirerBank.getTransRetLogEvents(receipt);
    	
    	TransRetLogEventResponse response = responseList.get(0);
    	DoTransactionResult result = new DoTransactionResult();
    	
    	ResponseStruct<DoTransactionResult> specificResponseStruct = new ResponseStruct<>(0,result,"","");
    	
    	int status = response.status.intValue();
    	result.setErrorCode(status);
    	specificResponseStruct.setErrorCode(status);
    	if(status == 0 || status == ErrorCode.ContractError.CANCEL_TRANSACTION_ALREADY_EXIST.getErrorCode())
    	{
    		result.setSucc(true);
    		specificResponseStruct.setMessage("Cancel transaction has already successfully written to blockchain.");
    		logger.info("Cancel transaction [{}] has already successfully written to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	else if(status == ErrorCode.ContractError.TRANSACTION_STATUS_IS_INVALID.getErrorCode())
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage(ErrorCode.ContractError.TRANSACTION_STATUS_IS_INVALID.getMsg());
    		logger.error("Failed to write canceled transaction [{}] to blockchain. transaction status is not correct.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	else if(status == ErrorCode.ContractError.ORDER_DOES_NOT_EXIST.getErrorCode())
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage(ErrorCode.ContractError.ORDER_DOES_NOT_EXIST.getMsg());
    		logger.error("Failed to write canceled transaction [{}] to blockchain. transaction does not exisit.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	else
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage("Failed to write canceled transaction to blockchain");
    		logger.error("Failed to write canceled transaction [{}] to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	result.setTxHash(receipt.getTransactionHash());
    	
        return specificResponseStruct;
	}

	public ResponseStruct<DoTransactionResult> refundedTransaction(String walletOwnerOrgId, DoTransactionArgs transactionArgs) {

    	List<byte[]>bytesArgs = transactionArgs.getStringArgsInner().transToList();
    	List<BigInteger>intArgs = transactionArgs.getIntArgsInner().transToList();
    	RemoteCall<TransactionReceipt> f = acquirerBank.refundedTransaction(transactionArgs.getDate().getBytes(),
				bytesArgs, intArgs);
    	TransactionReceipt receipt =null;
    	try {
    		receipt = f.send();
		} catch ( Exception e) {
			
			logger.error("Method [doTransaction] execute with exception:{}",e);
		}
    	DoTransactionResult result = new DoTransactionResult();
    	ResponseStruct<DoTransactionResult> specificResponseStruct = new ResponseStruct<>(0,result,"Success","Success");
    	List<TransRetLogEventResponse> responseList = acquirerBank.getTransRetLogEvents(receipt);
    	
    	TransRetLogEventResponse response = responseList.get(0);
    	
    	int status = response.status.intValue();
    	result.setErrorCode(status);
    	specificResponseStruct.setErrorCode(status);
    	if(status == 0 )
    	{
    		result.setSucc(true);
    		specificResponseStruct.setMessage("Refunded transaction has already successfully written to blockchain.");
    		logger.info("Refunded transaction [{}] has already successfully written to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	else if(status == ErrorCode.ContractError.ORIGINAL_TRANSACTION_IS_NOT_FOUND.getErrorCode())
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage(ErrorCode.ContractError.ORIGINAL_TRANSACTION_IS_NOT_FOUND.getMsg());
    		logger.error("Failed to write Refunded transaction [{}] to blockchain. original transaction is not found.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	else if(status == ErrorCode.ContractError.CANCEL_TRANSACTION_COULD_NOT_BE_REFUNDED.getErrorCode())
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage(ErrorCode.ContractError.CANCEL_TRANSACTION_COULD_NOT_BE_REFUNDED.getMsg());
    		logger.error("Transaction has already been canceled, could not be refounded.",transactionArgs.getStringArgsInner().getAbBizNo());
    	
    	}
    	else if(status == ErrorCode.ContractError.REFUND_TRANSACTION_STATUS_ERROR.getErrorCode())
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage(ErrorCode.ContractError.REFUND_TRANSACTION_STATUS_ERROR.getMsg());
    		logger.error("Transaction status is not correct for refounded.",transactionArgs.getStringArgsInner().getAbBizNo());
    	
    	}
    	else
    	{
    		result.setSucc(false);
    		specificResponseStruct.setMessage("Failed to write Refunded transaction [{}] to blockchain.");
    		logger.error("Failed to write Refunded transaction [{}] to blockchain.",transactionArgs.getStringArgsInner().getAbBizNo());
    	}
    	result.setTxHash(receipt.getTransactionHash());
    	
        return specificResponseStruct;
	}

	/* (non-Javadoc)
	 * @see cn.webank.blockchain.api.accounting.AcquirerBankClient#updateCheckCodeStatus(java.lang.String, int)
	 */
	public ResponseStruct<UpdateCheckCodeStatusResult> updateCheckCodeStatus(String orgId, int checkCode) {
		byte[] orgValue = orgId.getBytes();
		byte[] orgBytes = new byte[32];
		System.arraycopy(orgValue, 0, orgBytes, 0, orgValue.length);
		RemoteCall<TransactionReceipt>  f = clearCenter.updateCheckCodeStatus(orgBytes, BigInteger.valueOf(checkCode));
		
		TransactionReceipt receipt =null;
		try {
    		receipt = f.send();
		} catch (Exception e) {
			
			logger.error("Method [updateCheckCodeStatus] execute with exception:{}",e);
		}
		List<cn.webank.blockchain.contracts.web3j.ClearCenter.TransRetLogEventResponse> responseList = clearCenter.getTransRetLogEvents(receipt);
		cn.webank.blockchain.contracts.web3j.ClearCenter.TransRetLogEventResponse response = null;
		if(responseList.size() > 0)
		{
			response = responseList.get(0);

			UpdateCheckCodeStatusResult result = new UpdateCheckCodeStatusResult();
			int status = response.status.intValue();
			result.setStatus(status);
			result.setCheckCode(checkCode);
			ResponseStruct<UpdateCheckCodeStatusResult> resp = new ResponseStruct<UpdateCheckCodeStatusResult>();
			resp.setResult(result);
			if(status == 0)
			{
				logger.debug("[updateCheckCodeStatus] update checkcode status successfully.");
				resp.setErrorCode(0);
				resp.setMessage("update checkcode status successfully. checkcode is : "+ checkCode);
				resp.setRawResponse("update checkcode status successfully. checkcode is : "+ checkCode);
			}
			else
			{
				logger.debug("[updateCheckCodeStatus] update checkcode status failed.");
				resp.setErrorCode(status);
				resp.setMessage("update checkcode status failed. checkcode is : "+ checkCode);
				resp.setRawResponse("update checkcode status failed. checkcode is : "+ checkCode);
			}
			return resp;
		}
		return null;
	}
	
    //写本地文件接口
    private Integer writeLocalFile(Integer checkCode, ArrayList<LinkedHashMap> arrTrans, String filePath) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filePath, true);
            bw = new BufferedWriter(fw);

            String stat = String.valueOf(arrTrans.size()) + "|" + checkCode + "|0";//统计信息
            bw.write(stat);
            bw.newLine();
            if (arrTrans.isEmpty()) {
                bw.newLine();
                logger.info("no clearing record. filePath={}", filePath);
                bw.flush();
                fw.flush();
                bw.close();
                fw.close();
                return 0;
            }
            String header = Utils.getMapHeader(arrTrans.get(0));//字段名
            bw.write(header);
            bw.newLine();
            for (int i = 0; i < arrTrans.size(); i++) {
                bw.write(Utils.getMapContent(arrTrans.get(i)));
                bw.newLine();
            }
            bw.flush();
            fw.flush();
            bw.close();
            fw.close();
            return 0;
        } catch (Exception e) {
            logger.error("write local file fail. exception is={} {}", e.toString(), e.getStackTrace());
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
        logger.error("write local file fail.");
        return ErrorCode.WRITE_FILE_ERROR;
    }

    public boolean isFileExist(String filePath) {
        File f = new File(filePath);
//        if (f.isDirectory()) {
//            return ErrorCode.FILE_ALREADY_EXIST;
//        }
        if(f.exists() || f.isDirectory()) {
            // do something
            return true;
        }
        return false;
    }

    /**
     * 根据address查询订单详情
     * @param address
     * @return
     */
    private LinkedHashMap<String, String> getTransInfo(Address addr)
    {
		RemoteCall<Tuple3<byte[], List<byte[]>, List<BigInteger>>> f = acquirerBank.getTransInfo(addr.toString());

		Tuple3<byte[], List<byte[]>, List<BigInteger>> resultList = null;
    	try {
    		resultList = f.send();
		} catch ( Exception e) {
			logger.error("Method [getTransInfo] execute with exception:{}",e);
			return null;
		}

			String date = new String(resultList.getValue1()).trim();
			
			LinkedHashMap<String,String>mapResult = new LinkedHashMap<>(28);
			mapResult.put("date", date);
			
			List<byte[]> stringResult = resultList.getValue2();

			mapResult.put("ibBizNo", new String((stringResult.get(0))).trim());
			mapResult.put("abBizNo", new String(stringResult.get(1)).trim());
			mapResult.put("msBizNo", new String(stringResult.get(2)).trim());
			mapResult.put("orgBizNo", new String(stringResult.get(3)).trim());
			mapResult.put("userAccount", new String(stringResult.get(4)).trim());
			mapResult.put("userName", new String(stringResult.get(5)).trim());
			mapResult.put("fromOrgId", new String(stringResult.get(6)).trim());
			mapResult.put("toOrgId", new String(stringResult.get(7)).trim());
			mapResult.put("userAppIp", new String(stringResult.get(8)).trim());
			mapResult.put("reportCity", new String(stringResult.get(9)).trim());
			mapResult.put("merchantSvcId", new String(stringResult.get(10)).trim());
			mapResult.put("merchantSvcName", new String(stringResult.get(11)).trim());
			mapResult.put("merchantId", new String(stringResult.get(12)).trim());
			mapResult.put("merchantName", new String(stringResult.get(13)).trim());
			mapResult.put("merchantOrgCode", new String(stringResult.get(14)).trim());
			mapResult.put("mccCode", new String(stringResult.get(15)).trim());
			mapResult.put("txTime", new String(stringResult.get(16)).trim());
			mapResult.put("storeId", new String(stringResult.get(17)).trim());
		
			
			List<BigInteger>intResult = resultList.getValue3();
			
			mapResult.put("txType", intResult.get(0).toString().trim());
			mapResult.put("isRefunded", intResult.get(1).toString().trim());
			mapResult.put("rmbAmt", intResult.get(2).toString().trim());
			mapResult.put("txAmt", intResult.get(3).toString().trim());
			mapResult.put("currency", intResult.get(4).toString().trim());
			mapResult.put("fxRate", intResult.get(5).toString().trim());
			mapResult.put("exTimestamp", intResult.get(6).toString().trim());
			mapResult.put("tradeTypeValue", intResult.get(7).toString().trim());
			mapResult.put("txStatus", intResult.get(8).toString().trim());
			mapResult.put("checkCode", intResult.get(9).toString().trim());
			
			return mapResult;

    	
    }
    
    private List<LinkedHashMap>getDataList(List<Address>addresses)
    {
    	ArrayList<LinkedHashMap> totalList = new ArrayList<>();
    	for(Address addr:addresses)
    	{
//    		String address = addr.getValue().toString();
    		LinkedHashMap map = getTransInfo(addr);
    		totalList.add(map);
    	}
    	return totalList;
    	
    }
    /*
     * TODO : 目前如果传入不存在的checkCode，不会返回错误
     */
    public ResponseStruct<Integer> generateClearingTransFile(String orgId, Integer checkCode, String filePath) {

        String message = null;
        ResponseStruct<Integer> generateResponseStruct = new ResponseStruct<>();
        if (isFileExist(filePath)) {
            logger.error(ErrorCode.ContractError.CHECK_INFO_NOT_EXIST.getMsg());
            message = filePath + ", file path is already exist!";
            logger.error(message);
            generateResponseStruct.setErrorCode(ErrorCode.FILE_ALREADY_EXIST);
            generateResponseStruct.setResult(ErrorCode.FILE_ALREADY_EXIST);
            generateResponseStruct.setMessage(message);
            return generateResponseStruct;
        }

        int offset = 0;
        int pageNum = 10;
        int repeatNum = 0;
        ArrayList<LinkedHashMap> totalList = new ArrayList<>();
        while (true) {
            GetClearingTransArgs transactionArgs = new GetClearingTransArgs();
            transactionArgs.setOrgId(orgId);
            transactionArgs.setCheckCode(checkCode);
            transactionArgs.setNum(pageNum);
            transactionArgs.setOffset(offset);
            ResponseStruct<GetClearingTransResult> response = getClearingTrans(transactionArgs);
            
            if (0 == response.getErrorCode()) {

            	List<LinkedHashMap> resultList = getDataList(response.getResult().getAddressList()); 
                totalList.addAll(resultList);
                if (resultList.size() < pageNum) {
                    break;
                } else {
                    offset += pageNum;
                }
            
            } else {
                //目前是交易不存在
                if (ErrorCode.ContractError.CHECK_INFO_NOT_EXIST.getErrorCode().equals(response.getErrorCode())) {
                    logger.error(ErrorCode.ContractError.CHECK_INFO_NOT_EXIST.getMsg());
//                    generateResponseStruct.setRawResponse(response.getRawResponse());
//                    generateResponseStruct.setErrorCode(response.getErrorCode());
//                    generateResponseStruct.setResult(response.getErrorCode());
//                    generateResponseStruct.setMessage(ErrorCode.ContractError.CHECK_INFO_NOT_EXIST.getMsg());
//                    return generateResponseStruct;
                    break;
                }
                /*
                 * 拉取大文件的过程中，有时候api service直接挂掉了，会导致返回10002的错误，这里sleep一会后继续重试。
                 * 前置挂掉以后，重启后需要几秒钟才能恢复服务，所以需要多sleep一会
                 */
                if (repeatNum < queryBigDataRetryNum) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        logger.error("FATAL ERROR", e);
                    }
                    repeatNum++;
                    continue;
                }
                message = "Get clearing record failed";
                if (ErrorCode.ContractError.CHECK_CODE_TOO_LARGE.equals(response.getErrorCode())) {
                    message += " | " + ErrorCode.ContractError.CHECK_INFO_NOT_EXIST.getMsg();
                }
                logger.error("repeat num : {}", repeatNum);
                logger.error("{}. checkCode={} ret={}", message, checkCode, response);
                generateResponseStruct.setRawResponse(response.getRawResponse());
                generateResponseStruct.setErrorCode(response.getErrorCode());
                generateResponseStruct.setResult(response.getErrorCode());
                generateResponseStruct.setMessage(message);
                return generateResponseStruct;
            }
        }

        Integer ret = ErrorCode.GENERATE_CLEARING_TRANS_FILE_ERROR_UNKNOWN;
        try {
            ret = writeLocalFile(checkCode, totalList, filePath);
            if (0 == ret) {
                message = "generateClearingTransFile success!";
            } else {
                message = "generateClearingTransFile failed!";
            }
        } catch (Exception e) {
            message = "writeLocalFile file fail";
            logger.error("{}. exception is={} {}", message, e.toString(), e.getStackTrace());
        }
        generateResponseStruct.setRawResponse("");
        generateResponseStruct.setErrorCode(ret);
        generateResponseStruct.setResult(ret);
        generateResponseStruct.setMessage(message);
        return generateResponseStruct;
    }

    ResponseStruct<GetMerchantInfoResult> getMerchantInfoByQrCodeString(String qrCodeString) {
        return null;
    }

//    void registerDeductMoneyNotifyCallback(DeductMoneyNotifyCallback callback) {
//
//        directThroughLineService.setPushCallback(new OnNotifyCallback(callback));
//    }

    String generatePaymentAuthCode(String userAccount) {
        return null;
    }

    public ResponseStruct<DirectRouteNotifyMsgResult> checkDirectRouteMsgHealth(String toOrgId, CheckDirectRouteMsgHealthArgs arg) {

        String fromOrgId = Config.getOrgId();
        ;
        return this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                CheckDirectRouteMsgHealthArgs.class,
                DirectRouteNotifyMsgResult.class,
                DirectRouteMsgType.TYPE_CHECK_DIRECT_ROUTE_MSG_HEALTH,
                defaultDirectRouteRequestTimeout
        );
    }

    public ResponseStruct<DirectRouteNotifyMsgResult> notifySetCheckCodeStatus(String toOrgId, SetCheckCodeStatusNotifyArgs arg) {

        return this.getImpl(
                configOrgId,
                toOrgId,
                arg,
                SetCheckCodeStatusNotifyArgs.class,
                DirectRouteNotifyMsgResult.class,
                DirectRouteMsgType.TYPE_SET_CHECK_CODE_STATUS_NOTIFY,
                defaultDirectRouteRequestTimeout
        );
    }
}
