package cn.webank.blockchain.impl.directroutemsg;

import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.impl.accounting.IssuingBankClientImpl;
import cn.webank.blockchain.impl.payment.PushNotifyCallback;
import org.fisco.bcos.channel.client.ChannelPushCallback;
import org.fisco.bcos.channel.dto.ChannelPush;
import org.fisco.bcos.channel.dto.ChannelResponse;
import cn.webank.directroute.callback.DirectRouteCallback;
import cn.webank.directroute.protocol.WePopDirectPathRequestBody;
import cn.webank.directroute.tool.DirectRouteBodyParser;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by junqizhang on 08/07/2017.
 */
public class OnNotifyCallback<T> extends ChannelPushCallback {

    private static final Logger logger = LoggerFactory.getLogger(IssuingBankClientImpl.class);
    @Setter
    @Getter
    protected DirectRouteCallback directRouteCallback = new DirectRouteCallback();
    PushNotifyCallback<T> pushNotifyCallback;

    public OnNotifyCallback(PushNotifyCallback<T> callback) {
        pushNotifyCallback = callback;
    }

    /*
     * TODO : 目前onPush只有一个单线程
     */
    @Override
    public void onPush(ChannelPush push) {

        if (null == directRouteCallback) {
            ChannelResponse response = new ChannelResponse();
            response.setContent("directRouteCallback is null on server side!");
            response.setErrorCode(0);
            push.sendResponse(response);
            return;
        }
        logger.info("received ChannelPush msg : " + push.getContent());
        WePopDirectPathRequestBody wePopDirectPathRequestBody = DirectRouteBodyParser.deserializeG(push.getContent(), WePopDirectPathRequestBody.class);
        DirectRouteMsgType responseMsgType = wePopDirectPathRequestBody.getMsgType();
        String errorMsg = "";
        Class msgBodyClass = wePopDirectPathRequestBody.getMsgType().getMsgBodyArgsClass();
        Class msgResultClass = wePopDirectPathRequestBody.getMsgType().getMsgBodyResultClass();
        DirectRouteMsgType msgType = wePopDirectPathRequestBody.getMsgType();
        String resultBodyStr = msgType.callOnPush(directRouteCallback, push.getMessageID(), wePopDirectPathRequestBody.getMsgBody());

        WePopDirectPathRequestBody responseRequestBody = new WePopDirectPathRequestBody();
        responseRequestBody.setMsgBody(resultBodyStr);
        responseRequestBody.setMsgType(responseMsgType);
        String responseRequestBodyStr = DirectRouteBodyParser.serialize(responseRequestBody);

        /*
         * 接收到以后需要给发送端回包
         */
        ChannelResponse response = new ChannelResponse();
        response.setContent(responseRequestBodyStr);
        response.setErrorCode(0);
        push.sendResponse(response);
    }
}
