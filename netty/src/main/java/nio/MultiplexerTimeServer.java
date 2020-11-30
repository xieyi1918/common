package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel socketChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.socket().bind(new InetSocketAddress(port),1024);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stop(){
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInupt(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInupt(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //add the new connection to the selector
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("server receive order : "+body);
                    String currentTime = "query time order".equalsIgnoreCase(body) ? new Date().toString() : "bad order";
                    doWrite(sc,currentTime);
                }else if(readBytes<0){
                    //对链路关闭
                    key.cancel();
                    sc.close();
                }else{
                    //读到0字节
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String currentTime) throws IOException {
        if(currentTime!=null && currentTime.trim().length()>0){
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }
}
