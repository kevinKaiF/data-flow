package com.github.dataflow.node.model.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/26
 */
public class GlobalExecutor implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(GlobalExecutor.class);

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private ExecutorService executorService;

    private int corePoolSize;

    private int maxPoolSize;

    private int keepLiveTime;

    private BlockingQueue<Runnable> blockingQueue;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (blockingQueue == null) {
            blockingQueue = new ArrayBlockingQueue<>(16);
        }
        executorService = new ThreadPoolExecutor(corePoolSize,
                                                 maxPoolSize,
                                                 keepLiveTime,
                                                 TimeUnit.SECONDS,
                                                 blockingQueue,
                                                 new NamedThreadFactory(),
                                                 new LogRejectedExecutionHandler());
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    private class NamedThreadFactory implements ThreadFactory{
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "dataflow-global-" + atomicInteger.getAndIncrement());
            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
            return thread;
        }
    }

    private class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("uncaught exception", e);
        }
    }

    private class LogRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.warn("Task " + r.toString() + " rejected from " + executor.toString());
        }
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepLiveTime(int keepLiveTime) {
        this.keepLiveTime = keepLiveTime;
    }

    public void setBlockingQueue(BlockingQueue<Runnable> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }
}
