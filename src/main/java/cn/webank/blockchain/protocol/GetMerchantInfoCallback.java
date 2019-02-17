package cn.webank.blockchain.protocol;

import cn.webank.blockchain.impl.payment.PushNotifyCallback;
import org.fisco.bcos.channel.dto.ChannelResponse;

/**
 * Created by junqizhang on 08/07/2017.
 */
public interface GetMerchantInfoCallback extends PushNotifyCallback<GetMerchantInfoArgs> {

    /**
     * 请求扣款的通知的回调，透传了支付授权码
     *
     * @param args 发卡行传递过来的消息包体
     * @return 返回给发送端的包体，必须回复，否则发送端会因收不到回复而超时
     */
    ChannelResponse onPush(GetMerchantInfoArgs args);

//    {
//
////        System.out.println("server:收到PUSH消息:" + push.getContent());
//
//        /*
//         * 接收到以后需要给发送端回包
//         */
//        ChannelResponse response = new ChannelResponse();
//        response.setContent("Hello world2!");
//        response.setErrorCode(0);
//
//        return response;
////        sendResponse(response);
//    }
}
