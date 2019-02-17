package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class BlockInfo {

    @IndexField(index = 0)
    private String author;

    @IndexField(index = 1)
    private String difficulty;

    @IndexField(index = 2)
    private String extraData;

    @IndexField(index = 3)
    private String gasLimit;

    @IndexField(index = 4)
    private String gasUsed;

    @IndexField(index = 5)
    private String hash;

    @IndexField(index = 6)
    private String logsBloom;

    @IndexField(index = 7)
    private String miner;

    @IndexField(index = 8)
    private String number;

    @IndexField(index = 9)
    private String parentHash;

    @IndexField(index = 10)
    private String receiptsRoot;

    @IndexField(index = 11)
    private String sha3Uncles;

    @IndexField(index = 12)
    private String stateRoot;

    @IndexField(index = 13)
    private String timestamp;

    @IndexField(index = 14)
    private String totalDifficulty;

    @IndexField(index = 15)
    private String[] transactions;

    @IndexField(index = 16)
    private String transactionsRoot;

    @IndexField(index = 17)
    private String[] uncles;

    @IndexField(index = 18)
    private String method;

    @IndexField(index = 19)
    private Integer version;

    @IndexField(index = 20)
    private Integer sleep_time;


}
