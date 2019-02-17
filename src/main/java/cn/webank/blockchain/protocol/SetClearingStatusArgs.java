package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 * 清算结果上链
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class SetClearingStatusArgs implements IArgs {

    /*
     * 清算场次标识（对账码）
     */
    @IndexField(index = 0)
    private Integer lastCheckCode;

    /*
     * 清算结果状态, 1：完成
     */
    @IndexField(index = 1)
    private Integer status;

    /*
     * 境外合作行的机构id，用于区分不同境外合作行的清算
     * 例如为港中银的钱包清算，就填BOCHK
     */
    @IndexField(index = 2)
    private String walletOwnerOrg;

    /*
     * lastCheckCode对应的场次所有订单累加的人民币金额，精度到分
     * 为场次中所有订单的rmbAmt的和
     */
    @IndexField(index = 3)
    private Integer totalRmbAmt;

    /*
     * lastCheckCode对应的场次所有订单累加的外币金额，精度到分
     * 为场次中所有订单的totalTxAmt的和
     */
    @IndexField(index = 4)
    private Integer totalTxAmt;

    /*
     * 外汇币种，填totalTxAmt的币种
     */
    @IndexField(index = 5)
    private Integer currency;

    /*
     * WeBank备付金余额
     */
    @IndexField(index = 6)
    private Integer wbBalance;

    /*
     * 清算行成功清算的时间戳（清算行完成结售汇的时间）
     */
    @IndexField(index = 7)
    private Long timestamp;

    public CurrencyType getCurrency() {
        return CurrencyType.getType(this.currency);
    }

    public void setCurrency(CurrencyType currencyType) {
        this.currency = currencyType.getValue();
    }
}
