package bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class TimeServerHandlerPool {

    private ExecutorService executor;

    public TimeServerHandlerPool(int maxPoolSize,int queueSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(Runnable task){
        executor.execute(task);
    }

}
