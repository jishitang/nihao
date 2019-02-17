package cn.webank.demo;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.constants.TradeType;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.directroute.callback.DirectRouteCallback;
//import com.sun.org.apache.regexp.internal.RE;

/**
 * Created by gmzer on 08/07/2017.
 */
public class WbDirectRouteCallback extends DirectRouteCallback {

    /**
     * 请求扣款的通知的回调，透传了支付授权码
     *
     * @param args 发卡行传递过来的消息包体
     * @return 返回给发送端的包体，必须回复，否则发送端会因收不到回复而超时
     */
    @Override
    public GetMerchantInfoResult onPush(GetMerchantInfoArgs args) {

        GetMerchantInfoResult msgBodyObj = new GetMerchantInfoResult();
        msgBodyObj.setMerchantId("testMechantId001");
        msgBodyObj.setMerchantName("testMerchantName001");
        msgBodyObj.setMerchantSvcId("testMerchantSvcId001");
        msgBodyObj.setMerchantSvcName("testMerchantSvcName001");
        msgBodyObj.setExtraData("test extra data");
        msgBodyObj.setMessage("test success!");
        return msgBodyObj;
    }

    @Override
    public PlaceOrderResult onPush(PlaceOrderArgs args) {

        PlaceOrderResult msgBodyObj = new PlaceOrderResult();
        msgBodyObj.setErrorCode(0);
        msgBodyObj.setAbBizNo("abBizNoTest001");
        msgBodyObj.setIbBizNo("ibBizNoTest001");
        msgBodyObj.setMsBizNo("msBizNoTest001");
        msgBodyObj.setMessage("test success");
        return msgBodyObj;
    }

    @Override
    public RecordAccountsPayableResult onPush(RecordAccountsPayableArgs args) {
        RecordAccountsPayableResult msgBodyObj = new RecordAccountsPayableResult();
        CurrencyType currencyType = args.getCurrency();
        TradeType tradeType = args.getTradeType();
        msgBodyObj.setErrorCode(0);
        msgBodyObj.setMessage("test success");
        return msgBodyObj;
    }

    @Override
    public DirectRouteNotifyMsgResult onPush(ReportFailedStatusArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        TradeType tradeType = arg.getTradeType();
        result.setErrorCode(0);
        result.setMessage("test success");
        return result;
    }

    @Override
    public DirectRouteNotifyMsgResult onPush(SetCheckCodeStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(0);
        result.setMessage("test success");
        return result;
    }

    @Override
    public DirectRouteNotifyMsgResult onPush(ClearingStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(0);
        result.setMessage("test success");
        return result;
    }
}
