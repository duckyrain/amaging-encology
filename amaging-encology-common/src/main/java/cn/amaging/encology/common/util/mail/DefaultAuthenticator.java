package cn.amaging.encology.common.util.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by DuQiyu on 2018/9/11 14:01.
 */
public class DefaultAuthenticator extends Authenticator {

    private String userName;

    private String password;

    public DefaultAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public DefaultAuthenticator() {
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
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

    @Override
    public String toString() {
        return "DefaultAuthenticator{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
