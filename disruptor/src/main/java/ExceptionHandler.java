public class ExceptionHandler implements com.lmax.disruptor.ExceptionHandler<Object> {
    public void handleEventException(Throwable ex, long sequence, Object event) {
//        log.error("Exception processing  sequence : {} ,event : {}" , sequence , event);
        ex.printStackTrace();
    }

    public void handleOnStartException(Throwable ex) {
//        log.error("Exception during onStart() : {} ",ex.getMessage());
    }

    public void handleOnShutdownException(Throwable ex) {
//        log.error("Exception during onShutdown() : {} ",ex.getMessage());
    }
}
