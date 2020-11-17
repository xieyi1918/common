import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class DisruptorWrapper {

    private static int bufferSize = 1024;

    private EventFactory<ObjectEvent> factory;
    private Disruptor disruptor;
    private EventHandler1 handler;
    private ObjectEventTranslator translator;

    private void initDisruptor1() {
        disruptor = new Disruptor<>(new ObjectEventFactory(), bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());
        //disruptor.handleEventsWith(handler1).handleEventsWith(handler2).handleEventsWith(handler3).then(new ResetObjectEventHandler());
        disruptor.handleEventsWith(new EventHandler1()).then(new ResetHandler());
        disruptor.setDefaultExceptionHandler(new ExceptionHandler());
        translator = new ObjectEventTranslator();
        start();
    }

    private void initDisruptor2() {
        disruptor = new Disruptor<>((EventFactory<ObjectEvent>) () -> null, bufferSize,Executors.defaultThreadFactory(),ProducerType.MULTI, new BlockingWaitStrategy());
        //多个handler
        //disruptor.handleEventsWith(handler1).handleEventsWith(handler2).handleEventsWith(handler3).then(new ResetObjectEventHandler());
        disruptor.handleEventsWith(new EventHandler1()).then(new EventHandler2()).then(new ResetHandler());
        disruptor.setDefaultExceptionHandler(new ExceptionHandler());
        translator = new ObjectEventTranslator();
        start();
    }

    public void publishMessageFromMq(Object handler, Object arg) {
//        mqDisruptor.publishEvent(translator);
//        param not empty follow code
        disruptor.publishEvent(translator, handler, arg);
    }

    public void start() {
        disruptor.start();
    }

    public void stop() {
        disruptor.shutdown();
    }

}
