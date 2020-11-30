package bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Objects;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class TimeServerHandler implements Runnable{

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if(Objects.isNull(body)){
                    break;
                }
                System.out.println("time server receive order : "+body+",current thread : "+Thread.currentThread());
                currentTime = "query time order".equalsIgnoreCase(body) ? new Date().toString() : "";
                out.println(currentTime);
            }
        }catch (Exception e){
            if(in!=null){
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
                out = null;
            }
            if(this.socket!=null){
                try {
                    this.socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}
