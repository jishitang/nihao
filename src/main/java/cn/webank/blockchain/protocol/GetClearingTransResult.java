package cn.webank.blockchain.protocol;

import java.util.List;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */

//import java.util.ArrayList;

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class GetClearingTransResult {

	@IndexField(index = 0)
	private int status;
	
	@IndexField(index = 1)
	private int errcode;
	
	@IndexField(index = 2)
	private List<Address>addressList;
}
