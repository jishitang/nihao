package cn.webank.blockchain.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by junqizhang on 08/07/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckDirectRouteMsgHealthArgs extends DirectRouteBaseMsgArgs {

    /*
     * 任意包体
     */
    private String message;
}
