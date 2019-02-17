package cn.webank.demo;

import cn.webank.blockchain.protocol.ClearingStatusNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyResult;
import cn.webank.blockchain.protocol.SetCheckCodeStatusNotifyArgs;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.directroute.callback.DirectRouteCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by junqizhang on 08/07/2017.
 */
public class BochkDirectRouteCallback extends DirectRouteCallback {

    private static final Logger log = LoggerFactory.getLogger(BochkDirectRouteCallback.class);

    /**
     * 注释见父类
     */
    @Override
    public DeductMoneyNotifyResult onPush(DeductMoneyNotifyArgs args) {

        log.info("received notify : " + args);
        DeductMoneyNotifyResult msgBodyObj = new DeductMoneyNotifyResult();
        msgBodyObj.setErrorCode(0);
        msgBodyObj.setMessage("received succ!");
        return msgBodyObj;
    }

    /**
     * 注释见父类
     */
    @Override
    public DirectRouteNotifyMsgResult onPush(SetCheckCodeStatusNotifyArgs arg) {
        DirectRouteNotifyMsgResult result = new DirectRouteNotifyMsgResult();
        result.setErrorCode(0);
        result.setMessage("test success");
        return result;
    }

    /**
     * 注释见父类
     */
    @Override
    public DirectRouteNotifyMsgResult onPush(ClearingStatusNotifyArgs args) {

        log.info("received notify : " + args);
        DirectRouteNotifyMsgResult msgBodyObj = new DirectRouteNotifyMsgResult();
        msgBodyObj.setErrorCode(0);
        msgBodyObj.setMessage("received succ!");
        return msgBodyObj;
    }
}
