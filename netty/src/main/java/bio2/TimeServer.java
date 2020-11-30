package bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080 ;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("start port : "+port);
            Socket socket = null;
            TimeServerHandlerPool timeServerHandlerPool = new TimeServerHandlerPool(10,100);
            while (true){
                socket = serverSocket.accept();
                timeServerHandlerPool.execute(new TimeServerHandler(socket));
            }
        }catch (Exception e){

        }finally {
            if(serverSocket!=null){
                System.out.println("close server");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
