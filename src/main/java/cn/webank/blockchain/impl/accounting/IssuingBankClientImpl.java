package cn.webank.blockchain.impl.accounting;

import cn.webank.blockchain.api.accounting.IssuingBankClient;
import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.constants.RecordAccountsPayableResultType;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.impl.payment.AuthGenerateService;
import cn.webank.blockchain.impl.payment.AuthGenerateServiceImpl;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.common.conf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by junqizhang on 2017/5/19.
 */
@Component("account.IssuingBankClientImpl")
public class IssuingBankClientImpl extends BaseIssuingBankClientImpl implements IssuingBankClient {

    private static final Logger logger = LoggerFactory.getLogger(IssuingBankClientImpl.class);

    private static final String fromOrgId = Config.getIssuingOrgId();

    private static final String toOrgId = Config.getAcquirerOrgId();

    private static final AuthGenerateService authGenerateService = new AuthGenerateServiceImpl();

    public ResponseStruct<GetMerchantInfoResult> getMerchantInfoByQrCodeString(String qrCodeString) {

        GetMerchantInfoArgs arg = new GetMerchantInfoArgs();
        arg.setMerchantQrCode(qrCodeString);

        ResponseStruct<GetMerchantInfoResult> responseStruct = this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                GetMerchantInfoArgs.class,
                GetMerchantInfoResult.class,
                DirectRouteMsgType.TYPE_GET_MERCHANT_INFO,
                defaultDirectRouteRequestTimeout
        );
        return responseStruct;
    }

    public ResponseStruct<PlaceOrderResult> placeOrder(PlaceOrderArgs arg) {

        ResponseStruct<PlaceOrderResult> responseStruct = this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                PlaceOrderArgs.class,
                PlaceOrderResult.class,
                DirectRouteMsgType.TYPE_PLACE_ORDER,
                defaultDirectRouteRequestTimeout
        );
        return responseStruct;
    }

    public ResponseStruct<RecordAccountsPayableResult> recordAccountsPayable(RecordAccountsPayableArgs arg) {

        ResponseStruct<RecordAccountsPayableResult> responseStruct = this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                RecordAccountsPayableArgs.class,
                RecordAccountsPayableResult.class,
                DirectRouteMsgType.TYPE_RECORD_ACCOUNTS_PAYABLE,
                directRouteTransactionTimeout
        );

        /*
         * 记账的result类型： 成功，失败和未知
         */
        if (0 == responseStruct.getErrorCode()) {
            //链上链下成功，再判断ats的错误码
            Integer atsErrorCode = responseStruct.getResult().getErrorCode();

            if (ErrorCode.ATS_RECORED_PAYABLE_ACCOUNT_SUCCESS.contains(atsErrorCode)) {

                responseStruct.getResult().setResultType(RecordAccountsPayableResultType.SUCCESS);

            } else if (ErrorCode.ATS_RECORED_PAYABLE_ACCOUNT_UNKNOWN.contains(atsErrorCode)) {

                responseStruct.getResult().setResultType(RecordAccountsPayableResultType.UNKNOWN);
            } else {
                responseStruct.getResult().setResultType(RecordAccountsPayableResultType.FAILED);
            }
        } else if (ErrorCode.DIRECT_ROUTE_REQUEST_TIMEOUT.equals(responseStruct.getErrorCode())) {
            //链上链下超时
            responseStruct.getResult().setResultType(RecordAccountsPayableResultType.UNKNOWN);
        } else {
            responseStruct.getResult().setResultType(RecordAccountsPayableResultType.FAILED);
        }
        return responseStruct;
    }

    public ResponseStruct<DirectRouteNotifyMsgResult> reportFailedStatus(ReportFailedStatusArgs arg) {

        ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                ReportFailedStatusArgs.class,
                DirectRouteNotifyMsgResult.class,
                DirectRouteMsgType.TYPE_REPORT_FAILED_STATUS,
                defaultDirectRouteRequestTimeout
        );
        return responseStruct;
    }


    /**
     * 生成支付授权码
     * 使用随机数函数，根据一定的规则生成
     *
     * @param userAccount 用户在链上的账号
     * @return
     */
    public String generatePaymentAuthCode(String userAccount) {
        return authGenerateService.generateCode(userAccount);
    }

    /**
     * 被扫模式下，注册接收"请求支付"的通知
     */
//    public void registerDeductMoneyNotifyCallback(PushNotifyCallback<DeductMoneyNotifyArgs> callback) {
//
////        OnNotifyCallback onNotifyCallback = new OnNotifyCallback(callback);
////
//        directThroughLineService.setPushCallback(onNotifyCallback);
//    }

//    public void registerClearingFinishNotifyCallback(PushNotifyCallback<DeductMoneyNotifyArgs> callback) {
//    }


}
