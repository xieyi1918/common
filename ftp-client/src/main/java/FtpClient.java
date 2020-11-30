import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @Author yi.xie
 * @Date 2020/11/26
 */
public class FtpClient {
    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private int ftpPort;

    @Value("${ftp.username}")
    private String ftpUsername;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.passiveMode}")
    private boolean passiveMode;

    @Value("${ftp.local.path}")
    protected String localPath;

//    @PostConstruct
//    public void afterConstruct() {
//        logger.info("FTP模式:{}", passiveMode ? "被动" : "主动");
//    }

    /**
     * 获取连接
     */
    public FTPClient getFtpClient() {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            ftpClient.setConnectTimeout(5000);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (passiveMode) {
                ftpClient.enterLocalPassiveMode();
            }
        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
        }
        return ftpClient;
    }


    /**
     * 下载文件
     */
    public File download(String fileName) {
        File localFile = null;
        try {
            FTPClient ftpClient = getFtpClient();
            if(fileName.contains(File.separator)){
                String [] infos;
                if(File.separator.equals("\\")){
                    infos = fileName.split('\\' + File.separator);
                }else{
                    infos = fileName.split(File.separator);
                }
                StringBuilder dir = new StringBuilder();
                for(int i=0;i<=infos.length-1-1;i++){
                    if(infos[i].length()>0) {
                        dir.append(File.separator).append(infos[i]);
                    }
                }
                ftpClient.changeWorkingDirectory(dir.toString());
                fileName = infos[infos.length-1];
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    File downFile = new File(localPath + File.separator + file.getName());
                    OutputStream out = new FileOutputStream(downFile);
                    ftpClient.retrieveFile(file.getName(), out);
                    out.flush();
                    out.close();
                    localFile = downFile;
                    break;
                }
            }
            closeFTP(ftpClient);
        } catch (Exception e) {
//            logger.error("download failed");
        }
        return localFile;
    }

    public boolean closeFTP(FTPClient ftp){
        try {
            ftp.logout();
        } catch (Exception e) {
//            logger.error("ftpClient closed failed");
        }finally{
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
//                    logger.error("ftpClient closed failed");
                }
            }
        }
        return false;
    }

    public static List<String> readTxt(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("xml not exist!!!");
        }
        List<String> list = new ArrayList<>();
        try {
            FileInputStream in = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(in, "GBK"));
            String temp;
            while ((temp = read.readLine()) != null) {
                list.add(temp);
            }
            read.close();
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("read txt failed!!!");
        }
        return list;
    }


    public static List<String> readTxt(File file) {
        List<String> list = new ArrayList<>();
        try {
            FileInputStream in = new FileInputStream(file);
            BufferedReader read = new BufferedReader(new InputStreamReader(in, "GBK"));
            String temp;
            while ((temp = read.readLine()) != null) {
                list.add(temp);
            }
            read.close();
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("read txt failed!!!");
        }
        return list;
    }

    public static List<Map<String,Object>> parseXml2Map(String xmlPath) throws DocumentException {
        File file = new File(xmlPath);
        if (!file.exists()) {
            throw new RuntimeException("xml not exist!!!");
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Map<String,Object>> dataList = new ArrayList<>(root.elements().size());
        for (Object rootItem : root.elements()) {
            Map<String, Object> map = new LinkedHashMap<>();
            Element dataEle = (Element) rootItem;
            set(dataEle, map);
            dataList.add(map);
        }
        return dataList;
    }

    public static List<Map<String,Object>> parseXml2Map(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        List<Map<String,Object>> dataList = new ArrayList<>(root.elements().size());
        for (Object rootItem : root.elements()) {
            Map<String, Object> map = new LinkedHashMap<>();
            Element dataEle = (Element) rootItem;
            set(dataEle, map);
            dataList.add(map);
        }
        return dataList;
    }

    public static void set(Element dataEle, Map<String, Object> map) {
        for (Object item : dataEle.elements()) {
            Element element = (Element) item;
            String key = element.getName();
            Object value = element.getData();
            char[] chars = key.toCharArray();
            boolean allUper = true;
            for(char k : chars){
                if(Character.isLowerCase(k)){
                    allUper = false;
                }
            }
            if(allUper){
                key = key.toLowerCase();
            }else {
                key = convertFirstToLow(key);
            }

            if (element.elements().size() == 0) {
                if(map.containsKey(key)){
                    List<Object> list = new ArrayList<>();
                    list.add(map.get(key));
                    list.add(value);
                    map.put(key,list);
                }else {
                    map.put(key, value.toString().isEmpty() ? null : value);
                }
            } else {
                Map<String, Object> itemMap = new LinkedHashMap<>();
                if (map.containsKey(key)) {
                    ((List) map.get(key)).add(itemMap);
                } else {
                    List<Map> list = new ArrayList<>();
                    list.add(itemMap);
                    map.put(key, list);
                }
                set(element, itemMap);
            }
        }
    }

    public static String convertFirstToLow(String str){
        return  new StringBuilder().append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static void cleanDirectory(String localPath) {
        File file = new File(localPath);
        if(file.isDirectory()){
            for(File innerFile : file.listFiles()){
                innerFile.delete();
            }
        }
    }
}
