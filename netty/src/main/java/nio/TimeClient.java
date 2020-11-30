package nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Author yi.xie
 * @Date 2020/11/30
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new TimeClientHandle("127.0.0.1",port),"timeclient-001").start();
    }

}
