package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */

//import java.util.ArrayList;

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class CreateVirtualAccountResult {
    protected Integer status;
}
