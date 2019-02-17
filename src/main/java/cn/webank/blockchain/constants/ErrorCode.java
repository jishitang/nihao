package cn.webank.blockchain.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by junqizhang on 20/06/2017.
 */
public class ErrorCode {

    /*
     * 到区块链读交易结果一直超时，达到次数限制
     * NOTE : 这种情况下，属于交易执行状态未知
     */
    public static final Integer BLOCK_CHAIN_READ_TIMEOUT_REACH_LIMIT = 600004;

    /*
     * 目前没有处理的ResourceAccessException，统一返回这个错误码
     */
    public static final Integer RESOURCE_ACCESS_EXCEPTION_UNKNOWN = 600006;

    public static final Integer GENERATE_CLEARING_TRANS_FILE_ERROR_UNKNOWN = 600007;

    public static final Integer WRITE_FILE_ERROR = 600008;

    /* 清算未完成*/
    public static final Integer CLEARING_NOT_FINISH_ERROR = 600009;

    public static final Integer FILE_ALREADY_EXIST = 600010;

    public static final Integer FILE_PATH_IS_DIRECTORY = 600011;

    public static final Integer DIRECT_ROUTE_MSG_CALLBACK_SERVER_SIDE_NO_HANDLE = 600099;

    /*
     * 链上链下base error code
     */
    public static final Integer DIRECT_ROUTE_MSG_BASE_ERROR = 600100;

    /*
     * 链上链下超时
     */
    public static final Integer DIRECT_ROUTE_REQUEST_TIMEOUT = 600102;

    /*
     * 前置server down掉
     */
    public static final Integer UNI_FRONT_SERVER_NOT_FOUND = 600404;

    public static final Integer UNI_FRONT_SERVER_REQUEST_UNKNOWN = 600405;

    public static final Integer API_SERVICE_NOT_FOUND = 600406;

    /*
     * 前置server存在，但是response超时, 可以通过下面的方式模拟
     * 1. 前置server在回包的时候被jdb debug定住
     * 2. 前置server所在的服务器配置iptables，使其不能回包
     *
     * NOTE : 这种情况下，属于交易执行状态未知
    */
    public static final Integer UNI_FRONT_SERVER_RESPONSE_TIMEOUT = 600504;

    /*
     * 读交易执行结果，有关键字段为null的情况
     * NOTE : 这种情况下，属于交易执行状态未知
     */
    public static final Integer BLOCK_CHAIN_READ_TX_RETURN_NULL = 610001;

    public static final Integer BLOCK_CHAIN_WRITE_TX_RETURN_NULL = 610002;

    public static final Integer BLOCK_CHAIN_WRITE_TX_RETURN_ERROR = 610003;

    public static final Integer BLOCK_CHAIN_WRITE_TX_UNKNOWN = 610004;

//    public static final Integer BLOCK_CHAIN_TX_NOT_CONFIRM_YET = 32012;

    /*
     * ATS 记账成功的错误码
     */
    public static final Set<Integer> ATS_RECORED_PAYABLE_ACCOUNT_SUCCESS = new HashSet<>(
            Arrays.asList(0, 800000)
    );
    /*
     * ATS 记账未知的错误码
     */
    public static final Set<Integer> ATS_RECORED_PAYABLE_ACCOUNT_UNKNOWN = new HashSet<>(
            Arrays.asList(800101, 800102, 800103)
    );
    /*
     * 执行合约返回的错误
     */
    public enum ContractError {

        CHECK_CODE_REQUEST_REPEAT(20122, "setCheckCodeTag request repeated! mabye your request id is repeated or the process is success before!"),

        /*
         * 每次拉去数据的步长不能超过20条
         */
        OFFSET_TOO_LARGE(20222, "current offset is greater than max offset:20."),

        /*
         * 区块没有同步完全
         */
        CHECK_CODE_TOO_LARGE(20220, "this error is triggered when checkCode greater than currentChechCode, probably because of the block height of the node is lower than others!"),

        /*
         * 错误原因是本场次没有任何交易，未来OTS写场切到合约以后，理论上不会再有个错误码
         */
        CHECK_INFO_NOT_EXIST(20201, "check code info not exist, probably due to no any transaction in this check code!"),

        /*
         * 交易还没有被确认，需要继续根据tx hash去区块链确认是否出块上链
         */
        BLOCK_CHAIN_TX_NOT_CONFIRM_YET(32012, "tx have not been confirmed, probably due to blockchain node miner is stop!"),

        /*
         * API Service down掉，由前置返回
         */
        FRONT_SERVER_DETECT_API_SERVICE_DOWN(10005, "API Service is down!"),
        
        /*
         * 正常消费订单已经上链
         */
        TRANSACTION_DOES_EXIST(20202, "transaction exist !"),
        
        /*
         * 错误金额格式
         */
        TRANSACTION_RMBAMT_WRONG(20204, "wrong amount !"),
        
        
        /*
         * 没有查到订单
         */
        ORDER_DOES_NOT_EXIST(20301, "transaction does not exist !"),
        
        /*
         * 撤销的订单已存在（重复上链，属于正常状态码）
         */
        CANCEL_TRANSACTION_ALREADY_EXIST(20302, "transaction is already canceled!"),
        
        /*
         * 订单的状态不对
         */
        TRANSACTION_STATUS_IS_INVALID(20313, "transaction status is invalid!"),
        
        /*
         * 原订单没找到
         */
        ORIGINAL_TRANSACTION_IS_NOT_FOUND(20321, "original transaction is not found!"),
        
        /*
         * 已经撤销的订单
         */
        CANCEL_TRANSACTION_COULD_NOT_BE_REFUNDED(20322, "transaction is canceled, could not be refunded."),
        
        /*
         * 订单类型不对
         */
        REFUND_TRANSACTION_STATUS_ERROR(20323, "status of transaction to be refunded is invalid."),
        ;

        private Integer errorCode;
        private String msg;

        private ContractError(Integer errorCode, String msg) {
            this.errorCode = errorCode;
            this.msg = msg;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public String getMsg() {
            return msg;
        }

        public boolean equals(Integer errorCode) {
            return this.errorCode.equals(errorCode);
        }
    }

}
