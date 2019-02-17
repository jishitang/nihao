package cn.webank.blockchain.impl.directroutemsg;

import org.fisco.bcos.channel.dto.ChannelPush;
import lombok.Data;

/**
 * Created by Junqi Zhang on 04/09/2017.
 */
@Data
public class DirectRouteMsgOnPushRunnable implements Runnable {

    private ChannelPush push;

    public void run() {

    }
}