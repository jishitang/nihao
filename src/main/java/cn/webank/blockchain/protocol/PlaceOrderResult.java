package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import cn.webank.blockchain.spi.common.dto.IResult;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class PlaceOrderResult implements IArgs, IResult {

    /**
     * 下单的错误码
     */
    private Integer errorCode;

    /**
     * 发卡行交易流水号，原封不动返回
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

    /*
     * 业务处理的错误信息
     */
    private String message;
}
