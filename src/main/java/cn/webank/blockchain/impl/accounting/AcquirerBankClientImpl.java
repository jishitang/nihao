package cn.webank.blockchain.impl.accounting;

import cn.webank.blockchain.api.accounting.AcquirerBankClient;
import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by junqizhang on 2017/5/19.
 */
@Component("account.AcquirerBankClientImpl")
public class AcquirerBankClientImpl extends BaseIssuingBankClientImpl implements AcquirerBankClient {

    private static final Logger logger = LoggerFactory.getLogger(AcquirerBankClientImpl.class);


    public ResponseStruct<DeductMoneyNotifyResult> requestPayment(String toOrgId, DeductMoneyNotifyArgs arg) {

        ResponseStruct<DeductMoneyNotifyResult> responseStruct = this.getImpl(
                acquirerOrgId,
                issuingOrgId,
                arg,
                DeductMoneyNotifyArgs.class,
                DeductMoneyNotifyResult.class,
                DirectRouteMsgType.TYPE_DEDUCT_MONEY_NOTIFY,
                defaultDirectRouteRequestTimeout
        );
        return responseStruct;
    }

    public ResponseStruct<GetOrderDetailByAbBizResult> getOrderDetailByAbBizNo(GetOrderDetailByAbBizNoArgs getOrderDetailByAbBizNoArgs){
		RemoteCall<Tuple3<byte[], List<byte[]>, List<BigInteger>>> f =acquirerBank.getOrderDetailByAbBizNo(BaseIssuingBankClientImpl.stringToBytes32(getOrderDetailByAbBizNoArgs.getAbBizNo()));

		Tuple3<byte[], List<byte[]>, List<BigInteger>> typeList = null;
    	try {
    		typeList = f.send();
		} catch (Exception e) {
			logger.error("Method [getOrderDetailByAbBizNo] execute with exception:{}",e);
			return null;
		}

    	ResponseStruct<GetOrderDetailByAbBizResult> responseStruct = new ResponseStruct<GetOrderDetailByAbBizResult>();
    	
    	GetOrderDetailByAbBizResult result = new GetOrderDetailByAbBizResult();
			String date = typeList.getValue1().toString();
			result.setDate(date);
			
			List<byte[]> stringResult = typeList.getValue2();

			result.setIbBizNo(new String(stringResult.get(0)));
			result.setAbBizNo(new String(stringResult.get(1)));
			result.setMsBizNo(new String(stringResult.get(2)));
			result.setOrgBizNo(new String(stringResult.get(3)));
			result.setUserAccount(new String(stringResult.get(4)));
			result.setUserName(new String(stringResult.get(5)));
			result.setFromOrgId(new String(stringResult.get(6)));
			result.setToOrgId(new String(stringResult.get(7)));
			result.setUserAppIp(new String(stringResult.get(8)));
			result.setReportCity(new String(stringResult.get(9)));
			result.setMerchantSvcId(new String(stringResult.get(10)));
			result.setMerchantSvcName(new String(stringResult.get(11)));
			result.setMerchantId(new String(stringResult.get(12)));
			result.setMerchantName(new String(stringResult.get(13)));
			result.setMerchantOrgCode(new String(stringResult.get(14)));
			result.setMccCode(new String(stringResult.get(15)));
			result.setTxTime(new String(stringResult.get(16)));
			
			List<BigInteger>intResult = typeList.getValue3();
			
			result.setTxType(intResult.get(0).intValue());
			result.setIsStrike(intResult.get(1).intValue());
			result.setRmbAmt(intResult.get(2).intValue());
			result.setTxAmt(intResult.get(3).intValue());
			result.setCurrency(intResult.get(4).intValue());
			result.setFxRate(intResult.get(5).intValue());
			result.setExTimestamp(intResult.get(6).longValue());
			result.setTradeTypeValue(intResult.get(7).intValue());
			result.setTxStatus(intResult.get(8).intValue());
			result.setCheckCode(intResult.get(9).intValue());
			
			responseStruct.setErrorCode(0);
			responseStruct.setMessage("Success.");

    	responseStruct.setResult(result);
        return responseStruct;
    }
    

	/* (non-Javadoc)
	 * @see cn.webank.blockchain.api.accounting.AcquirerBankClient#notifyCancelTransaction(java.lang.String, cn.webank.blockchain.protocol.DeductMoneyNotifyArgs)
	 */
	@Override
	public ResponseStruct<DirectRouteNotifyMsgResult> notifyCancelTransaction(String toOrgId, CancelTransactionNotifyArgs arg) {
		 ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = this.getImpl(
	                acquirerOrgId,
	                issuingOrgId,
	                arg,
	                CancelTransactionNotifyArgs.class,
	                DirectRouteNotifyMsgResult.class,
	                DirectRouteMsgType.TYPE_CANCEL_TRANSACTION_NOTIFY,
	                defaultDirectRouteRequestTimeout
	        );
	        return responseStruct;
	}
	

	/* (non-Javadoc)
	 * @see cn.webank.blockchain.api.accounting.AcquirerBankClient#notifyRefundTransaction(java.lang.String, cn.webank.blockchain.protocol.DeductMoneyNotifyArgs)
	 */
	@Override
	public ResponseStruct<DirectRouteNotifyMsgResult> notifyRefundTransaction(String toOrgId, RefundTransactionNotifyArgs arg) {
		 ResponseStruct<DirectRouteNotifyMsgResult> responseStruct = this.getImpl(
	                acquirerOrgId,
	                issuingOrgId,
	                arg,
	                RefundTransactionNotifyArgs.class,
	                DirectRouteNotifyMsgResult.class,
	                DirectRouteMsgType.TYPE_REFUND_TRANSACTION_NOTIFY,
	                defaultDirectRouteRequestTimeout
	        );
	        return responseStruct;
	}
}