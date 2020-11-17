import com.lmax.disruptor.EventHandler;

public class EventHandler1 implements com.lmax.disruptor.EventHandler<ObjectEvent> {

    @Override
    public void onEvent(ObjectEvent event, long l, boolean b) {
        //
    }
}
