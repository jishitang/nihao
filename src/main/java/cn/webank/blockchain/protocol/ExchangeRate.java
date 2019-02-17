package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class ExchangeRate implements IArgs {

    /*
     * 币种
     */
    @IndexField(index = 0)
    private Integer currency;
    /*
     * 兑换汇率
     */
    @IndexField(index = 1)
    protected Integer fxRate;
    /*
     * 退货时的兑换汇率
     */
    @IndexField(index = 2)
    protected Integer refundFxRate;
    /*
     * 清算行上报汇率的时间戳
     */
    @IndexField(index = 3)
    protected Integer exTimestamp;
    /*
     * 汇率的单位
     */
    @IndexField(index = 4)
    protected Integer currencyUnit;
    /*
     * ext data array for integer
     */
    @IndexField(index = 5)
    protected IntegerThreeExtArray integerThreeExtArray;
    /*
     * ext data array for string
     */
    @IndexField(index = 6)
    protected StringThreeExtArray stringThreeExtArray;

    public ExchangeRate() {
        integerThreeExtArray = new IntegerThreeExtArray();
        integerThreeExtArray.setExtData0(0);
        integerThreeExtArray.setExtData1(0);
        integerThreeExtArray.setExtData2(0);

        stringThreeExtArray = new StringThreeExtArray();
        stringThreeExtArray.setExtData0("null0");
        stringThreeExtArray.setExtData1("null1");
        stringThreeExtArray.setExtData2("null2");
    }

    public CurrencyType getCurrency() {
        return CurrencyType.getType(this.currency);
    }

    public void setCurrency(CurrencyType currencyType) {
        this.currency = currencyType.getValue();
    }
}
