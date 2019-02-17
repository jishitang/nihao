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
public class PagingArgsObj implements IArgs {
    /**
     * 批量拉取的交易条数，默认20条
     */
    @IndexField(index = 0)
    private Integer num;

    /**
     * 前端展示的页面数，小于0默认至为0
     */
    @IndexField(index = 1)
    private Integer curr_page;
}
