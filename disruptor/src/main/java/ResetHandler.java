import com.lmax.disruptor.EventHandler;

public class ResetHandler implements EventHandler<ObjectEvent> {

    @Override
    public void onEvent(ObjectEvent event, long l, boolean b) {
        event.clear();
    }
}
