package cn.webank.blockchain.spi.common.dto.args;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 * 清算结果通知
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetClearingTransArgsObj implements IArgs {
    /**
     * 机构标识
     */
    @IndexField(index = 0)
    private String orgId;

    /**
     * 对账码
     */
    @IndexField(index = 1)
    private String check_code;

    /**
     * 此次拉取的开始offset
     */
    @IndexField(index = 2)
    private Integer offset;

    /**
     * 一次拉取的交易数，不能超过20
     */
    @IndexField(index = 3)
    private Integer num;
}
