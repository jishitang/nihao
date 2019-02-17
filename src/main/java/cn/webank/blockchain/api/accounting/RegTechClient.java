package cn.webank.blockchain.api.accounting;

import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionQueryResult;

/**
 * Created by junqizhang on 21/06/2017.
 * 监管相关使用的client
 */
public interface RegTechClient {

    /**
     * 拉取所有场切码的list
     * TODO : 是否返回除去checkCode以外更多的信息
     *
     * @param orgId 需要拉取哪一个境外合作行的场切码列表
     * @param args  分页的参数
     * @return
     */
    ResponseStruct<TransactionQueryResult> getCheckCodeList(String orgId, PageArgs args) throws Exception;

    /**
     * 根据场切码拉取这一场的清算状态
     * TODO : 目前这一场对应哪一天的数据没有
     *
     * @param checkCode
     * @return if "status" are:
     * 20100 : 传入的checkCode小于合约里面的currentCheckCode，可能是因为当前节点区块没有同步完整导致，也可能是传错了checkCode
     */
    ResponseStruct<GetClearingStatusResult> getClearingStatus(String orgId, Integer checkCode);

    /**
     * 获取块高(目前是拉取的监管节点的块高)
     * 如果能拉取到块高，证明节点进程是存在的
     *
     * @param orgId 机构id
     * @return
     */
    ResponseStruct<GetBlockNumberResult> getBlockNumber(String orgId);

    /**
     * 根据块高拉取区块信息(目前到监管节点去拉取块高)
     *
     * @param orgId       机构id
     * @param blockHeight blockHeight 块高，从1开始的连续数字，例如0，1，2，3，4......
     * @return 区块信息
     */
    ResponseStruct<GetBlockByNumberResult> getBlockByNumber(String orgId, Integer blockHeight);

    /**
     * 拉取所有交易
     *
     * @param getCCTransListArgs
     * @return
     */
    ResponseStruct<GetCCTransListResult> getCCTransList(GetCCTransListArgs getCCTransListArgs);
}
