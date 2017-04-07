package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.dao.SiteMenuPartContentDao;
import com.easysoft.ecommerce.service.MailService;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;


@Service
//@Transactional
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private SiteMenuPartContentDao siteMenuPartContentDao;
//    static Map <Long, Template>templates =  new HashMap<Long, Template>();

    public void sendEmailFromTemplateContent(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplateContent) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setFrom(from);
                message.setTo(to);
                if (cc != null) {
                    message.setCc(cc);
                }
                if (bcc != null) {
                    message.setBcc(bcc);
                }
                message.setSubject(subject);

                String body = "";
                if (!StringUtils.isEmpty(emailTemplateContent)) {
                    StringWriter write = new StringWriter();

                    //Create Velocity Template
                    RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
                    StringReader reader = new StringReader(emailTemplateContent);
                    SimpleNode node = null;
                    try {
                        node = runtimeServices.parse(reader, from+subject);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Template template = new Template();
                    template.setRuntimeServices(runtimeServices);
                    template.setData(node);
                    template.initDocument();
                    template.setEncoding("UTF-8");

//                    String imageServer = ServiceLocatorHolder.getServiceLocator().getSystemContext().getGlobalConfig("image.server");
//                    map.put("imageServer",imageServer);
                    VelocityContext velocityContext = new VelocityContext(map);
                    template.merge(velocityContext, write);
                    body = write.toString();
                }

                message.setText(body, true);
            }
        };
        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
            ex.printStackTrace();
        }

    }

    public void sendEmail(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplate) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setFrom(from);
                message.setTo(to);
                if (cc != null) {
                    message.setCc(cc);
                }
                if (bcc != null) {
                    message.setBcc(bcc);
                }
                message.setSubject(subject);
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, emailTemplate, "UTF-8", map);
                message.setText(text, true);
            }
        };
        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void sendEmail(String from, String to, final String[] cc, final String[] bcc, String subject, String body, boolean isHtml) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(to);
        if (cc != null) {
            mail.setCc(cc);
        }
        if (bcc != null) {
            mail.setBcc(bcc);
        }
        mail.setSubject(subject);
        mail.setText(body);
        try {
            this.mailSender.send(mail);
        } catch (MailException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void sendEmail(String from, String to, final String[] cc, final String[] bcc, String subject, String body) {
        sendEmail(from, to, cc, bcc, subject, body, true);
    }

}
