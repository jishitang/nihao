package cn.webank.blockchain.spi.common.protocols.response;

import java.util.List;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import lombok.Data;


/**
 * Created by junqizhang on 01/06/2017.
 */

//import java.util.ArrayList;

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class TransactionQueryResult  {

	List<Integer> checkCodeList;
}
