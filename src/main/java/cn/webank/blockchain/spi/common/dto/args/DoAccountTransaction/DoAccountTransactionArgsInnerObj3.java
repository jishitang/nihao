package cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction;

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
public class DoAccountTransactionArgsInnerObj3 implements IArgs {
    /**
     * 交易类型; 前缀1：收单行交易 13：消费; 前缀2：发卡行交易 21充值，22：提现 31：退货
     */
    @IndexField(index = 0)
    private Integer txType;

    /**
     * 是否冲账;1：冲账； 0：非冲账
     */
    @IndexField(index = 1)
    private Integer isStrike;

    /**
     * 发生金额,精度到分
     */
    @IndexField(index = 2)
    private Integer amount;

    /**
     * 消费币种
     */
    @IndexField(index = 3)
    private Integer currency;
}
