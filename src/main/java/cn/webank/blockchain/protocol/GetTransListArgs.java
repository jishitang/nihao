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
public class GetTransListArgs implements IArgs {
    /**
     * 发卡行在链上的唯一标示，由链提前分配好。
     */
    @IndexField(index = 0)
    private String orgId;

    /**
     * 发卡行用户在链上的唯一标示，要求在发卡行内唯一（可用卡号的md5值），在链下可以与用户的卡号进行映射即可
     */
    @IndexField(index = 1)
    private String userAccount;

    /*
     * 批量拉取的交易条数，默认20条
     */
    @IndexField(index = 2)
    private Integer num;

    /**
     * 当前页数，前端app当前展示到第几页。
     * 小于0默认为0
     */
    @IndexField(index = 3)
    private Integer currPage;
}
