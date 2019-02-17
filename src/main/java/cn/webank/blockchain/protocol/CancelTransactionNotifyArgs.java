package cn.webank.blockchain.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tonychen 2018年1月2日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CancelTransactionNotifyArgs extends DirectRouteBaseMsgArgs{

	/**
	 * WeBank订单id
	 */
	private String abBizNo;
	
	/**
	 * 时间戳，精确到毫秒
	 */
	private Long timestamp;
}
