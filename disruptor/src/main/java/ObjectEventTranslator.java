import com.lmax.disruptor.EventTranslatorTwoArg;

public class ObjectEventTranslator implements EventTranslatorTwoArg<ObjectEvent,Object,Object> {
    @Override
    public void translateTo(ObjectEvent event, long sequence, Object arg0, Object arg1) {

    }

    public void translateTo(ObjectEvent event, long sequence) {

    }
}
