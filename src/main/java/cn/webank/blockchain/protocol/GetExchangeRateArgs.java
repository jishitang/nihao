package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetExchangeRateArgs implements IArgs {

    /**
     * 币种
     */
    @IndexField(index = 0)
    private Integer currencyType;
}
