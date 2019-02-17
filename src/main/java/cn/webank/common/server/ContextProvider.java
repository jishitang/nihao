package cn.webank.common.server;

/**
 * Created by junqizhang on 08/06/2017.
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 获取应用的 ApplicationContext 相关服务
 * <p>
 * 同时支持业务正常启动和测试框架
 * 【注意】需要非懒加载
 *
 * @author toutzhang
 * @since 20160205
 */
@Lazy(false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("cn.webank.cfpd.commons.server.ContextProvider")
public class ContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 获取系统 ApplicationContext 对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (null == context) {
            context = arg0;
        }
    }

    /**
     * 获取 Bean 实例
     *
     * @param clazz Bean 类型
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 获取 Bean 实例
     *
     * @param name  Bean 名称
     * @param clazz Bean 类型
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    /**
     * 获取框架实例化的业务线程池
     * <p>
     * 可用于线程池监控
     *
     * @return 业务线程池
     */
    public static Map<String, ThreadPoolTaskExecutor> getThreadPoolMap() {
        Map<String, ThreadPoolTaskExecutor> executors = context.getBeansOfType(ThreadPoolTaskExecutor.class, false, false);
        // 这个判断是多余的，实际测试不会返回 null
        if (null == executors) {
            executors = Collections.emptyMap();
        }

        return executors;
    }

    /**
     * 判断当前定时任务线程对应的线程池是否 shutdown 了
     *
     * @return true 已经关闭；false 未关闭
     */
    public static boolean isSchedulerShutdown() {
        String threadName = Thread.currentThread().getName();
        Map<String, ThreadPoolTaskScheduler> executors = context.getBeansOfType(ThreadPoolTaskScheduler.class, false, false);
        for (Map.Entry<String, ThreadPoolTaskScheduler> entry : executors.entrySet()) {
            ThreadPoolTaskScheduler scheduler = entry.getValue();
            // 只有当线程对应的线程池还能从 context 获取并且没有 shutdown 才返回 false
            if (threadName.startsWith(scheduler.getThreadNamePrefix()) && !scheduler.getScheduledExecutor().isShutdown()) {
                return false;
            }
        }

        return true;
    }
}
