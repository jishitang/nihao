package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 境外合作行通过下单接口（PlaceOrder）传过来的参数，在
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
@EqualsAndHashCode(callSuper = false)
public class PlaceOrderArgs extends DirectRouteBaseMsgArgs implements IArgs {

    /*
     * 聚合支付服务商id
     */
    protected String merchantSvcId;

    /*
     * 聚合支付服务商name
     */
    protected String merchantSvcName;

    /*
     * 商户id
     */
    protected String merchantId;

    /*
     * 商户name
     */
    protected String merchantName;

    /*
     * 下单的备注信息,可选
     */
    protected String message;

    /*
     * 发卡行交易流水号，每一笔记账需保证唯一性
     */
    private String ibBizNo;

    /*
     * 发卡行的用户标示，用户在发卡行注册的唯一标示
     * 例如可以填入Wallet id
     */
    private String userAccount;

    /*
     * 用户的名字：付款方姓名/名称
     */
    private String userName;

    /*
     * 订单的人民币金额（用户输入的金额），精度到分
     */
    private Integer rmbAmt;
}
