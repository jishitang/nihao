package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.constants.TradeType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户输入完密码，点击确认支付后调用，表明确认这笔交易，这个请求会到区块链账本上"记账"
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
@EqualsAndHashCode(callSuper = false)
public class RecordAccountsPayableArgs extends DirectRouteBaseMsgArgs implements IArgs {

    /*
     * 交易日期, YYYYMMDD
     * 主扫 : OPTIONAL
     * 被扫 : OPTIONAL
     */
    private String date;

    /*
     * 发卡行交易订单id，每一笔记账需保证唯一性
     * 主扫 : REQUIRED
     * 被扫 : REQUIRED
     */
    private String ibBizNo;

    /*
     * 收单行订单id (即WeBank订单id)
     */
    private String abBizNo;

    /*
     * 聚合支付服务商订单id
     */
    private String msBizNo;

    /**
     * 原交易流水号，冲账交易才需要，其它交易不需要
     */
    private String orgBizNo;

    /**
     * 发卡行的标示，发卡行在清算中心注册的标示
     */
    // commented out, due to ats can use the fromOrgId field in parent class
    //  private String fromOrg;

    /*
     * 发卡行的用户标示，用户在发卡行注册的唯一标示
     * 例如可以填入Wallet id
     */
    private String userAccount;

    /**
     * 用户的名字：付款方姓名/名称
     */
    private String userName;

    /**
     * 收单行在清算中心注册的标示
     */
    // commented out, due to ats can use the fromOrgId field in parent class
    // private String toOrg;

    /**
     * 商户在收单行注册的唯一标示
     * 商户的id
     */
    private String merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 交易类型;
     * 13：消费（记账）
     * 31：退货
     */
    private Integer txType;

    /**
     * 是否冲账;1：冲账； 0：非冲账
     */
    private Integer isStrike;

    /**
     * 订单的人民币金额（用户输入的金额），精度到分
     */
    private Integer rmbAmt;

    /**
     * 付款方钱包原币扣款金额,精度到分,例如BOCHK就是传入HKD消费的金额
     * txAmt (港币金额) = 四舍五入( rmbAmt * 10000 /  fxRate )
     */
    private Integer txAmt;

    /**
     * 付款方钱包原币币种
     */
    private Integer currencyValue;

    /**
     * 结汇汇率
     */
    private Integer fxRate;

    /**
     * 清算行上报汇率的序列号
     */
    private Integer exTimestamp;

    /**
     * 订单的时间戳，精确到毫秒
     */
    private Long txTime;

    /**
     * 用户手机的ip，需要可以判断出用户当前在哪个城市消费
     */
    private String userAppIp;

    /**
     * 支付类型
     * 1:主扫; 3:被扫
     */
    private Integer tradeTypeValue;

    public RecordAccountsPayableArgs() {
        this.tradeTypeValue = TradeType.TRADE_TYPE_ERROR.getValue();
        this.currencyValue = CurrencyType.CURRENCY_TYPE_ERROR.getValue();
    }

    @JsonIgnore
    public CurrencyType getCurrency() {
        return CurrencyType.getType(this.currencyValue);
    }

    @JsonIgnore
    public void setCurrency(CurrencyType currencyType) {
        this.currencyValue = currencyType.getValue();
    }

    @JsonIgnore
    public TradeType getTradeType() {
        return TradeType.getType(this.tradeTypeValue);
    }

    @JsonIgnore
    public void setTradeType(TradeType tradeType) {
        this.tradeTypeValue = tradeType.getValue();
    }
}
