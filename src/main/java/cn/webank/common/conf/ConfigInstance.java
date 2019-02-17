package cn.webank.common.conf;

//import lombok.Data;

/**
 * Created by junqizhang on 09/06/2017.
 */
public enum ConfigInstance {
    INSTANCE;

    //    @Autowired
    private Config conf;

    ConfigInstance() {
        this.conf = new Config();
    }

    public Config getConfig() {
        return this.conf;
    }
}
