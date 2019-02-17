package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetOrderDetailByAbBizNoArgs implements IArgs {

    /**
     * 发卡行订单号
     */
    @IndexField(index = 0)
    private String abBizNo;

}
