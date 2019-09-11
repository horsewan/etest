package com.eningqu.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 *
 * @desc TODO  邮件发送工具类
 * @author     Yanghuangping
 * @since      2018/8/23 17:13
 * @version    1.0
 *
 **/
@Component
public class EmailKit {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    TemplateEngine templateEngine;


    /**
     * TODO 发送简单的文本邮件
     * @param from          发送者
     * @param to            接受者
     * @param carbonCopy    抄送者
     * @param subject       主题
     * @param content       内容
     */
    @Async("")
    public void sendSimpleEmail(String from, String[] to, String[] carbonCopy, String subject, String content){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setCc(carbonCopy);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            logger.info("Send mail success");
        } catch (MailException e) {
            logger.error("Send mail failed, error message is {}\n", e.getMessage());
        }
    }

    /**
     * TODO 发送HTML邮件
     */
    @Async("")
    public void sendHTMLEmail(String deliver, String[] receiver, String[] carbonCopy, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(deliver);
            messageHelper.setTo(receiver);
            messageHelper.setCc(carbonCopy);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            mailSender.send(message);
            logger.info("Send email success");
        } catch (MessagingException e) {
            logger.error("Send mail failed, error message is {}\n", e.getMessage());
        }
    }

    /**
     * TODO 发送带附件的邮件
     */
    public void sendAttachmentEmail(String deliver, String[] receiver, String[] carbonCopy, String subject, String content, boolean isHtml, String fileName, File file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom(deliver);
            messageHelper.setTo(receiver);
            messageHelper.setCc(carbonCopy);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);
            messageHelper.addAttachment(fileName, file);

            mailSender.send(message);
            logger.info("Send mail success");
        } catch (MessagingException e) {
            logger.error("Send mail failed, error message is {}\n", e.getMessage());
        }
    }


    /**
     * TODO 发送模板邮件  所用引擎是thymeleaf
     */
    public void sendTplEmail(String deliver, String[] receiver, String[] carbonCopy, String subject, String template, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            String content = templateEngine.process(template, context);
            messageHelper.setFrom(deliver);
            messageHelper.setTo(receiver);
            messageHelper.setCc(carbonCopy);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            mailSender.send(message);
            logger.info("Send mail success");
        } catch (MessagingException e) {
            logger.error("Send mail failed, error message is {} \n", e.getMessage());
        }
    }
}
