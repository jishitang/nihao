package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 *
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetClearingTransArgs implements IArgs {
    /**
     * 发卡行在链上的唯一标示，由链提前分配好。
     */
    @IndexField(index = 0)
    private String orgId;

    /**
     * 对账码
     */
    @IndexField(index = 1)
    private Integer checkCode;

    /**
     * 此次拉取的开始offset
     */
    @IndexField(index = 2)
    private Integer offset;

    /*
     * 一次拉取的交易数，不能超过20
     */
    @IndexField(index = 3)
    private Integer num;
}
