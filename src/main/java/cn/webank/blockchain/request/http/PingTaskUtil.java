package cn.webank.blockchain.request.http;

/**
 * Created by junqizhang on 2017/5/8.
 */
public enum PingTaskUtil {

    INSTANCE;

    private PingTask pingTask;

    public PingTask getPingTask() {
        return pingTask;
    }

    public void setPingTask(PingTask pingTaskParam) {
        pingTask = pingTaskParam;
    }
}
