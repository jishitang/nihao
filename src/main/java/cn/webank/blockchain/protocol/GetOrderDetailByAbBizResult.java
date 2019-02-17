package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

@Data
//@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class GetOrderDetailByAbBizResult implements IArgs {

    /**
     * 交易日期, YYYYMMDD
     */
    @IndexField(index = 0)
    private String date;

    /**
     * ibBizNo String  20byte  境外合作行交易流水号
     */
    @IndexField(index = 1)
    private String ibBizNo;

    /**
     * abBizNo String  32byte  WeBank订单id
     */
    @IndexField(index = 2)
    private String abBizNo;

    /**
     * msBizNo String  最长64byte    聚合支付服务商订单id
     */
    @IndexField(index = 3)
    private String msBizNo;

    /**
     * orgBizNo    String  32byte  目前未使用
     */
    @IndexField(index = 4)
    private String orgBizNo;

    /**
     * userAccount String  最长32byte    境外合作行的用户id（Wallet id）
     */
    @IndexField(index = 5)
    private String userAccount;

    /**
     * userName    String  最长64byte    userName
     */
    @IndexField(index = 6)
    private String userName;
    /**
     * fromOrgId   String  最长8byte 境外合作行的机构id
     */
    @IndexField(index = 7)
    private String fromOrgId;

    /**
     * toOrgId String  最长8byte WeBank的机构id
     */
    @IndexField(index = 8)
    private String toOrgId;

    /**
     * txTime  String        订单的时间戳，精确到毫秒
     */
    @IndexField(index = 9)
    private String userAppIp;

    /**
     * userAppIp   String  最长32byte    用户消费的时候上报的用户手机ip ，需要可以判断出用户当前在哪个城市消费
     */
    @IndexField(index = 10)
    private String reportCity;

    /**
     * reportCity  String  最长32byte    用户消费的时候上报的ip对应的城市
     */
    @IndexField(index = 11)
    private String merchantSvcId;

    /**
     * merchantSvcId    String 最长32byte    聚合支付服务商id
     */
    @IndexField(index = 12)
    private String merchantSvcName;

    /**
     * merchantSvcName String      聚合支付服务商name,目前固定为空
     */
    @IndexField(index = 13)
    private String merchantId;

    /**
     * merchantId  String  32byte  商户的id
     */
    @IndexField(index = 14)
    private String merchantName;

    /**
     * merchantName    String  256byte 商户名称
     */
    @IndexField(index = 15)
    private String merchantOrgCode;

    /**
     * merchantOrgCode String  最长32byte    商户的组织机构代码
     */
    @IndexField(index = 16)
    private String mccCode;

    /**
     * mccCode String  最长16byte    商户类别
     */
    @IndexField(index = 17)
    private String txTime;

    /**
     * checkCode   Integer     场切码
     */
    @IndexField(index = 18)
    private Integer txType;

    /**
     * isStrike    Integer     目前未使用
     */
    @IndexField(index = 19)
    private Integer isStrike;

    /**
     * rmbAmt  Integer     订单的人民币金额,精度到分
     */
    @IndexField(index = 20)
    private Integer rmbAmt;

    /**
     * txAmt   Integer     付款方钱包原币扣款金额,精度到分,例如BOCHK就是传入HKD消费的金额
     */
    @IndexField(index = 21)
    private Integer txAmt;

    /**
     * currency    Integer     付款方钱包原币币种
     */
    @IndexField(index = 22)
    private Integer currency;

    /**
     * fxRate  Integer     结汇汇率,整数，精确到分
     */
    @IndexField(index = 23)
    private Integer fxRate;

    /**
     * exTimestamp Long     清算行上报汇率的序列号
     */
    @IndexField(index = 24)
    private Long exTimestamp;

    /**
     * txType  Integer     交易类型(值的含义见SDK文档)
     */
    @IndexField(index = 25)
    private Integer tradeTypeValue;

    /**
     * txStatus    Integer     交易状态；1：处理成功； 2：退货完成（924版本无退货功能）；3：交易已被冲正（废弃）；4：交易已经撤销(924版本无撤销功能)
     */
    @IndexField(index = 26)
    private Integer txStatus;

    /**
     * tradeType  Integer     支付类型(值的含义见SDK文档)
     */
    @IndexField(index = 27)
    private Integer checkCode;

}
