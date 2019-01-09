package cn.amaging.encology.common.util;

import cn.amaging.encology.common.util.mail.MailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;

/**
 * Created by DuQiyu on 2018/9/11 13:57.
 */
@Configuration
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.userName}")
    private String userName;

    @Value("${mail.password}")
    private String password;

    private static MailInfo mail;

    @PostConstruct
    private void init() {
        mail = new MailInfo(host, port, protocol, from, userName, password);
    }

    /**
     * @param tos 收件人
     * @param subject 标题
     * @param content 内容
     * @return boolean
     * */
    public static boolean send(String[] tos, String subject, String content) {
        return send(tos, null, null, subject, content, null);
    }

    /**
     * @param tos 收件人
     * @param subject 标题
     * @param content 内容
     * @param attachFiles 附件（绝对路径）
     * @return boolean
     * */
    public static boolean send(String[] tos, String subject, String content, String[] attachFiles) {
        return send(tos, null, null, subject, content, attachFiles);
    }

    /**
     * @param tos 收件人
     * @param CCs 抄送
     * @param subject 标题
     * @param content 内容
     * @return boolean
     * */
    public static boolean send(String[] tos, String[] CCs, String subject, String content) {
        return send(tos, CCs, null, subject, content, null);
    }

    /**
     * @param tos 收件人
     * @param CCs 抄送
     * @param subject 标题
     * @param content 内容
     * @param attachFiles 附件（绝对路径）
     * @return boolean
     * */
    public static boolean send(String[] tos, String[] CCs, String subject, String content, String[] attachFiles) {
        return send(tos, CCs, null, subject, content, attachFiles);
    }

    /**
     * @param tos 收件人
     * @param CCs 抄送
     * @param BCCs 密送
     * @param subject 标题
     * @param content 内容
     * @return boolean
     * */
    public static boolean send(String[] tos, String[] CCs, String[] BCCs, String subject, String content) {
        return send(tos, CCs, BCCs, subject, content);
    }

    /**
     * @param tos 收件人
     * @param CCs 抄送
     * @param BCCs 密送
     * @param subject 主题
     * @param content 内容
     * @param attachFiles 附件
     * @return boolean
     * */
    public static boolean send(String[] tos, String[] CCs, String[] BCCs, String subject, String content, String[] attachFiles) {
        boolean result = false;
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getDefaultInstance(mail.getProperties(), mail.getAuthenticator());
        try {
            // 根据session创建一个邮件消息
            Message message = new MimeMessage(session);
            // 消息发送者
            message.setFrom(new InternetAddress(mail.getFrom()));
            // 收件地址
            message.setRecipients(Message.RecipientType.TO, internetAddresses(tos));
            // 抄送地址
            message.addRecipients(Message.RecipientType.CC, internetAddresses(CCs));
            // 密送地址
            message.addRecipients(Message.RecipientType.BCC, internetAddresses(BCCs));
            // 邮件主题
            message.setSubject(MimeUtility.encodeText(subject,"utf-8","B"));
            // 邮件内容和附件
            Multipart multipart = new MimeMultipart();
            // 文本内容设置
            if(null != content && content.trim().length() > 0) {
                BodyPart part = new MimeBodyPart();
                part.setContent(content, "text/html;charset=utf-8");
                multipart.addBodyPart(part);
            }
            // 附件内容设置
            if (null != attachFiles && attachFiles.length > 0) {
                for (String attachFile : attachFiles) {
                    BodyPart part = new MimeBodyPart();
                    // 根据文件名获取数据源
                    DataSource dataSource = new FileDataSource(attachFile);
                    DataHandler dataHandler = new DataHandler(dataSource);
                    part.setDataHandler(dataHandler);
                    part.setFileName(MimeUtility.encodeText(dataSource.getName()));
                    multipart.addBodyPart(part);
                }
            }
            message.setContent(multipart);
            // 发送时间
            message.setSentDate(new Date());
            // 发送邮件
            Transport transport = session.getTransport(mail.getProtocol());
            transport.connect(mail.getUserName(), mail.getPassword());
            transport.send(message, message.getAllRecipients());
            transport.close();
            logger.info("send mail to:{}, cc:{}, bcc:{}, subject:{}", tos, CCs, BCCs, subject);
            result = true;
        } catch (Exception e) {
            logger.error("send mail error.", e);
        }
        return result;
    }

    private static InternetAddress[] internetAddresses(String[] addresses) throws AddressException {
        if (null == addresses || addresses.length == 0) {
            return null;
        }
        InternetAddress[] iAddr = new InternetAddress[addresses.length];
        for (int i = 0; i < addresses.length; ++i) {
            iAddr[i] = new InternetAddress(addresses[i]);
        }
        return iAddr;
    }

}
