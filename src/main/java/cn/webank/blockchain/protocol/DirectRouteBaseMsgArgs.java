package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;
import lombok.EqualsAndHashCode;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
@EqualsAndHashCode(callSuper = false)
public class DirectRouteBaseMsgArgs implements IArgs {

    /*
     * 消息id，用于链上链下消息去重
     */
    protected String messageId;

    /*
     * 来源机构id
     */
    protected String fromOrgId;

    /*
     * 目的机构id
     */
    protected String toOrgId;
}
