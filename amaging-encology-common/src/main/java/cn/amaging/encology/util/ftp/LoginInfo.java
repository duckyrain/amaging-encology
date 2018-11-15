package cn.amaging.encology.util.ftp;

/**
 * Created by DuQiyu on 2018/10/24 17:28.
 */
public class LoginInfo {

    private String hostName;

    private int port = 21;

    private String userName;

    private String password;

    private String encoding;

    public LoginInfo(String hostName, int port, String userName, String password) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public LoginInfo(String hostName, int port, String userName, String password, String encoding) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.encoding = encoding;
    }

    public LoginInfo() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
