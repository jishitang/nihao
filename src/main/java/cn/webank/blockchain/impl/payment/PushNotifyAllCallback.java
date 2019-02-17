package cn.webank.blockchain.impl.payment;

import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;

/**
 * Created by junqizhang on 17/5/24.
 */
public interface PushNotifyAllCallback {

    GetMerchantInfoResult onPush(GetMerchantInfoArgs args);

    PlaceOrderResult onPush(PlaceOrderArgs args);

    RecordAccountsPayableResult onPush(RecordAccountsPayableArgs args);

    DeductMoneyNotifyResult onPush(DeductMoneyNotifyArgs args);

    DirectRouteNotifyMsgResult onPush(ClearingStatusNotifyArgs args);
}
