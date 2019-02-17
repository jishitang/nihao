package cn.webank.blockchain.api.accounting.callback;

import cn.webank.blockchain.protocol.SetCheckCodeStatusNotifyArgs;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;

/**
 * Created by junqizhang on 17/5/24.
 */
public interface ClearingBankNotifyCallback {

    /**
     * WeBank场切成功后，通知境外合作行和清算行
     * 境外合作行和清算行都需要覆盖实现这个方法
     *
     * @param arg notify包体
     * @return 直接回复 DirectRouteNotifyMsgResult.errorCode == 0 表明已经成功接收通知
     */
    DirectRouteNotifyMsgResult onPush(SetCheckCodeStatusNotifyArgs arg);
}
