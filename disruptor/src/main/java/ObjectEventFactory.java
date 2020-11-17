import com.lmax.disruptor.EventFactory;

public class ObjectEventFactory implements EventFactory<ObjectEvent> {
    @Override
    public ObjectEvent newInstance() {
        return new ObjectEvent();
    }
}
