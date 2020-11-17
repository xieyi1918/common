import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    public static ThreadPoolExecutor getSingleThreadPoolExecutor(String threadNamePrefix) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(threadNamePrefix + "-");
                    thread.setUncaughtExceptionHandler((t, e) -> {
//                        log.error("EXCEPTION-THREAD : {}", t.getId());
                        e.printStackTrace();
                    });
                    return thread;
                },
                (r, executor) -> {
                    throw new RuntimeException("reject task");
                });
        return threadPoolExecutor;
    }


    public static ThreadPoolExecutor getTaskThreadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setUncaughtExceptionHandler((t, e) -> {
//                        log.error("EXCEPTION-THREAD : {}", t.getId());
                        e.printStackTrace();
                    });
                    return thread;
                },
                (r, executor) -> {
                    throw new RuntimeException("reject task");
                });
        return threadPoolExecutor;
    }
}
