package cn.webank.blockchain.spi.common.dto.args;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class ResultObj {
    private int a;
    private String b;
}
