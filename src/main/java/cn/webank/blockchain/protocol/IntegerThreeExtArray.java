package cn.webank.blockchain.protocol;

import java.math.BigInteger;
import java.util.ArrayList;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class IntegerThreeExtArray implements IArgs {

    /*
     * 兑换汇率
     */
    @IndexField(index = 1)
    protected Integer extData1;
    /*
     * 退货时的兑换汇率
     */
    @IndexField(index = 2)
    protected Integer extData2;
    /**
     * 币种
     */
    @IndexField(index = 0)
    private Integer extData0;
    
    public ArrayList<BigInteger> tansToList()
    {
    	ArrayList<BigInteger>dataList = new ArrayList<BigInteger>(3);
    	dataList.add(BigInteger.valueOf(extData0));
    	dataList.add(BigInteger.valueOf(extData1));
    	dataList.add(BigInteger.valueOf(extData2));
    	
    	return dataList;
    }
}
