package cn.webank.blockchain.protocol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import lombok.Data;
import lombok.ToString;

/**
 * 清算结果通知
 */
@ToString
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class DoTransactionIntArgsInner implements IArgs {
    /**
     * 扫码类型：被扫或者主扫
     */
    @IndexField(index = 0)
    private Integer txType;

    /**
     * 这笔订单是否已经被退货，默认为0
     */
    @IndexField(index = 1)
    private Integer isRefunded;

    /**
     * rmbAmt  Integer     订单的人民币金额,精度到分
     */
    @IndexField(index = 2)
    private Integer rmbAmt;

    /**
     * txAmt   Integer     付款方钱包原币扣款金额,精度到分,例如BOCHK就是传入HKD消费的金额
     */
    @IndexField(index = 3)
    private Integer txAmt;

    /**
     * currency    Integer     付款方钱包原币币种
     */
    @IndexField(index = 4)
    private Integer currency;

    /**
     * fxRate  Integer     结汇汇率,整数，精确到分
     */
    @IndexField(index = 5)
    private Integer fxRate = 0;

    /**
     * exTimestamp Long     清算行上报汇率的序列号
     */
    @IndexField(index = 6)
    private Long exTimestamp = 0L;

    /**
     * txType  Integer     交易类型(值的含义见SDK文档)
     */
    @IndexField(index = 7)
    private Integer tradeType = 0;

    /**
     * txStatus    Integer     
     * 交易状态；1：处理成功； 2：退货完成（924版本无退货功能）；3：交易已被冲正（废弃）；4：交易已经撤销(924版本无撤销功能)
     */
    @IndexField(index = 8)
    private Integer txStatus = 0;

    /**
     * 场切码,目前由ATS传入
     */
    @IndexField(index = 9)
    private Integer checkCode = 0;

    /**
     * 是否弥补交易, 正常的doTransaction使用0，弥补交易传入1
     */
    @IndexField(index = 10)
    private Integer extInt0 = 0;

    @IndexField(index = 11)
    private Integer extInt1 = 0;

    @IndexField(index = 12)
    private Integer extInt2 = 0;

    @IndexField(index = 13)
    private Integer extInt3 = 0;

    @IndexField(index = 14)
    private Integer extInt4 = 0;

    @IndexField(index = 15)
    private Integer extInt5 = 0;
    
    
    public List<BigInteger> transToList()
    {
    	List<BigInteger>dataList = new ArrayList<BigInteger>(16);
    	dataList.add(BigInteger.valueOf(txType));
    	dataList.add(BigInteger.valueOf(isRefunded));
    	dataList.add(BigInteger.valueOf(rmbAmt));
    	dataList.add(BigInteger.valueOf(txAmt));
    	dataList.add(BigInteger.valueOf(currency));
    	dataList.add(BigInteger.valueOf(fxRate));
    	dataList.add(BigInteger.valueOf(exTimestamp));
    	dataList.add(BigInteger.valueOf(tradeType));
    	dataList.add(BigInteger.valueOf(txStatus));
    	dataList.add(BigInteger.valueOf(checkCode));
    	dataList.add(BigInteger.valueOf(extInt0));
    	dataList.add(BigInteger.valueOf(extInt1));
    	dataList.add(BigInteger.valueOf(extInt2));
    	dataList.add(BigInteger.valueOf(extInt3));
    	dataList.add(BigInteger.valueOf(extInt4));
    	dataList.add(BigInteger.valueOf(extInt5));
    	
    	return dataList;
    }
}
