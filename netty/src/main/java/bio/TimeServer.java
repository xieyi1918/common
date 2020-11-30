package bio;

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
            while (true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
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
