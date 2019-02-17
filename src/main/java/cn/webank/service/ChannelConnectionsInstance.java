package cn.webank.service;

import cn.webank.blockchain.impl.directroutemsg.OnNotifyCallback;
import org.fisco.bcos.channel.client.Service;
import cn.webank.common.conf.Config;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by junqizhang on 12/07/2017.
 */

public enum ChannelConnectionsInstance {
    INSTANCE;

    @Getter
    private Service directThroughLineService;

    @Getter
    private OnNotifyCallback onNotifyCallback;

    ChannelConnectionsInstance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        directThroughLineService = context.getBean(Service.class);

        Set<String> topics = new HashSet<String>();
        topics.add(Config.getOrgId());
        try {
            /*
             * TODO : 适配ancelmo的bug
             */
            directThroughLineService.setTopics(topics);
            directThroughLineService.run();
            Thread.sleep(4000);
        } catch (Exception e) {

        }

        this.onNotifyCallback = new OnNotifyCallback(null);
        this.directThroughLineService.setPushCallback(onNotifyCallback);
    }

//    public Service getService() {
//        return this.directThroughLineService;
//    }
//
//    public Service getCallback() {
//        return this.onNotifyCallback;
//    }
}
