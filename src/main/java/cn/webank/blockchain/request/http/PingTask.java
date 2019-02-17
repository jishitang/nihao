package cn.webank.blockchain.request.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tabsuyu on 2016/11/23.
 */
public class PingTask {

    private static final int MAX_PING_FAILED = 10;
    private static Logger logger = LoggerFactory.getLogger(PingTask.class);
    private Timer timer;
    private TimeTask timeTask;
    private long delayMilliseconds;
    //    private Config config;
    private ExecutorService poolExecutor = Executors.newCachedThreadPool();
//    private ConcurrentHashMap<Chain, Integer> pingFailMap = new ConcurrentHashMap<Chain, Integer>();

    public PingTask(long delayMilliseconds) {
        this.timer = new Timer();
        this.delayMilliseconds = delayMilliseconds;
//        this.config = config;
        this.timeTask = new TimeTask();
    }

    public void schedule() {
        this.timer.schedule(this.timeTask, this.delayMilliseconds, this.delayMilliseconds);
    }

    public class TimeTask extends TimerTask {

        @Override
        public void run() {
            UniFrontServicePoolInstance.INSTANCE.getServicePool().getAllUniFrontService();

            for (RetrofitService<UniFrontService> service : UniFrontServicePoolInstance.INSTANCE.getServicePool().getAllUniFrontService()) {
                Runnable runnable = new Ping(service);
                poolExecutor.execute(runnable);
            }
        }
    }

    public class Ping implements Runnable {

        //        private Chain chain;
        private RetrofitService<UniFrontService> service;

        public Ping(RetrofitService<UniFrontService> service) {
            this.service = service;
        }

        @Override
        public void run() {
            try {
//                logger.info("ping ip:" + this.service.getApiBaseUrl());
                Response<String> response = this.service.getService().ping(null).execute();
//                logger.info("pong from ip {}, : {}",this.service.getApiBaseUrl(), response.body());
                this.service.setHealth(true);


            } catch (Exception e) {
                this.service.setHealth(false);
            }
        }
    }
}
