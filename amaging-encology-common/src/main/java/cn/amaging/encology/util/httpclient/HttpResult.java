package cn.amaging.encology.util.httpclient;

/**
 * Created by DuQiyu on 2018/9/10 11:42.
 */
public class HttpResult {

    /**
     * http status code（200, 401, 502, etc.）
     * */
    private int status;

    /**
     * Response body
     * */
    private String content;

    public HttpResult(int status, String content) {
        this.status = status;
        this.content = content;
    }

    public HttpResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "status=" + status +
                ", content='" + content + '\'' +
                '}';
    }


}
