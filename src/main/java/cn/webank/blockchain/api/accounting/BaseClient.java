package cn.webank.blockchain.api.accounting;

import cn.webank.blockchain.protocol.CheckDirectRouteMsgHealthArgs;
import cn.webank.blockchain.protocol.GetClearingStatusResult;
import cn.webank.blockchain.protocol.GetClearingTransArgs;
import cn.webank.blockchain.protocol.GetClearingTransResult;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.directroute.callback.DirectRouteCallback;

/**
 * Created by Junqi Zhang on 06/09/2017.
 */
public interface BaseClient {

    /**
     * 注册处理来自其他机构的通知消息的回调函数
     * 需要实现一个类，继承DirectRouteCallback，并实现对应的几个onPush函数
     */
    void registerCallback(DirectRouteCallback directRouteCallback);

    /**
     * 链上链下check heath接口
     *
     * @param toOrgId
     * @param arg
     * @return
     */
    ResponseStruct<DirectRouteNotifyMsgResult> checkDirectRouteMsgHealth(String toOrgId, CheckDirectRouteMsgHealthArgs arg);

    /**
     * 拉取特定场切码下的所有交易列表，并生成文件
     * 格式如下：
     * 第一行是头部信息，包括：本场交易的数量; 场切码。字段之间用竖线划分。
     * 第二行是字段名,字段名之间用竖线划分。
     * 从第三行开始，每一行代表一个交易，字段用竖线划分。
     * <p>
     * 异常情况：
     * 1.本场次没有任何交易：会生产文件，但只有第一行的头部信息，交易数量字段为0
     * 2.任何出错的情况：返回错误
     * <p>
     * TODO : 目前bug，需要配置orgId为BOCHK才行
     *
     * @param orgId     机构id，例如需要拉取港中银的场切交易对账文件，则填"BOCHK"
     * @param checkCode 场切码
     * @param filePath  指定一个路径，本函数会在这个路径上生成对应的文件，filePath需要指定到文件名
     * @return 0 == ResponseStruct.result 表明执行成功；其他标识失败
     */
    ResponseStruct<Integer> generateClearingTransFile(String orgId, Integer checkCode, String filePath);

    /**
     * 拉取清算的结果
     *
     * @param orgId     填发卡行orgId
     * @param checkCode 场切码
     * @return
     */
    ResponseStruct<GetClearingStatusResult> getClearingStatus(String orgId, Integer checkCode);

    /**
     * 根据场切码，按偏移量拉取交易文件
     *
     * @param transactionArgs
     * @return
     */
    ResponseStruct<GetClearingTransResult> getClearingTrans(GetClearingTransArgs transactionArgs);
}
