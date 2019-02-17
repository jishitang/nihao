package cn.webank.common.conf;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

//import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by junqizhang on 08/06/2017.
 */

//private enum ConfigInstance {
//    INSTANCE;
//
//    private Config conf;
//
//    ConfigInstance() {
//        this.conf = new Config();
//    }
//
////    public void setConfig(Config conf) {
////        this.conf = conf;
////    }
//    public Config getConfig() {
//        return this.conf;
//    }
//}

@Data
//@Component
//@Configuration
//@PropertySource("file:application.properties")
public class Config {

    //    @Value("${wepop.uniFrontSvrURL}")
    protected List<String> uniFrontSvrUrl;

    //    @Value("${bank.blockchain.keyid}")
    private String keyId; // 每个链都有不同的key_id

    //    @Value("${bank.blockchain.orgapp}")
    private String orgId; // 请求来源标示，谁发起请求上链，就填谁的标示。需事先分配好

    //    @Value("${bank.blockchain.bankNo}")
//    private String bankNo; // 机构标识

    //    @Value("${bank.blockchain.appName}")
    private String appName;// 应用类型

    //    @Value("${bank.blockchain.version}")
    private String version;

    //    @Value("${bank.blockchain.encryptType}")
    private String encryptType;

    //    @Value("${bank.blockchain.issuingBank.contractaddress}")
    private String issuingBankContractAddress; // 发卡行逻辑合约地址

    //    @Value("${bank.blockchain.clearingBank.contractaddress}")
    private String clearingBankContractAddress; // 清算行逻辑合约地址

    //    @Value("${bank.blockchain.acquirerBank.contractaddress}")
    private String acquirerBankContractAddress; // 收单逻辑合约地址

    //    @Value("${bank.blockchain.externalWethAccount}")
    private String externalWethAccount; // 调用方账号，需事先分配

    //    @Value("${bank.blockchain.pwd}")
    private String pwd; // 调用方账号密码，需事先分配

    private String issuingOrgId;

    private String acquirerOrgId;

    private String clearingOrgId;

    private Integer pingTaskInterval;

    private Integer stressTestAllInterfaceInterval;

    private Integer writeBlockchainRepeatNum;

    private Integer queryBlockchainTxSuccInterval;

    /*
     * 普通链上链下消息的超时时间
     */
    private Integer defaultDirectRouteRequestTimeout;
    /*
     * 链上链下且需要写交易的超时时间
     */
    private Integer directRouteTransactionTimeout;
    /*
     * 读区块链的超时时间
     */
    private Integer queryBlockChainTimeout;
    /*
     * 写区块链的超时时间
     */
    private Integer writeBlockChainTimeout;

    public Config() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ConfigProvider service = context.getBean(ConfigProvider.class);
    }

    private static Config getInstance() {
        return ConfigInstance.INSTANCE.getConfig();
    }

    public static List<String> getUniFrontSvrUrl() {
        if (null == getInstance().uniFrontSvrUrl) {

            String uniFrontSvrUrls = ConfigUtil.getConf("wepop.uniFrontSvrURL");
            String[] parts = uniFrontSvrUrls.split(",");
            getInstance().uniFrontSvrUrl = new ArrayList<>();
            for (String url : parts) {
                getInstance().uniFrontSvrUrl.add(url);
            }
        }
//        UniFrontServicePoolInstance.INSTANCE.init(getInstance().uniFrontSvrUrl);
        return getInstance().uniFrontSvrUrl;
    }

    public static String getKeyId() {
        if (null == getInstance().keyId) {
            getInstance().keyId = ConfigUtil.getConf("bank.blockchain.keyid");
        }
        return getInstance().keyId;
    }

    public static String getOrgId() {
        if (null == getInstance().orgId) {
            getInstance().orgId = ConfigUtil.getConf("bank.blockchain.orgId");
        }
        return getInstance().orgId;
    }

    public static String getIssuingBankContractAddress() {
        if (null == getInstance().issuingBankContractAddress) {
            getInstance().issuingBankContractAddress = ConfigUtil.getConf("bank.blockchain.issuingBank.contractaddress");
        }
        return getInstance().issuingBankContractAddress;
    }

    public static String getClearingBankContractAddress() {
        if (null == getInstance().clearingBankContractAddress) {
            getInstance().clearingBankContractAddress = ConfigUtil.getConf("bank.blockchain.clearingBank.contractaddress");
        }
        return getInstance().clearingBankContractAddress;
    }

    public static String getAcquirerBankContractAddress() {
        if (null == getInstance().acquirerBankContractAddress) {
            getInstance().acquirerBankContractAddress = ConfigUtil.getConf("bank.blockchain.acquirerBank.contractaddress");
        }
        return getInstance().acquirerBankContractAddress;
    }

    public static String getExternalWethAccount() {
        if (null == getInstance().externalWethAccount) {
            getInstance().externalWethAccount = ConfigUtil.getConf("bank.blockchain.externalWethAccount");
        }
        return getInstance().externalWethAccount;
    }

    public static String getPwd() {
        if (null == getInstance().pwd) {
            getInstance().pwd = ConfigUtil.getConf("bank.blockchain.pwd");
        }
        return getInstance().pwd;
    }

//    public static String getBankNo() {
//        if (null == getInstance().bankNo) {
//            getInstance().bankNo = ConfigUtil.getConf("bank.blockchain.bankNo");
//        }
//        return getInstance().bankNo;
//    }

    public static String getAppName() {
        if (null == getInstance().appName) {
            getInstance().appName = ConfigUtil.getConf("bank.blockchain.appName");
        }
        return getInstance().appName;
    }

    public static String getVersion() {
        if (null == getInstance().version) {
            getInstance().version = ConfigUtil.getConf("bank.blockchain.version");
        }
        return getInstance().version;
    }

    public static String getEncryptType() {
        if (null == getInstance().encryptType) {
            getInstance().encryptType = ConfigUtil.getConf("bank.blockchain.encryptType");
        }
        return getInstance().encryptType;
    }

    public static String getIssuingOrgId() {
        if (null == getInstance().issuingOrgId) {
            getInstance().issuingOrgId = ConfigUtil.getConf("wepop.issuingOrgId");
        }
        return getInstance().issuingOrgId;
    }

    public static String getAcquirerOrgId() {
        if (null == getInstance().acquirerOrgId) {
            getInstance().acquirerOrgId = ConfigUtil.getConf("wepop.acquirerOrgId");
        }
        return getInstance().acquirerOrgId;
    }

    public static String getClearingOrgId() {
        if (null == getInstance().clearingOrgId) {
            getInstance().clearingOrgId = ConfigUtil.getConf("wepop.clearingOrgId");
        }
        return getInstance().clearingOrgId;
    }

    public static int getPingTaskInterval() {
        if (null == getInstance().pingTaskInterval) {
            getInstance().pingTaskInterval = Integer.valueOf(ConfigUtil.getConf("pingtask.interval"));
        }
        return getInstance().pingTaskInterval;
    }

    public static int getStressTestAllInterfaceInterval() {
        if (null == getInstance().stressTestAllInterfaceInterval) {
            getInstance().stressTestAllInterfaceInterval = Integer.valueOf(ConfigUtil.getConf("stressTest.callAllInterfaceInterval"));
        }
        return getInstance().stressTestAllInterfaceInterval;
    }

    public static int getWriteBlockchainRepeatNum() {
        if (null == getInstance().writeBlockchainRepeatNum) {
            getInstance().writeBlockchainRepeatNum = Integer.valueOf(ConfigUtil.getConf("writeBlockchainRepeatNum"));
        }
        return getInstance().writeBlockchainRepeatNum;
    }

    public static int getQueryBlockchainTxSuccInterval() {
        if (null == getInstance().queryBlockchainTxSuccInterval) {
            getInstance().queryBlockchainTxSuccInterval = Integer.valueOf(ConfigUtil.getConf("queryBlockchainTxSuccInterval"));
        }
        return getInstance().queryBlockchainTxSuccInterval;
    }

    public static int getDefaultDirectRouteRequestTimeout() {
        if (null == getInstance().defaultDirectRouteRequestTimeout) {
            getInstance().defaultDirectRouteRequestTimeout = Integer.valueOf(ConfigUtil.getConf("defaultDirectRouteRequestTimeout"));
        }
        return getInstance().defaultDirectRouteRequestTimeout;
    }

    public static int getDirectRouteTransactionTimeout() {
        if (null == getInstance().directRouteTransactionTimeout) {
            getInstance().directRouteTransactionTimeout = Integer.valueOf(ConfigUtil.getConf("directRouteTransactionTimeout"));
        }
        return getInstance().directRouteTransactionTimeout;
    }

    public static int getQueryBlockChainTimeout() {
        if (null == getInstance().queryBlockChainTimeout) {
            getInstance().queryBlockChainTimeout = Integer.valueOf(ConfigUtil.getConf("queryBlockChainTimeout"));
        }
        return getInstance().queryBlockChainTimeout;
    }

    public static int getWriteBlockChainTimeout() {
        if (null == getInstance().writeBlockChainTimeout) {
            getInstance().writeBlockChainTimeout = Integer.valueOf(ConfigUtil.getConf("writeBlockChainTimeout"));
        }
        return getInstance().writeBlockChainTimeout;
    }
}


