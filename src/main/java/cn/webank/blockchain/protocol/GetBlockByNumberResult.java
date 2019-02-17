package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class GetBlockByNumberResult {

    /*
     * 区块生成的时间戳
     */
    @IndexField(index = 0)
    String timestamp;

    /*
     * 当前块打包进的了多少条交易（这里的交易是指区块链的写交易，与WePOP的记账交易不是同一个意思）
     */
    @IndexField(index = 1)
    Integer transactionsCount;

    /*
     * 区块Hash
     */
    @IndexField(index = 2)
    String blockHash;
}
