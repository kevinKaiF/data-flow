package com.github.dataflow.node.model.alarm;

import com.github.dataflow.core.alarm.AlarmService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
public class MailAlarmService extends AbstractAlarmService implements AlarmService {
    private final static Logger  logger        = LoggerFactory.getLogger(MailAlarmService.class);
    private final static Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private              int     stmpPort      = 25;
    private JavaMailSender mailSender;
    private String         emailUsername;
    private String         emailPassword;
    private String         emailHost;
    private String         emailReceiver;

    @Override
    public void doStart() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);
        mailSender.setHost(emailHost);
        mailSender.setDefaultEncoding("UTF-8");
        Properties pros = new Properties();
        pros.put("mail.smtp.auth", "true");
        pros.put("mail.smtp.timeout", "25000");
        pros.put("mail.smtp.port", stmpPort);
        mailSender.setJavaMailProperties(pros);
        this.mailSender = mailSender;
    }

    @Override
    protected void doStop() {

    }

    @Override
    public void doSendAlarm(String subject, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();           // 纯文本
            mail.setText(message);                                      // 邮件内容
            mail.setSubject(subject);                                   // 主题
            mail.setFrom(emailUsername);                                // 发件人

            String receiveKeys[] = StringUtils.split(emailReceiver, ",");
            List<String> address = new ArrayList<>();
            for (String receiveKey : receiveKeys) {
                if (isEmailAddress(receiveKey)) {
                    address.add(receiveKey);
                }
            }

            if (address != null && !address.isEmpty()) {
                for (String add : address) {
                    mail.setTo(add);
                    sendMail(mail);
                }
            }
        } catch (Exception e) {
            logger.error("send mail failure, detail : ", e);
        }
    }

    private boolean isEmailAddress(String email) {
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    private void sendMail(SimpleMailMessage mail) {
        // 正确设置了账户/密码，才尝试发送邮件
        if (StringUtils.isNotEmpty(emailUsername) && StringUtils.isNotEmpty(emailPassword)) {
            mailSender.send(mail);
        }
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public void setStmpPort(int stmpPort) {
        this.stmpPort = stmpPort;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

}
