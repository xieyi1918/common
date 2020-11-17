import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;


public class HttpClientUtils {

    private HttpClientUtils(){

    }

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARSET = "UTF-8";

    private static final String LOG_CONTENT = "\n url:{}--- param---{}";
    public static String get(String url, Map<String, String> params) {
//        log.info(LOG_CONTENT, url, params);
        String result = null;
        if (params == null || params.isEmpty()) {
            result = request(new HttpGet(url));
        } else {
            try {
                URIBuilder uriBuilder = new URIBuilder(url);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
                result = request(new HttpGet(uriBuilder.build()));
            } catch (URISyntaxException e) {
//                log.error(ExceptionUtil.getExceptionStack(e));
            }
        }
        return result;
    }

    public static String post(String url, Object req, String token) {
//        log.info(LOG_CONTENT, url, req);
        String result;
        if (req == null) {
            result = request(new HttpPost(url));
        } else {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(JsonUtil.obj2Str(req), ContentType.APPLICATION_JSON);
            entity.setContentEncoding(CHARSET);
            post.setEntity(entity);
            result = sumscopeRequest(post,token);
        }
        return result;
    }

    public static String post(String url, Map<String, String> req) {
//        log.info(LOG_CONTENT, url, req);
        String result = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : req.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
            HttpPost post = new HttpPost(uriBuilder.build());
            result = request(post);
        } catch (URISyntaxException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
        return result;
    }

    private static String request(HttpRequestBase request) {
        String result = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            request.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
            request.setHeader(HttpHeaders.ACCEPT_CHARSET, CHARSET);
            request.setHeader(HttpHeaders.ACCEPT_ENCODING, CHARSET);
            request.setHeader(HttpHeaders.CONTENT_ENCODING, CHARSET);
            request.setHeader(HttpHeaders.CONNECTION, "close");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(120000)
                    .setConnectTimeout(120000)
                    .setConnectionRequestTimeout(120000)
                    .build();
            request.setConfig(requestConfig);
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), CHARSET))) {
                StringBuilder sb = new StringBuilder();
                char[] chars = new char[8 * 1024];
                int nRead;
                do {
                    nRead = reader.read(chars, 0, 8 * 1024);
                    if (nRead == -1) {
                        break;
                    }
                    sb.append(chars, 0, nRead);
                } while (true);
                result = sb.toString();
            }
        } catch (IOException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
//            throw new ProgramException("request failed");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
//                    log.error(ExceptionUtil.getExceptionStack(e));
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
//                    log.error(ExceptionUtil.getExceptionStack(e));
                }
            }
        }
        return result;
    }

    private static String sumscopeRequest(HttpRequestBase request, String sumscopeToken) {
        String result = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = HttpClients.createDefault();
            request.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
            request.setHeader(HttpHeaders.ACCEPT_CHARSET, CHARSET);
            request.setHeader(HttpHeaders.ACCEPT_ENCODING, CHARSET);
            request.setHeader(HttpHeaders.CONTENT_ENCODING, CHARSET);
            request.setHeader(HttpHeaders.CONNECTION, "close");
            request.setHeader("sumscope-token",sumscopeToken);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(120000)
                    .setConnectTimeout(120000)
                    .setConnectionRequestTimeout(120000)
                    .build();
            request.setConfig(requestConfig);
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), CHARSET))) {
                StringBuilder sb = new StringBuilder();
                char[] chars = new char[8 * 1024];
                int nRead;
                do {
                    nRead = reader.read(chars, 0, 8 * 1024);
                    if (nRead == -1) {
                        break;
                    }
                    sb.append(chars, 0, nRead);
                } while (true);
                result = sb.toString();
            }
        } catch (IOException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
//            throw new ProgramException("request failed");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
//                    log.error(ExceptionUtil.getExceptionStack(e));
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
//                    log.error(ExceptionUtil.getExceptionStack(e));
                }
            }
        }
        return result;
    }
}
