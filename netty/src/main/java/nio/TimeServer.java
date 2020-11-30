package nio;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
