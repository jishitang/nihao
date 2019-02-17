package cn.webank.blockchain.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by junqizhang on 08/07/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SetCheckCodeStatusNotifyArgs extends DirectRouteBaseMsgArgs {

    /*
     * 场切是否成功,固定填true
     */
    private boolean isSucc = false;

    /*
     * 境外合作行的机构id，用于区分不同境外合作行的清算
     * 例如为港中银的钱包清算，就填BOCHK
     */
    private String walletOwnerOrg;

    /*
     * 上一场的场切码, 也就是当前需要清算的场次的场切码
     */
    private Integer lastCheckCode;

    /*
     * 这一笔场切码属于哪一天的
     * 如果按1天1场来算，例如20170829表示lastCheckCode这一笔场切属于20170829这一天
     */
    private String lastCheckCodeDate;

    /*
     * 场切的备注信息
     * Optional
     */
    private String checkRemark;

    /*
     * 额外信息，可以为空
     * Optional
     */
    private String extraData;
}
