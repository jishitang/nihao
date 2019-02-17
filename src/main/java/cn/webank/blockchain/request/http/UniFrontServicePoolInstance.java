package cn.webank.blockchain.request.http;

import cn.webank.blockchain.impl.directroutemsg.OnNotifyCallback;
import lombok.Getter;

import java.util.List;

/**
 * Created by Junqi Zhang on 12/07/2017.
 */

public enum UniFrontServicePoolInstance {
    INSTANCE;

    @Getter
    private UniFrontServicePool servicePool;

    @Getter
    private OnNotifyCallback onNotifyCallback;

    private boolean isInit = false;

    public void init(List<String> arr) {
        if (!isInit) {
            this.servicePool = new UniFrontServicePool(arr);
            isInit = true;
        }
    }
}
