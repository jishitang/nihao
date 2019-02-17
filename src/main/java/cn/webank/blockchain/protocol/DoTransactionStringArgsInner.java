package cn.webank.blockchain.protocol;

import java.util.ArrayList;
import java.util.List;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import cn.webank.blockchain.utils.DataConvertUtils;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import lombok.Data;
import lombok.ToString;

/**
 *
 */
@ToString
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class DoTransactionStringArgsInner implements IArgs {
//	/**
//	 * 清算结果状态，1：完成
//	 */
//	public static final Integer STATUS_CLEARING_COMPLETE = Integer.valueOf(1);
//

    /**
     * ibBizNo String  20byte  境外合作行交易流水号
     */
    @IndexField(index = 0)
    private String ibBizNo;

    /**
     * abBizNo String  32byte  WeBank订单id
     */
    @IndexField(index = 1)
    private String abBizNo;

    /**
     * msBizNo String  最长64byte    聚合支付服务商订单id
     */
    @IndexField(index = 2)
    private String msBizNo;

    /**
     * orgBizNo    String  32byte  目前未使用
     */
    @IndexField(index = 3)
    private String orgBizNo;

    /**
     * userAccount String  最长32byte    境外合作行的用户id（Wallet id）
     */
    @IndexField(index = 4)
    private String userAccount;

    /**
     * userName    String  最长64byte    userName
     */
    @IndexField(index = 5)
    private String userName;
    /**
     * fromOrgId   String  最长8byte 境外合作行的机构id
     */
    @IndexField(index = 6)
    private String fromOrgId;

    /**
     * toOrgId String  最长8byte WeBank的机构id
     */
    @IndexField(index = 7)
    private String toOrgId;

    /**
     * txTime  String        订单的时间戳，精确到毫秒
     */
    @IndexField(index = 8)
    private String userAppIp;

    /**
     * userAppIp   String  最长32byte    用户消费的时候上报的用户手机ip ，需要可以判断出用户当前在哪个城市消费
     */
    @IndexField(index = 9)
    private String reportCity;

    /**
     * reportCity  String  最长32byte    用户消费的时候上报的ip对应的城市
     */
    @IndexField(index = 10)
    private String merchantSvcId;

    /**
     * merchantSvcId    String 最长32byte    聚合支付服务商id
     */
    @IndexField(index = 11)
    private String merchantSvcName;

    /**
     * merchantSvcName String      聚合支付服务商name,目前固定为空
     */
    @IndexField(index = 12)
    private String merchantId;

    /**
     * merchantId  String  32byte  商户的id
     */
    @IndexField(index = 13)
    private String merchantName;

    /**
     * merchantName    String  256byte 商户名称
     */
    @IndexField(index = 14)
    private String merchantOrgCode;

    /**
     * merchantOrgCode String  最长32byte    商户的组织机构代码
     */
    @IndexField(index = 15)
    private String mccCode;
    
    /**
     * 门店ID
     */
    @IndexField(index = 16)
    private String storeId;

    /**
     * mccCode String  最长16byte    商户类别
     */
    @IndexField(index = 17)
    private String txTime;

    @IndexField(index = 18)
    private String extStr0 = "";

    @IndexField(index = 19)
    private String extStr1 = "";

    @IndexField(index = 20)
    private String extStr2 = "";

    @IndexField(index = 21)
    private String extStr3 = "";

    @IndexField(index = 22)
    private String extStr4 = "";

    @IndexField(index = 23)
    private String extStr5 = "";

    @IndexField(index = 24)
    private String extStr6 = "";
    
    public List<byte[]> transToList()
    {
    	List<byte[]>dataList = new ArrayList<byte[]>(25);
    	
    	dataList.add(ibBizNo.getBytes());
    	dataList.add(abBizNo.getBytes());
    	dataList.add(msBizNo.getBytes());
    	dataList.add(orgBizNo.getBytes());
    	dataList.add(userAccount.getBytes());
    	dataList.add(userName.getBytes());
    	dataList.add(fromOrgId.getBytes());
    	dataList.add(toOrgId.getBytes());
    	dataList.add(userAppIp.getBytes());
    	dataList.add(reportCity.getBytes());
    	dataList.add(merchantSvcId.getBytes());
    	dataList.add(merchantSvcName.getBytes());
    	dataList.add(merchantId.getBytes());
    	dataList.add(merchantName.getBytes());
    	dataList.add(merchantOrgCode.getBytes());
    	dataList.add(mccCode.getBytes());
    	dataList.add(storeId.getBytes());
    	dataList.add(txTime.getBytes());
    	dataList.add(extStr0.getBytes());
    	dataList.add(extStr1.getBytes());
    	dataList.add(extStr2.getBytes());
    	dataList.add(extStr3.getBytes());
    	dataList.add(extStr4.getBytes());
    	dataList.add(extStr5.getBytes());
    	dataList.add(extStr6.getBytes());
    	
    	return dataList;
    }
}
