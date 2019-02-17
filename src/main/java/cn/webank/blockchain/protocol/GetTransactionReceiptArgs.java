package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetTransactionReceiptArgs implements IArgs {
    @IndexField(index = 0)
    private String hash;
}
