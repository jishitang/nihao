package cn.webank.blockchain.protocol;

import lombok.Data;

/**
 * @author tonychen 2017年12月29日
 *
 */
@Data
public class UpdateCheckCodeStatusResult {

	/**
	 * update成功与否的状态标识
	 */
	private Integer status;
	
	/**
	 * 场切码
	 */
	private Integer checkCode;
	
}
