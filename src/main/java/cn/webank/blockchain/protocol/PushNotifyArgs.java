package cn.webank.blockchain.protocol;

import org.fisco.bcos.channel.dto.ChannelPush;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by junqizhang on 08/07/2017.
 */
public class PushNotifyArgs {

    /*
     * 原始结构
     */
    @Getter
    @Setter
    protected ChannelPush channelPush;
}
