import com.lmax.disruptor.EventHandler;

public class EventHandler2 implements EventHandler<ObjectEvent> {

    @Override
    public void onEvent(ObjectEvent event, long l, boolean b) {
        System.out.println("handler2 : seq :"+l+" thread : "+Thread.currentThread());
    }
}
