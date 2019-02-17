package cn.webank.blockchain.impl.accounting;

import cn.webank.blockchain.api.accounting.ClearingBankClient;
import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.contracts.web3j.ClearCenter.TransRetLogEventResponse;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.protocol.ClearingStatusNotifyArgs;
import cn.webank.blockchain.protocol.ExchangeRate;
import cn.webank.blockchain.protocol.SetClearingStatusArgs;
import cn.webank.blockchain.protocol.TxExecuteResult;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.SetExchangeRateResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.common.conf.Config;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junqizhang on 2017/5/19.
 */
@Component("account.ClearingBankClientImpl")
public class ClearingBankClientImpl extends BaseIssuingBankClientImpl implements ClearingBankClient {

    private static final Logger logger = LoggerFactory.getLogger(ClearingBankClientImpl.class);

    private static final String fromOrgId = Config.getClearingOrgId();

    public ResponseStruct<TxExecuteResult> setExchangeRate(String toOrgId, ExchangeRate args){

    	System.out.println("Enter method [setExchangeRate].");
    	logger.debug("Enter method [setExchangeRate].");
    	ArrayList<BigInteger>exDataArr = args.getIntegerThreeExtArray().tansToList();
    	ArrayList<byte[]>exDataStr0 = args.getStringThreeExtArray().transToList();
		List<byte[]> exDataStr32 = new ArrayList<>();
		for (byte[] b: exDataStr0) {
			exDataStr32.add(stringToBytes32(new String(b)));
		}
    	//调用合约方法
    	RemoteCall<TransactionReceipt> f = clearCenter.setExchangeRate(BigInteger.valueOf(args.getCurrency().getValue()),
				BigInteger.valueOf(args.getFxRate()), BigInteger.valueOf(args.getRefundFxRate()),
				BigInteger.valueOf(args.getExTimestamp()),
				BigInteger.valueOf(args.getCurrencyUnit()), exDataArr, exDataStr32);
    	
    	TransactionReceipt receipt =null;
    	try {
    		receipt = f.send();
		} catch (Exception e) {
			System.out.println("Execute method with exception:{}" + e);
			logger.error("Execute method with exception:{}",e);
			return null;
		}
    	List<TransRetLogEventResponse> responseList = clearCenter.getTransRetLogEvents(receipt);
    	
    	TransRetLogEventResponse response = responseList.get(0);
    	TxExecuteResult result = new TxExecuteResult();
    	
    	result.setErrorCode(response.status.intValue());
    	result.setSucc(response.status.intValue() == SetExchangeRateResult.STATUS_SUCC);
    	result.setTxHash(receipt.getTransactionHash());

        ResponseStruct<TxExecuteResult> specificResponseStruct = new ResponseStruct<>(0,result,"Success","Success");
        specificResponseStruct.setResult(result);

        System.out.println("Exit method [setExchangeRate].");
        logger.debug("Exit method [setExchangeRate].");
        return specificResponseStruct;
    }

    public ResponseStruct<TxExecuteResult> setClearingStatus(String orgId, SetClearingStatusArgs args) {

    	RemoteCall<TransactionReceipt> f = clearCenter.setClearingStatus(BigInteger.valueOf(args.getLastCheckCode()),
				BigInteger.valueOf(args.getStatus()), args.getWalletOwnerOrg().getBytes(),
				BigInteger.valueOf(args.getTotalRmbAmt()), BigInteger.valueOf(args.getTotalTxAmt()),
				BigInteger.valueOf(args.getCurrency().getValue()), BigInteger.valueOf(args.getWbBalance()),
				BigInteger.valueOf(args.getTimestamp()));
        
    	TransactionReceipt receipt =null;
    	try {
    		receipt = f.send();
		} catch (Exception e) {
			logger.error("Execute method[setClearingStatus] with exception:{}",e);
			return null;
		}
    	List<TransRetLogEventResponse> responseList = clearCenter.getTransRetLogEvents(receipt);
    	
    	TransRetLogEventResponse response = responseList.get(0);
    	TxExecuteResult result = new TxExecuteResult();
    	int status = response.status.intValue();
    	result.setErrorCode(status);
    	result.setSucc(status == 0);
    	result.setTxHash(receipt.getTransactionHash());
    	
        ResponseStruct<TxExecuteResult> specificResponseStruct = new ResponseStruct<>();
        specificResponseStruct.setResult(result);
        specificResponseStruct.setErrorCode(status);
        return specificResponseStruct;
    }

    public ResponseStruct<DirectRouteNotifyMsgResult> notifyClearingStatusToIssuingBank(String toOrgId, ClearingStatusNotifyArgs arg) {

        return this.getImpl(
                fromOrgId,
                toOrgId,
                arg,
                ClearingStatusNotifyArgs.class,
                DirectRouteNotifyMsgResult.class,
                DirectRouteMsgType.TYPE_CLEARING_STATUS_NOTIFY,
                defaultDirectRouteRequestTimeout
        );
    }

    public ResponseStruct<DirectRouteNotifyMsgResult> notifyClearingStatusToAcquirerBank(String toOrgId, ClearingStatusNotifyArgs arg) {

        return this.getImpl(
                fromOrgId,
                acquirerOrgId,
                arg,
                ClearingStatusNotifyArgs.class,
                DirectRouteNotifyMsgResult.class,
                DirectRouteMsgType.TYPE_CLEARING_STATUS_NOTIFY,
                defaultDirectRouteRequestTimeout
        );
    }
}
