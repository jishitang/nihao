package cn.webank.blockchain.api.accounting;

import cn.webank.blockchain.protocol.CancelTransactionNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyArgs;
import cn.webank.blockchain.protocol.DeductMoneyNotifyResult;
import cn.webank.blockchain.protocol.DoTransactionArgs;
import cn.webank.blockchain.protocol.DoTransactionResult;
import cn.webank.blockchain.protocol.GetOrderDetailByAbBizNoArgs;
import cn.webank.blockchain.protocol.GetOrderDetailByAbBizResult;
import cn.webank.blockchain.protocol.RefundTransactionNotifyArgs;
import cn.webank.blockchain.protocol.SetCheckCodeStatusNotifyArgs;
import cn.webank.blockchain.protocol.UpdateCheckCodeStatusResult;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;

/**
 * Created by junqizhang on 21/06/2017.
 * 收单行使用的client
 */
public interface AcquirerBankClient extends BaseClient {

    /**
     * 请求发卡行对用户进行扣款
     *
     * @param toOrgId
     * @param arg
     * @return
     */
    ResponseStruct<DeductMoneyNotifyResult> requestPayment(String toOrgId, DeductMoneyNotifyArgs arg);

    /**
     * 通过链上链下端口通知撤销订单
     *
     * @param toOrgId
     * @param arg
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> notifyCancelTransaction(String toOrgId, CancelTransactionNotifyArgs arg);
    
    
    /**
     * 通过链上通知退货订单
     *
     * @param toOrgId
     * @param arg
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> notifyRefundTransaction(String toOrgId, RefundTransactionNotifyArgs arg);
    
    
    /**
     * 场切完成后，通知其他机构的通用函数
     *
     * @param toOrgId  通知哪家机构，例如通知港中银就传入BOCHK，通知中银深圳就传入BOC
     * @param arg
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> notifySetCheckCodeStatus(String toOrgId, SetCheckCodeStatusNotifyArgs arg);

    /**
     * 记账交易写到区块链
     * @param walletOwnerOrgId  填入发卡行的机构id(orgId)
     * @param arg              传入一笔记交易的所有字段参数
     * @return
     */
    ResponseStruct<DoTransactionResult> doTransaction(String walletOwnerOrgId, DoTransactionArgs arg);


    /**
     * 撤销交易
     * @param walletOwnerOrgId 填入发卡行的机构id(orgId)
     * @param arg              传入一笔记账交易的所有字段参数
     * @return
     */
    ResponseStruct<DoTransactionResult> cancelTransaction (String walletOwnerOrgId, DoTransactionArgs arg);

    /**
     * 退货接口
     *
     * @param walletOwnerOrgId 填入发卡行的机构id(orgId）
     * @param arg              传入一笔记账交易的所有字段参数
     * @return
     */
    ResponseStruct<DoTransactionResult> refundedTransaction  (String walletOwnerOrgId, DoTransactionArgs arg);
    
    /**
     * 更新场切码状态
     * @param orgId
     * @param checkCode
     * @return
     */
    public ResponseStruct<UpdateCheckCodeStatusResult> updateCheckCodeStatus(String orgId,int checkCode);

    ResponseStruct<GetOrderDetailByAbBizResult> getOrderDetailByAbBizNo(GetOrderDetailByAbBizNoArgs getOrderDetailByAbBizNoArgs);
}
