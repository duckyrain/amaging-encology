package cn.amaging.encology.util;

import cn.amaging.encology.util.httpclient.CONTENT_TYPE;
import cn.amaging.encology.util.httpclient.HttpResult;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by DuQiyu on 2018/9/10 11:39.
 */
@Configuration
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    @Value("${httpclient.connectionRequestTimeout:2000}")
    private int connectionRequestTimeout;

    @Value("${httpclient.connectionTimeout:3000}")
    private int connectionTimeout;

    @Value("${httpclient.socketTimeout:5000}")
    private int socketTimeout;

    @Value("${httpclient.maxConnPerRoute:100}")
    private int maxConnPerRoute;

    @Value("${httpclient.maxConnTotal:1000}")
    private int maxConnTotal;

    @Value("${httpclient.connectionTimeToLive:30}")
    private int connectionTimeToLive;

    @Value("${httpclient.evictIdleConnections:30}")
    private int evictIdleConnections;

    private static RequestConfig requestConfig;

    private static CloseableHttpClient closeableHttpClient;

    @PostConstruct
    private void init() {
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            TrustStrategy trustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) {
                    return true;
                }
            };
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStrategy).build();
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        } catch (Exception e) {
            logger.error("ssl config error.", e);
        }
        closeableHttpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setMaxConnPerRoute(maxConnPerRoute)
                .setMaxConnTotal(maxConnTotal)
                .setConnectionTimeToLive(connectionTimeToLive, TimeUnit.SECONDS)
                .evictIdleConnections(evictIdleConnections, TimeUnit.SECONDS).build();
    }

    // HTTP Method
    enum METHOD {
        GET, POST, PUT, PATCH, DELETE
    }

    private static final String FORM_DATA_TYPE = "multipart/form-data";
    private static final String X_WWW_FORM_URLENCODED_TYPE = "application/x-www-form-urlencoded";
    private static final String TEXT_PLAIN_TYPE = "text/plain";
    private static final String APPLICATION_JSON_TYPE = "application/json";
    private static final String APPLICATION_JAVASCRIPT_TYPE = "application/javascript";
    private static final String APPLICATION_XML_TYPE = "application/xml";
    private static final String TEXT_XML_TYPE = "text/xml";
    private static final String TEXT_HTML_TYPE = "text/html";

    public static HttpResult get(String uri) throws Exception {
        return get(uri, null);
    }

    public static HttpResult get(String uri, Map<String, String> headers) throws Exception {
        return get(uri, headers, Consts.UTF_8);
    }

    public static HttpResult post(String uri, Map<String, String> body) throws Exception {
        return post(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> body) throws Exception {
        return post(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> body, Charset charset) throws Exception {
        return post(uri, contentType, null, body, charset);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body) throws Exception {
        return post(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult post(String uri, String body) throws Exception {
        return post(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, String body) throws Exception {
        return post(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, String body, Charset charset) throws Exception {
        return post(uri, contentType, null, body, charset);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String body) throws Exception {
        return post(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult put(String uri, Map<String, String> body) throws Exception {
        return put(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> body) throws Exception {
        return put(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> body, Charset charset) throws Exception {
        return put(uri, contentType, null, body, charset);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body) throws Exception {
        return put(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult put(String uri, String body) throws Exception {
        return put(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, String body) throws Exception {
        return put(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, String body, Charset charset) throws Exception {
        return put(uri, contentType, null, body, charset);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String body) throws Exception {
        return put(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult patch(String uri, Map<String, String> body) throws Exception {
        return patch(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> body) throws Exception {
        return patch(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> body, Charset charset) throws Exception {
        return patch(uri, contentType, null, body, charset);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body) throws Exception {
        return patch(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult patch(String uri, String body) throws Exception {
        return patch(uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, body);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, String body) throws Exception {
        return patch(uri, contentType, body, Consts.UTF_8);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, String body, Charset charset) throws Exception {
        return patch(uri, contentType, null, body, charset);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String body) throws Exception {
        return patch(uri, contentType, headers, body, Consts.UTF_8);
    }

    public static HttpResult delete(String uri) throws Exception {
        return delete(uri, null);
    }

    public static HttpResult delete(String uri, Map<String, String> headers) throws Exception {
        return delete(uri, headers, Consts.UTF_8);
    }

    public static HttpResult get(String uri, Map<String, String> headers, Charset charset) throws Exception {
        return request(METHOD.GET, uri, CONTENT_TYPE.X_WWW_FORM_URLENCODED, headers, null, null, charset);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body, Charset charset) throws Exception {
        return request(METHOD.POST, uri, contentType, headers, body, null, charset);
    }

    public static HttpResult post(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String bodyAsString, Charset charset) throws Exception {
        return request(METHOD.POST, uri, contentType, headers, null, bodyAsString, charset);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body, Charset charset) throws Exception {
        return request(METHOD.PUT, uri, contentType, headers, body, null, charset);
    }

    public static HttpResult put(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String bodyAsString, Charset charset) throws Exception {
        return request(METHOD.PUT, uri, contentType, headers, null, bodyAsString, charset);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> headers, Map<String, String> body, Charset charset) throws Exception {
        return request(METHOD.PATCH, uri, contentType, headers, body, null, charset);
    }

    public static HttpResult patch(String uri, CONTENT_TYPE contentType, Map<String, String> headers, String bodyAsString, Charset charset) throws Exception {
        return request(METHOD.PATCH, uri, contentType, headers, null, bodyAsString, charset);
    }

    public static HttpResult delete(String uri, Map<String, String> headers, Charset charset) throws Exception {
        return request(METHOD.DELETE, uri, CONTENT_TYPE.TEXT_PLAIN, headers, null, null, charset);
    }

    /**
     * @param method
     * @param contentType
     * @param headers
     * @param body
     * @param bodyAsString
     * @param charset
     * @return HttpResult
     */
    private static HttpResult request(METHOD method, String uri, CONTENT_TYPE contentType, Map<String, String> headers,
                                      Map<String, String> body, String bodyAsString, Charset charset) throws Exception {
        assert (null != uri && uri.trim().length() > 0) : "uri cannot be empty.";
        if (null == charset) {
            charset = Consts.UTF_8;
        }
        HttpResult result = null;
        HttpRequestBase request = httpRequest(method, uri, contentType, headers, body, bodyAsString, charset);
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity(), charset);
            logger.debug(logFormat(uri, headers, body, bodyAsString, status, content));
            if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_BAD_REQUEST) {
                logger.error("HTTP exception, status:{}, content:{}", status, content);
            }
            result = new HttpResult(status, content);
        } catch (Exception e) {
            logger.error("", e);
            result = new HttpResult(0, e.getMessage());
        } finally {
            if (null != response) {
                EntityUtils.consume(response.getEntity());
            }
            if (null != request) {
                request.releaseConnection();
            }
        }
        return result;
    }

    private static HttpRequestBase httpRequest(METHOD method, String uri, CONTENT_TYPE contentType, Map<String, String> headers,
                                               Map<String, String> body, String bodyAsString, Charset charset) throws Exception {
        HttpRequestBase httpRequestBase = null;
        if (METHOD.GET == method) {
            httpRequestBase = new HttpGet(uri);
        } else if (METHOD.DELETE == method) {
            httpRequestBase = new HttpDelete(uri);
        } else if (METHOD.POST == method) {
            httpRequestBase = new HttpPost(uri);
            ((HttpPost) httpRequestBase).setEntity(partEntity(body, bodyAsString, contentType, charset));
        } else if (METHOD.PATCH == method) {
            httpRequestBase = new HttpPatch(uri);
            ((HttpPatch) httpRequestBase).setEntity(partEntity(body, bodyAsString, contentType, charset));
        } else if (METHOD.PUT == method) {
            httpRequestBase = new HttpPut(uri);
            ((HttpPut) httpRequestBase).setEntity(partEntity(body, bodyAsString, contentType, charset));
        } else {
            throw new NoSuchMethodException("cannot provide this method.");
        }
        httpRequestBase.setConfig(requestConfig);
        httpRequestBase.setHeader(partContentType(contentType));
        httpRequestBase.setHeaders(partHeaders(headers, charset));
        return httpRequestBase;
    }

    private static Header partContentType(CONTENT_TYPE contentType) {
        return new BasicHeader(HTTP.CONTENT_TYPE, getMimeType(contentType));
    }

    private static Header[] partHeaders(Map<String, String> headers, Charset charset) {
        if (null != headers && headers.size() > 0) {
            Header[] arr = new Header[headers.size()];
            int index = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                arr[index++] = new BasicHeader(entry.getKey(), entry.getValue());
            }
            return arr;
        }
        return null;
    }

    private static HttpEntity partEntity(Map<String, String> body, String bodyAsString, CONTENT_TYPE contentType, Charset charset) throws UnsupportedEncodingException, NoSuchMethodException {
        if (null != body && body.size() > 0) {
            if (CONTENT_TYPE.FORM_DATA == contentType) {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    //noinspection ConstantConditions
                    builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create(getMimeType(contentType), charset)));
                }
                return builder.build();
            } else if (CONTENT_TYPE.X_WWW_FORM_URLENCODED == contentType) {
                List<NameValuePair> params = new LinkedList<>();
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                return new UrlEncodedFormEntity(params, charset);
            } else {
                throw new NoSuchMethodException("Only Content-Type 'form-data' or 'x-www-form-urlencoded' can use body params.");
            }
        }
        if (null != bodyAsString && bodyAsString.trim().length() > 0) {
            return new StringEntity(bodyAsString, ContentType.create(getMimeType(contentType), charset));
        }
        return null;
    }

    private static String getMimeType(CONTENT_TYPE contentType) {
        switch (contentType) {
            case FORM_DATA:
                return FORM_DATA_TYPE;
            case X_WWW_FORM_URLENCODED:
                return X_WWW_FORM_URLENCODED_TYPE;
            case TEXT_PLAIN:
                return TEXT_PLAIN_TYPE;
            case APPLICATION_JSON:
                return APPLICATION_JSON_TYPE;
            case APPLICATION_JAVASCRIPT:
                return APPLICATION_JAVASCRIPT_TYPE;
            case APPLICATION_XML:
                return APPLICATION_XML_TYPE;
            case TEXT_XML:
                return TEXT_XML_TYPE;
            case TEXT_HTML:
                return TEXT_HTML_TYPE;
            default:
                return TEXT_PLAIN_TYPE;
        }
    }

    /**
     * 格式化输出请求参数和响应结果
     * */
    private static String logFormat(String uri, Map<String, String> headers, Map<String, String> body,
                                    String bodyAsString, int status, String content) {
        StringBuilder headerSb = new StringBuilder();
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                //noinspection ConstantConditions
                headerSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            headerSb.deleteCharAt(headerSb.length() - 1);
        }
        if (null == bodyAsString || bodyAsString.trim().length() == 0) {
            if (null != body) {
                StringBuilder bodySb = new StringBuilder();
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    //noinspection ConstantConditions
                    bodySb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                bodySb.deleteCharAt(headerSb.length() - 1);
                bodyAsString = bodySb.toString();
            }
        }
        return String.format("uri:%s, header:%s, body:%s, status:%d, content:%s", uri, headerSb.toString(), bodyAsString, status, content);
    }

}







