package cn.webank.blockchain.api.accounting;

import cn.webank.blockchain.protocol.ClearingStatusNotifyArgs;
import cn.webank.blockchain.protocol.ExchangeRate;
import cn.webank.blockchain.protocol.SetClearingStatusArgs;
import cn.webank.blockchain.protocol.TxExecuteResult;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;

/**
 * Created by junqizhang on 21/06/2017.
 * 清算行使用的client
 */
public interface ClearingBankClient extends BaseClient {

    /**
     * 上报汇率接口
     * 每一个境外合作行使用不同的合约，所以需要指定机构的id,用于区分不同的境外合作行
     *
     * @param toOrgId 填境外合作行的机构id
     * @param args
     * @return
     */
    ResponseStruct<TxExecuteResult> setExchangeRate(String toOrgId, ExchangeRate args);

    /**
     * 更新清算结果（清算结果上链）
     *
     * @param toOrgId 机构的id，表明为哪个银行做清算，例如为港中银做清算，则填"BOCHK"
     * @param args
     * @return
     */
    ResponseStruct<TxExecuteResult> setClearingStatus(String toOrgId, SetClearingStatusArgs args);

    /**
     * 清算行清算完成后，通知收单行清算完成
     *
     * @param toOrgId 目前固定填"WB",表明发通知给WeBank
     * @param arg     通知消息的body
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> notifyClearingStatusToAcquirerBank(String toOrgId, ClearingStatusNotifyArgs arg);

    /**
     * 清算行清算完成后，通知发卡行清算完成
     *
     * @param toOrgId 机构的id，表明为哪个银行做清算，例如为港中银做清算，则填"BOCHK"
     * @param arg     通知消息的body
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> notifyClearingStatusToIssuingBank(String toOrgId, ClearingStatusNotifyArgs arg);

}
