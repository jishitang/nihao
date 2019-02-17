package cn.webank.blockchain.impl.payment;

import org.fisco.bcos.channel.dto.ChannelResponse;

/**
 * Created by junqizhang on 17/5/24.
 */
public interface PushNotifyCallback<T> {
    ChannelResponse onPush(T args);
}
