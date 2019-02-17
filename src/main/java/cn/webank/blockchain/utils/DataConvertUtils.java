/**
 * @author tonychen
 *
 */
package cn.webank.blockchain.utils;

import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;



/**
 * 将字符串转为web3j类型bytes32，长度一定得32，不然会抛异常
 * @author tonychen
 *
 */
public class DataConvertUtils {

	public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

}
