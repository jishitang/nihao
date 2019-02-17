package cn.webank.blockchain.protocol;

import java.util.List;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import lombok.Data;

/**
 *
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class GetTransListResult implements IArgs {
	
    /**
     * 交易地址列表
     */
    @IndexField(index = 0)
    private List<Address> addressList;

}
