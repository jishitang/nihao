package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.TradeType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 境外合作行扣款过程的发生的错误
 * 这个接口与记账接口是并列的，调用了报错接口，就不要调用记账接口；调用了记账接口，就不要调用报错接口
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
@EqualsAndHashCode(callSuper = false)
public class ReportFailedStatusArgs extends DirectRouteBaseMsgArgs implements IArgs {

    /*
     * 境外合作行扣款过程的相关错误码，通知到WeBank
     */
    private Integer errorCode;

    /*
     * 调用WeBank接口返回的失败情况，可以只上报ResponseStruct结构体里面的errorCode给WeBank
     */
    private Integer wbErrorCode;

    /*
     * 境外合作行扣款过程的相关错误message，通知到WeBank
     */
    private String message;

    /*
     * 支付类型
     * 1:主扫; 3:被扫
     */
    private Integer tradeTypeValue;

    /*
     * 发卡行交易流水号，每一笔记账需保证唯一性
     * 主扫模式下需要填
     */
    private String ibBizNo;

    /*
     * 发卡行的用户标示，用户在发卡行注册的唯一标示
     * 主扫模式下需要填
     */
    private String userAccount;

    /*
     * 收单行订单id (即WeBank订单id)
     * 被扫模式下需要填
     */
    private String abBizNo;

    public ReportFailedStatusArgs() {
        this.tradeTypeValue = TradeType.TRADE_TYPE_ERROR.getValue();
    }

    @JsonIgnore
    public TradeType getTradeType() {
        return TradeType.getType(this.tradeTypeValue);
    }

    /*
     * for json Serialize
     */
//    public void setTradeType(Integer tradeType) {
//        this.tradeType = tradeType;
//    }

    @JsonIgnore
    public void setTradeType(TradeType tradeType) {
        this.tradeTypeValue = tradeType.getValue();
    }
}
