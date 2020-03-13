package com.neuifo.mangareptile.domain.util;


import com.neuifo.data.domain.utils.LogHelper;
import com.neuifo.domain.executor.ThreadExecutor;
import com.neuifo.mangareptile.AppLayerErrorCatcher;
import com.neuifo.mangareptile.BuildConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Decorated {@link ThreadPoolExecutor}
 */
//@Singleton
public class JobExecutor implements ThreadExecutor {

    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 9;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue;

    private ThreadPoolExecutor threadPoolExecutor;

    private final ThreadFactory threadFactory;


    public void shutdown() {
        threadPoolExecutor.shutdownNow();
    }

    private void reset() {
        threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30, KEEP_ALIVE_TIME_UNIT,
                this.workQueue, this.threadFactory);
    }

    //@Inject
    public JobExecutor() {

        this.workQueue = new SynchronousQueue<Runnable>();//new LinkedBlockingQueue<>();
        this.threadFactory = new JobThreadFactory();
        /*this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, this.workQueue, this.threadFactory);*/

        this.threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30, KEEP_ALIVE_TIME_UNIT,
                this.workQueue, this.threadFactory
                /*new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false)*/);
    }

    /*Caused by: java.util.concurrent.RejectedExecutionException: Task rx.schedulers.ExecutorScheduler$ExecutorSchedulerWorker@101f542 rejected from java.util.concurrent.ThreadPoolExecutor@99bb953[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 29]
    at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2014)
    at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:794)
    at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1340)
    at com.higgs.app..dianjia.data.executor.JobExecutor.execute(JobExecutor.java:52)
    at rx.schedulers.ExecutorScheduler$ExecutorSchedulerWorker.schedule(ExecutorScheduler.java:78)

    java.util.concurrent.RejectedExecutionException: Task rx.internal.schedulers.ExecutorScheduler$ExecutorSchedulerWorker@d4217e0 rejected from java.util.concurrent.ThreadPoolExecutor@ad7ce5e[Running, pool size = 9, active threads = 9, queued tasks = 0, completed tasks = 19]
03-14 10:33:43.900 2100-2100/com.higgs.app..platform.pricing.test W/System.err:     at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2014)
03-14 10:33:43.900 2100-2100/com.higgs.app..platform.pricing.test W/System.err:     at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:794)
03-14 10:33:43.900 2100-2100/com.higgs.app..platform.pricing.test W/System.err:     at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1340)
03-14 10:33:43.900 2100-2100/com.higgs.app..platform.pricing.test W/System.err:     at com.higgs.app..platform.pricing.core.JobExecutor.execute(JobExecutor.java:82)
    */
    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }

        if (threadPoolExecutor.isShutdown()) {
            reset();
        }

        if (!threadPoolExecutor.isShutdown()) {
            this.threadPoolExecutor.execute(runnable);
            System.out.println("JobThreadFactory getTaskCount: " + threadPoolExecutor.getTaskCount() + " getCompletedTaskCount: " + threadPoolExecutor.getCompletedTaskCount() + " getPoolSize: " + threadPoolExecutor.getPoolSize());
        } else {
            System.err.println("JobThreadFactory(isShutdown) getTaskCount: " + threadPoolExecutor.getTaskCount() + " getCompletedTaskCount: " + threadPoolExecutor.getCompletedTaskCount() + " getPoolSize: " + threadPoolExecutor.getPoolSize());
        }

    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "android_";
        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, THREAD_NAME + counter++) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable e) {
                        LogHelper.Companion.getSystem().e("JobThreadFactory run error:" + e.getLocalizedMessage());
                        AppLayerErrorCatcher.INSTANCE.throwException(e);
                        if (BuildConfig.DEBUG) {
                            throw e;
                        }
                    }
                }
            };

            thread.setDaemon(true);
            System.out.println("JobThreadFactory newThread: " + thread.getName());
            return thread;
        }
    }
}
