package cn.webank.demo;

import cn.webank.blockchain.api.accounting.callback.ClearingBankNotifyCallback;
import cn.webank.blockchain.protocol.SetCheckCodeStatusNotifyArgs;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.directroute.callback.DirectRouteCallback;

/**
 * Created by junqizhang on 08/07/2017.
 */
public class BocDirectRouteCallback extends DirectRouteCallback implements ClearingBankNotifyCallback {

    /**
     * 注释见Interface
     */
    @Override
    public DirectRouteNotifyMsgResult onPush(SetCheckCodeStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(0);
        result.setMessage("test success");
        return result;
    }
}
