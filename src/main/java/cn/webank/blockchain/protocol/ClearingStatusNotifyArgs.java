package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by junqizhang on 08/07/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ClearingStatusNotifyArgs extends DirectRouteBaseMsgArgs {

    /*
     * 清算是否完成
     */
    private boolean isSucc = false;

    /*
     * 境外合作行的机构id，用于区分不同境外合作行的清算
     * 例如为港中银的钱包清算，就填BOCHK
     */
    private String walletOwnerOrg;

    /*
     * 上一场的场切码, 也就是当前需要清算的场次的场切码
     */
    private Integer lastCheckCode;

    /*
     * lastCheckCode对应的场次所有订单累加的人民币金额，精度到分
     * 为场次中所有订单的rmbAmt的和
     */
    private Integer totalRmbAmt;

    /*
     * lastCheckCode对应的场次所有订单累加的外币金额，精度到分
     * 为场次中所有订单的totalTxAmt的和
     */
    private Integer totalTxAmt;

    /*
     * 外汇币种，填totalTxAmt的币种
     */
    private Integer currencyValue;

    /*
     * WeBank备付金余额
     */
    private Integer wbBalance;

    /*
     * 清算行成功清算的时间戳（清算行完成结售汇的时间）
     */
    private Long timestamp;

    /*
     * 额外信息
     * Optional
     */
    private String extraData;

    @JsonIgnore
    public CurrencyType getCurrency() {
        return CurrencyType.getType(this.currencyValue);
    }

    @JsonIgnore
    public void setCurrency(CurrencyType currencyType) {
        this.currencyValue = currencyType.getValue();
    }
}
