package cn.webank.blockchain.protocol;

import java.util.ArrayList;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import cn.webank.blockchain.utils.DataConvertUtils;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class StringThreeExtArray implements IArgs {

    /*
     * 兑换汇率
     */
    @IndexField(index = 1)
    protected String extData1;
    /*
     * 退货时的兑换汇率
     */
    @IndexField(index = 2)
    protected String extData2;
    /**
     * 币种
     */
    @IndexField(index = 0)
    private String extData0;
    
    public ArrayList<byte[]> transToList()
    {
    	ArrayList<byte[]>dataList = new ArrayList<byte[]>(3);
    	
    	dataList.add(extData0.getBytes());
    	dataList.add(extData1.getBytes());
    	dataList.add(extData2.getBytes());
    	
    	return dataList;
    }
}
