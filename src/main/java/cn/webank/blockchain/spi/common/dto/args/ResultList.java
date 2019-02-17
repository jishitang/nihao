package cn.webank.blockchain.spi.common.dto.args;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class ResultList {
    @IndexField(index = 0)
    private Integer element0;

    @IndexField(index = 1)
    private Integer element1;
}
