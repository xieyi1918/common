import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @Author yi.xie
 * 多线程处理任务，按顺序输出结果
 * @Date 2020/12/10
 */
public class MultiWorker<T>{

    private BlockingQueue<FutureTask<T>> blockingQueue = new LinkedBlockingQueue();

    private ExecutorService outExecutor;

    private ExecutorService workerExecutor;

    private Consumer<T> inConsumer;

    private Consumer<T> outConsumer;

    public MultiWorker(ExecutorService outExecutor, ExecutorService workerExecutor, Consumer<T> inConsumer, Consumer<T> outConsumer) {
        this.outExecutor = outExecutor;
        this.workerExecutor = workerExecutor;
        this.inConsumer = inConsumer;
        this.outConsumer = outConsumer;
        if(Objects.isNull(outExecutor)){
            this.outExecutor = ThreadPoolUtil.getSingleThreadPoolExecutor("message-out-");
        }
        if(Objects.isNull(workerExecutor)){
            this.workerExecutor = ThreadPoolUtil.getTaskThreadPoolExecutor();
        }
        checkField();
    }

    private void checkField() {
        if(Objects.isNull(this.outExecutor) || Objects.isNull(this.workerExecutor) || Objects.isNull(this.inConsumer) || Objects.isNull(this.outConsumer)){
            //add error log message
            System.exit(-1);
        }
    }

    public void start() {
        outExecutor.submit(()->{
            while (true){
                T object = blockingQueue.take().get();
                outConsumer.accept(object);
            }
        });
    }

    private void addTask(T object) {
        FutureTask<T> futureTask = new FutureTask(getCallable(object));
        blockingQueue.offer(futureTask);
        workerExecutor.submit(futureTask);
    }

    public Callable<T> getCallable(T object){
        Callable<T> callable = () -> {
            //do something with object
            inConsumer.accept(object);
            return object;
        };
        return callable;
    }
}
