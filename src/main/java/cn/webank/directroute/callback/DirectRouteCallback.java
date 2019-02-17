package cn.webank.directroute.callback;

import cn.webank.blockchain.api.accounting.callback.ClearingBankNotifyCallback;
import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.impl.payment.PushNotifyAllCallback;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.common.conf.Config;

/**
 * Created by junqizhang on 08/07/2017.
 * 业务方需要继承DirectRouteCallback，并实现需要实现的方法。
 */
public class DirectRouteCallback implements PushNotifyAllCallback, ClearingBankNotifyCallback {

    static private final String MSG_HEALTH = "I am alive!";
    static private final String ERROR_MSG_NO_OVERRIDE = "server side have not handle this type of message!";

    /**
     * 链上链下health check
     * 不需要覆盖实现
     *
     * @param arg echo arg
     * @return
     */
    public DirectRouteNotifyMsgResult onPush(CheckDirectRouteMsgHealthArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(0);
        String orgId = Config.getOrgId();
        String msg = "I am " + orgId + ", and I am alive!" + " | echo : " + arg.getMessage();
        result.setMessage(msg);
        return result;
    }

    /**
     * 请求获取商户信息的通知的回调
     * 收单行需要覆盖实现这个方法，处理境外合作行"获取商户信息"的请求
     *
     * @param arg 传递过来的消息包体
     * @return 返回给发送端的包体，不能return null，否则发送端会因收不到回复而超时
     */
    public GetMerchantInfoResult onPush(GetMerchantInfoArgs arg) {
        GetMerchantInfoResult result = new GetMerchantInfoResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    /**
     * 下单通知的回调
     * 收单行需要覆盖实现这个方法，处理境外合作行"下单"的请求
     *
     * @param arg 传递过来的消息包体
     * @return 返回给发送端的包体，不能return null，否则发送端会因收不到回复而超时
     */
    public PlaceOrderResult onPush(PlaceOrderArgs arg) {
        PlaceOrderResult result = new PlaceOrderResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    /**
     * 记账通知的回调
     * 收单行需要覆盖实现这个方法，处理境外合作行"记账"的请求
     *
     * @param arg 传递过来的消息包体
     * @return 返回给发送端的包体，不能return null，否则发送端会因收不到回复而超时
     */
    public RecordAccountsPayableResult onPush(RecordAccountsPayableArgs arg) {
        RecordAccountsPayableResult result = new RecordAccountsPayableResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    /**
     * 请求扣款的通知的回调，透传了支付授权码
     * 境外合作行需要覆盖实现这个方法，处理来自境外合作行的"请求扣款"请求
     *
     * @param arg 传递过来的消息包体
     * @return 返回给发送端的包体，不能return null，否则发送端会因收不到回复而超时
     */
    public DeductMoneyNotifyResult onPush(DeductMoneyNotifyArgs arg) {
        DeductMoneyNotifyResult result = new DeductMoneyNotifyResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    public DirectRouteNotifyMsgResult onPush(SetCheckCodeStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    /**
     * 清算行清算完成的通知
     * 境外合作行和WeBank都需要覆盖实现这个方法
     * 清算行清算完成后，会通过这个通知通知到境外合作行，境外合作行收到这个通知后，就可以到链上拉取对账文件，随后开始对账
     *
     * @param arg notify包体
     * @return 返回给发送端的包体，不能return null，否则发送端会因收不到回复而超时; 直接回复 DirectRouteNotifyMsgResult.errorCode == 0 表明已经成功接收通知
     */
    public DirectRouteNotifyMsgResult onPush(ClearingStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

    /**
     * 境外合作行扣款过程的发生了任何错误，都通过这个接口通知到WeBank
     * WeBank需要覆盖实现这个方法
     *
     * @param arg 通知的消息包体
     * @return 返回的消息
     */
    public DirectRouteNotifyMsgResult onPush(ReportFailedStatusArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
        result.setMessage(ERROR_MSG_NO_OVERRIDE);
        return result;
    }

	/**
	 * @param args
	 * @return
	 */
	public DirectRouteNotifyMsgResult onPush(CancelTransactionNotifyArgs args) {
		 DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
		 result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
		 result.setMessage(ERROR_MSG_NO_OVERRIDE);
		 return result;
	}

	/**
	 * @param args
	 * @return
	 */
	public DirectRouteNotifyMsgResult onPush(RefundTransactionNotifyArgs args) {
		 DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
		 result.setErrorCode(ErrorCode.DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE);
		 result.setMessage(ERROR_MSG_NO_OVERRIDE);
		 return result;
	}
}
