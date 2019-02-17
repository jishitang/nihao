package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class GetBlockNumberResult {

    /*
     * 块高
     */
    @IndexField(index = 0)
    Integer data;
}
