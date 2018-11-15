package cn.amaging.encology.util.mail;

import javax.mail.Authenticator;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by DuQiyu on 2018/9/14 13:51.
 */
public class MailInfo {
    // 邮件服务器
    private String host;
    // 发送邮件的服务器的端口
    private int port = 25;
    // 邮件协议
    private String protocol = "smtp";
    // 邮件发送者的地址
    private String from;
    // 邮件接收者
    private String[] to;
    // 抄送接收者
    private String[] toCarbonCopy;
    // 密送接收者
    private String[] toBlindCarbonCopy;
    // 登录用户名
    private String userName;
    // 密码
    private String password;
    // 主题
    private String subject;
    // 文本内容
    private String content;
    // 附件
    private String[] attachFile;

    // 会话属性
    private Properties properties;
    // 身份认证
    private Authenticator authenticator;

    public MailInfo(String host, int port, String protocol, String from, String userName, String password) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.from = from;
        this.userName = userName;
        this.password = password;
        init();
    }

    private void init() {
        properties = new Properties();
        properties.put("mail.smtp.host", this.host);
        properties.put("mail.smtp.port", this.port);
        properties.put("mail.smtp.auth", "true");
        authenticator = new DefaultAuthenticator(this.userName, this.password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getToBlindCarbonCopy() {
        return toBlindCarbonCopy;
    }

    public void setToBlindCarbonCopy(String[] toBlindCarbonCopy) {
        this.toBlindCarbonCopy = toBlindCarbonCopy;
    }

    public String[] getToCarbonCopy() {
        return toCarbonCopy;
    }

    public void setToCarbonCopy(String[] toCarbonCopy) {
        this.toCarbonCopy = toCarbonCopy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String[] attachFile) {
        this.attachFile = attachFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public String toString() {
        return "MailInfo{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", protocol='" + protocol + '\'' +
                ", from='" + from + '\'' +
                ", to=" + Arrays.toString(to) +
                ", toCarbonCopy=" + Arrays.toString(toCarbonCopy) +
                ", toBlindCarbonCopy=" + Arrays.toString(toBlindCarbonCopy) +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", attachFile=" + Arrays.toString(attachFile) +
                '}';
    }
}


