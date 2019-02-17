package cn.webank.blockchain.spi.common.dto;

import lombok.Data;

@Data
public class PostData {

    /**
     * 每个链都有不同的key_id
     */
    private String chain_id;

    /**
     * 每个链都有不同的key_id
     */
    private String key_id;

    /**
     * 请求来源标示，谁发起请求上链，就填谁的标示。需事先分配好
     * 即orgId
     */
    private String org_app;

    /**
     * 协议版本号，默认1.0.0
     */
    private String version;

    /**
     * 机构标识
     */
    private String bankNo;

    /**
     * 应用类型
     */
    private String appName;

    /**
     * 交易请求的唯一id，由发起方保证唯一性
     */
    private String tx_id;

    /**
     * 是否需要对请求内容进行加密
     */
    private String encrypt_type;

    private ReqData req_data;

    @Data
    public static class ReqData {
        /**
         * 调用区块链节点的API名称，默认callContract
         */
        private String method;
        /**
         * callContract的参数
         */
        private Param param;

        @Data
        public static class Param {
            /**
             * "调用合约的函数名称，默认setCheckCodeTag"
             */
            private String func;
            /**
             * 清算行逻辑合约地址(事先配置)
             */
            private String contract_addr;
            /**
             * 调用方账号，需事先分配
             */
            private String address;
            /**
             * 调用方账号密码，需事先分配
             */
            private String pwd;
            /**
             * 调用合约函数的参数列表(LIST)
             */
            private Object args;
        }
    }

}





