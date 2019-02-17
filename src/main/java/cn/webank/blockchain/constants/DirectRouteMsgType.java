package cn.webank.blockchain.constants;

import cn.webank.blockchain.protocol.CancelTransactionNotifyArgs;
import cn.webank.blockchain.protocol.CheckDirectRouteMsgHealthArgs;
import cn.webank.blockchain.protocol.ClearingStatusNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyResult;
import cn.webank.blockchain.protocol.GetMerchantInfoArgs;
import cn.webank.blockchain.protocol.GetMerchantInfoResult;
import cn.webank.blockchain.protocol.PlaceOrderArgs;
import cn.webank.blockchain.protocol.PlaceOrderResult;
import cn.webank.blockchain.protocol.RecordAccountsPayableArgs;
import cn.webank.blockchain.protocol.RefundTransactionNotifyArgs;
import cn.webank.blockchain.protocol.ReportFailedStatusArgs;
import cn.webank.blockchain.protocol.SetCheckCodeStatusNotifyArgs;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.directroute.callback.DirectRouteCallback;
import cn.webank.directroute.tool.DirectRouteBodyParser;

/**
 * Created by junqizhang on 12/06/2017.
 */
public enum DirectRouteMsgType {

    TYPE_ERROR(0),
    /*
     * 获取商户信息
     */
    TYPE_GET_MERCHANT_INFO(1),

    /*
     * 请求WeBank生成订单
     */
    TYPE_PLACE_ORDER(2),

    /*
     * 记账接口
     */
    TYPE_RECORD_ACCOUNTS_PAYABLE(3),

    /*
     * 请求支付，通知发卡行扣款
     */
    TYPE_DEDUCT_MONEY_NOTIFY(4),

    /*
     * 清算状态通知
     */
    TYPE_SET_CHECK_CODE_STATUS_NOTIFY(5),

    /*
     * 清算状态通知
     */
    TYPE_CLEARING_STATUS_NOTIFY(6),

    /*
     * 告知扣款过程中的错误
     */
    TYPE_REPORT_FAILED_STATUS(7),

    /*
     * 链上链下check health
     */
    TYPE_CHECK_DIRECT_ROUTE_MSG_HEALTH(8),

	/**
	 * 撤销订单通知
	 */
	TYPE_CANCEL_TRANSACTION_NOTIFY(9),
	
	/**
	 * 退货订单通知
	 */
	TYPE_REFUND_TRANSACTION_NOTIFY(10);
	
    private Integer value;

    private DirectRouteMsgType(Integer index) {
        this.value = index;
    }

    public Integer getValue() {
        return this.value;
    }

    public Class getMsgBodyArgsClass() {

        switch (this) {
            case TYPE_GET_MERCHANT_INFO:
                return GetMerchantInfoArgs.class;
            case TYPE_PLACE_ORDER:
                return PlaceOrderArgs.class;
            case TYPE_RECORD_ACCOUNTS_PAYABLE:
                return RecordAccountsPayableArgs.class;
            case TYPE_DEDUCT_MONEY_NOTIFY:
                return DeductMoneyNotifyArgs.class;
            case TYPE_SET_CHECK_CODE_STATUS_NOTIFY:
                return SetCheckCodeStatusNotifyArgs.class;
            case TYPE_CLEARING_STATUS_NOTIFY:
                return ClearingStatusNotifyArgs.class;
            case TYPE_REPORT_FAILED_STATUS:
                return ReportFailedStatusArgs.class;
            case TYPE_CHECK_DIRECT_ROUTE_MSG_HEALTH:
                return CheckDirectRouteMsgHealthArgs.class;
            case TYPE_CANCEL_TRANSACTION_NOTIFY:
                return CancelTransactionNotifyArgs.class;
            case TYPE_REFUND_TRANSACTION_NOTIFY:
                return RefundTransactionNotifyArgs.class;
            default:
                break;
        }

        return null;
    }

    public Class getMsgBodyResultClass() {

        switch (this) {
            case TYPE_GET_MERCHANT_INFO:
                return GetMerchantInfoResult.class;
            case TYPE_PLACE_ORDER:
                return PlaceOrderResult.class;
            case TYPE_RECORD_ACCOUNTS_PAYABLE:
                return RecordAccountsPayableResult.class;
            case TYPE_DEDUCT_MONEY_NOTIFY:
                return DeductMoneyNotifyResult.class;
            case TYPE_SET_CHECK_CODE_STATUS_NOTIFY:
                return DirectRouteNotifyMsgResult.class;
            case TYPE_CLEARING_STATUS_NOTIFY:
                return DirectRouteNotifyMsgResult.class;
            case TYPE_REPORT_FAILED_STATUS:
                return DirectRouteNotifyMsgResult.class;
            case TYPE_CHECK_DIRECT_ROUTE_MSG_HEALTH:
                return DirectRouteNotifyMsgResult.class;
            case TYPE_CANCEL_TRANSACTION_NOTIFY:
                return DirectRouteNotifyMsgResult.class;
            case TYPE_REFUND_TRANSACTION_NOTIFY:
                return DirectRouteNotifyMsgResult.class;
            default:
                return DirectRouteNotifyMsgResult.class;
        }
    }

    public String callOnPush(DirectRouteCallback directRouteCallback, String messageId, String msgBodyStr) {

        String resultBodyStr = null;

        switch (this) {
            case TYPE_GET_MERCHANT_INFO: {
                GetMerchantInfoArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, GetMerchantInfoArgs.class);
                args.setMessageId(messageId);
                GetMerchantInfoResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_PLACE_ORDER: {
                PlaceOrderArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, PlaceOrderArgs.class);
                args.setMessageId(messageId);
                PlaceOrderResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_RECORD_ACCOUNTS_PAYABLE: {
                RecordAccountsPayableArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, RecordAccountsPayableArgs.class);
                args.setMessageId(messageId);
                RecordAccountsPayableResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_DEDUCT_MONEY_NOTIFY: {
                DeductMoneyNotifyArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, DeductMoneyNotifyArgs.class);
                args.setMessageId(messageId);
                DeductMoneyNotifyResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_SET_CHECK_CODE_STATUS_NOTIFY: {
                SetCheckCodeStatusNotifyArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, SetCheckCodeStatusNotifyArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_CLEARING_STATUS_NOTIFY: {
                ClearingStatusNotifyArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, ClearingStatusNotifyArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_REPORT_FAILED_STATUS: {
                ReportFailedStatusArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, ReportFailedStatusArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_CHECK_DIRECT_ROUTE_MSG_HEALTH: {
                CheckDirectRouteMsgHealthArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, CheckDirectRouteMsgHealthArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_CANCEL_TRANSACTION_NOTIFY: {
            	CancelTransactionNotifyArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, CancelTransactionNotifyArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            case TYPE_REFUND_TRANSACTION_NOTIFY: {
            	RefundTransactionNotifyArgs args = DirectRouteBodyParser.deserializeG(msgBodyStr, RefundTransactionNotifyArgs.class);
                args.setMessageId(messageId);
                DirectRouteNotifyMsgResult result = directRouteCallback.onPush(args);
                resultBodyStr = DirectRouteBodyParser.serialize(result);
            }
            break;
            default:
//                throw
//                logger.error("no handle msg type : " + push.getContent());
                break;
        }

        return resultBodyStr;
    }
}
