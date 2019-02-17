package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetCCTransListArgs implements IArgs {

    @IndexField(index = 0)
    private Integer startOffSet;

    @IndexField(index = 1)
    private Integer transNums;

    private String orgId;

}
