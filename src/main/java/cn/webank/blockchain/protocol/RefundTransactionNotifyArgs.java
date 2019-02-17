package cn.webank.blockchain.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tonychen 2018年1月2日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RefundTransactionNotifyArgs extends DirectRouteBaseMsgArgs{

	/**
	 * WeBank订单id
	 */
	private String abBizNo;
	
	/**
	 * 退货订单对应的原WeBank订单id
	 */
	private String abOriginalBizNo;
	
	/**
	 * 时间戳，精确到毫秒
	 */
	private Long timestamp;
}
