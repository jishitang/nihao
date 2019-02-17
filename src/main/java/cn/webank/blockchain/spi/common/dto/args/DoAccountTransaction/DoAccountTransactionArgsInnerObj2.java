package cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 *
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class DoAccountTransactionArgsInnerObj2 implements IArgs {

    @IndexField(index = 0)
    private String bizNo;//由发卡行生成的一个全局唯一的业务流水号

    @IndexField(index = 1)
    private String orgBizNo;//原业务流水号，异常调账时需用，正常时填空即可
}
