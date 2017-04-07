package com.easysoft.ecommerce.util.jobs;

import com.easysoft.ecommerce.dao.EmailServerDao;
import com.easysoft.ecommerce.dao.EmailSiteDao;
import com.easysoft.ecommerce.dao.EmailTemplateDao;
import com.easysoft.ecommerce.dao.ProductDao;
import com.easysoft.ecommerce.model.*;
import com.easysoft.ecommerce.service.impl.URLUTF8Encoder;
import com.easysoft.ecommerce.util.MoneyRange;
import com.easysoft.ecommerce.util.WebUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.velocity.app.VelocityEngine;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Auto email sending
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 */

public class AutoEmailSending {

    public static int NUM_EMAIL_LOAD = 5000;

    List<String> messages = new ArrayList<String>();

    VelocityEngine velocityEngine;
    StandardPBEStringEncryptor strongEncryptor;
    EmailServerDao emailServerDao;
    EmailSiteDao emailSiteDao;
    EmailTemplateDao emailTemplateDao;
    ProductDao productDao;
    Site site;
    String emailListFile;
    String emailProcessedFile;
    CacheManager cacheManager;


    public AutoEmailSending(ProductDao productDao, EmailServerDao emailServerDao, StandardPBEStringEncryptor strongEncryptor, VelocityEngine velocityEngine, EmailSiteDao emailSiteDao, EmailTemplateDao emailTemplateDao, CacheManager cacheManager, Site site, String emailListFile, String emailProcessedFile) {
        this.emailServerDao = emailServerDao;
        this.emailSiteDao = emailSiteDao;
        this.emailTemplateDao = emailTemplateDao;
        this.velocityEngine = velocityEngine;
        this.strongEncryptor = strongEncryptor;
        this.site = site;
        this.emailListFile = emailListFile;
        this.emailProcessedFile = emailProcessedFile;
        this.productDao = productDao;
        this.cacheManager = cacheManager;
    }

    public boolean sendingEmailFromFile() {
        FileInputStream fs = null;
        boolean hasFile = true;
        try {
            fs = new FileInputStream(emailListFile);
        } catch (FileNotFoundException e) {
            hasFile = false;
        }
        //if the email file exist. Process email
        if (hasFile) {

            //load the position email that processed in the previous.
            int startFrom = 0;
            try {
                FileInputStream fsPosition = new FileInputStream(emailProcessedFile);
                BufferedReader brPosition = new BufferedReader(new InputStreamReader(fsPosition));
                String position = brPosition.readLine();
                if (StringUtils.isNumeric(position)) {
                    startFrom = Integer.valueOf(position);
                }
                brPosition.close();
                fsPosition.close();
            } catch (FileNotFoundException e) {
                startFrom = 0;
            } catch (IOException e) {
                startFrom = 0;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            //ignore emails were processed.
            for (int i = 0; i < startFrom; ++i) {
                try {
                    br.readLine();
                } catch (IOException e) {
                    messages.add(e.getMessage());
                    return false; //show that this job is FAIL for some reason
                }
            }
            //Starting collect email and put into List
            List<String> emailList = new ArrayList<String>();
            try {
                String email;
                while ((email = br.readLine()) != null) {
                    emailList.add(email);
                    if (emailList.size() > NUM_EMAIL_LOAD) {
                        break;
                    }
                }
            } catch (IOException e) {
                messages.add(e.getMessage());
                return false; //show that this job is FAIL for some reason
            } finally {
                //close stream/buffer of openned file
                try {
                    br.close();
                    fs.close();
                } catch (IOException ignored) {

                }
            }

            List<Product> products = productDao.findBy("productMarketing", "Y");

            //Get email template
            EmailSite emailSite = emailSiteDao.getEmailInfor(site.getId(), "autoemail");
            if (emailSite != null) {
                EmailTemplate emailTemplate = emailTemplateDao.findById(emailSite.getEmailTemplateId());
                String subject = emailTemplate.getSubject();

                //Starting sending
                List<EmailServer> emailServers = emailServerDao.findAll();
                int index = 0;

                try {

                    EmailValidator emailValidator = EmailValidator.getInstance();
                    for (EmailServer emailServerInfor : emailServers) {
                        //Email Server configuration
                        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                        mailSender.setHost(emailServerInfor.getSmtp());
                        mailSender.setPort(emailServerInfor.getPort());
                        mailSender.setUsername(emailServerInfor.getUserName());
                        mailSender.setPassword(emailServerInfor.getPassword());
                        mailSender.setDefaultEncoding(emailServerInfor.getEncoding());
                        Properties prop = new Properties();
                        prop.setProperty("mail.smtp.auth", "true");
                        prop.setProperty("mail.smtp.starttls.enable", "true");
                        mailSender.setJavaMailProperties(prop);
                        String sender = emailServerInfor.getSender();
                        if (StringUtils.isEmpty(sender)) {
                            sender = "Cung Shopping <noreply@cungshopping.com>";
                        }

                        try {

                            //starting sending email on each server.
                            int emailsPerSend = emailServerInfor.getEmailsPerSend();
                            int start = 0;
                            //i will start from index on each email server.
                            //start will reach emails per sending time
                            //index will go through the email list
                            for (int i = index; start < emailsPerSend; i++, index++) {
                                //if email still be in queue
                                if (i < emailList.size()) {
                                    String emailWillSend = emailList.get(i);
                                    if (emailValidator.isValid(emailWillSend)) {
                                        Map map = new HashMap();
                                        map.put("products", products);
                                        map.put("MoneyRange", MoneyRange.class);
                                        map.put("site", site);

                                        map.put("email", URLUTF8Encoder.encode(strongEncryptor.encrypt(emailWillSend)));
                                        sendEmail(sender, emailWillSend, null, null, subject, map, emailTemplate.getTemplateFile(), mailSender);
                                        start++;
                                    }
                                } else { //all email in email file was sent. Should rename it for backup
                                    WebUtil.renameFile(emailListFile, emailListFile + ".bk");
                                    messages.add("Emails file was completed sending.Please upload another email file");
                                    sendEmail(sender, site.getSiteParamsMap().get("WEBMASTER_EMAIL"), null, null, "Emails file was completed sending.Please upload another email file", "Emails file was completed sending.Please upload another email file", true, mailSender);
                                    return true;
                                }

                            }

                        } catch (Exception e) {
                            messages.add("SMTP:" + mailSender.getUsername() +": Exception message:" + e.getMessage());
                        }

                    }
                } finally {
                    //write the position of email that is processing. Next time, start from this position.
                    WebUtil.writeFile(emailProcessedFile, (startFrom + index) + "");
                }


            } else {
                messages.add("Email template for autoemail has not been existed");
                return false;
            }

        }

        return true;
    }

    private boolean isExcluded(String emailWillSend) {
        return emailWillSend.trim().endsWith("vnn.vn") ||
                emailWillSend.trim().endsWith("fpt.vn");
    }

    public void sendEmail(final String from, final String to, final String[] cc, final String[] bcc, final String subject, final Map map, final String emailTemplate, final JavaMailSender javaMailSender) throws MailException {
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
        javaMailSender.send(preparator);

    }

    public void sendEmail(String from, String to, final String[] cc, final String[] bcc, String subject, String body, boolean isHtml, JavaMailSender javaMailSender) throws MailException {
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
        javaMailSender.send(mail);
    }


    public List<String> getMessages() {
        return messages;
    }

    public boolean filterValidEmail (String emailsFile) {

        FileInputStream fs = null;
        boolean hasFile = true;
        try {
            fs = new FileInputStream(emailsFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            try {
                StringBuilder buf = new StringBuilder();
                EmailValidator validator = EmailValidator.getInstance();
                String email;
                while ((email = br.readLine()) != null) {
                    if (validator.isValid(email) && isAddressValid(email)) {
                        buf.append(email).append('\n');
                    }
                }

                //Write the valid emails to the file
                File file = new File(emailsFile+".val");
                Writer output = new BufferedWriter(new FileWriter(file));
                output.write(buf.toString());
                output.close();

            } catch (IOException ignored) {
                return false;
            } finally {
                //close stream/buffer of openned file
                try {
                    br.close();
                    fs.close();
                } catch (IOException ignored) {
                    return false;
                }
            }
        } catch (FileNotFoundException ignored) {
            return false;
        }
        return true;
    }

    private int hear( BufferedReader in ) throws IOException {
        String line = null;
        int res = 0;

        while ( (line = in.readLine()) != null ) {
            String pfx = line.substring( 0, 3 );
            try {
                res = Integer.parseInt( pfx );
            }
            catch (Exception ex) {
                res = -1;
            }
            if ( line.charAt( 3 ) != '-' ) break;
        }

        return res;
    }

    private void say( BufferedWriter wr, String text )
            throws IOException {
        wr.write( text + "\r\n" );
        wr.flush();

    }
    private ArrayList getMX( String hostName )
            throws NamingException {
        // Perform a DNS lookup for MX records in the domain
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext( env );
        Attributes attrs = ictx.getAttributes
                ( hostName, new String[] { "MX" });
        Attribute attr = attrs.get( "MX" );

        // if we don't have an MX record, try the machine itself
        if (( attr == null ) || ( attr.size() == 0 )) {
            attrs = ictx.getAttributes( hostName, new String[] { "A" });
            attr = attrs.get( "A" );
            if( attr == null )
                throw new NamingException
                        ( "No match for name '" + hostName + "'" );
        }
        // Huzzah! we have machines to try. Return them as an array list
        // NOTE: We SHOULD take the preference into account to be absolutely
        //   correct. This is left as an exercise for anyone who cares.
        ArrayList res = new ArrayList();
        NamingEnumeration en = attr.getAll();

        while ( en.hasMore() ) {
            String mailhost;
            String x = (String) en.next();
            String f[] = x.split( " " );
            //  THE fix *************
            if (f.length == 1)
                mailhost = f[0];
            else if ( f[1].endsWith( "." ) )
                mailhost = f[1].substring( 0, (f[1].length() - 1));
            else
                mailhost = f[1];
            //  THE fix *************
            res.add( mailhost );
        }
        return res;
    }

    public boolean isAddressValid( String address ) {
        // Find the separator for the domain name
        int pos = address.indexOf( '@' );

        // If the address does not contain an '@', it's not valid
        if ( pos == -1 ) return false;

        // Isolate the domain/machine name and get a list of mail exchangers
        String domain = address.substring( ++pos );
        ArrayList mxList = null;
        try {
            Cache cache = this.cacheManager.getCache("CacheTag");
            Element element = cache.get(domain);
            if (element != null) {
                mxList = (ArrayList) element.getValue();
            } else {
                mxList = getMX( domain );
                element = new Element(domain, mxList);
                cache.put(element);
            }
        }
        catch (NamingException ex) {
            return false;
        }

        // Just because we can send mail to the domain, doesn't mean that the
        // address is valid, but if we can't, it's a sure sign that it isn't
        if ( mxList.size() == 0 ) return false;

        // Now, do the SMTP validation, try each mail exchanger until we get
        // a positive acceptance. It *MAY* be possible for one MX to allow
        // a message [store and forwarder for example] and another [like
        // the actual mail server] to reject it. This is why we REALLY ought
        // to take the preference into account.
        for (Object aMxList : mxList) {
            boolean valid = false;
            try {
                int res;
                //
                Socket skt = new Socket((String) aMxList, 25);
                BufferedReader rdr = new BufferedReader
                        (new InputStreamReader(skt.getInputStream()));
                BufferedWriter wtr = new BufferedWriter
                        (new OutputStreamWriter(skt.getOutputStream()));

                res = hear(rdr);
                if (res != 220) throw new Exception("Invalid header");
                say(wtr, "EHLO rgagnon.com");

                res = hear(rdr);
                if (res != 250) throw new Exception("Not ESMTP");

                // validate the sender address
                say(wtr, "MAIL FROM: <tim@orbaker.com>");
                res = hear(rdr);
                if (res != 250) throw new Exception("Sender rejected");

                say(wtr, "RCPT TO: <" + address + ">");
                res = hear(rdr);

                // be polite
                say(wtr, "RSET");
                hear(rdr);
                say(wtr, "QUIT");
                hear(rdr);
                if (res != 250)
                    throw new Exception("Address is not valid!");

                valid = true;
                rdr.close();
                wtr.close();
                skt.close();
            } catch (Exception ex) {
                // Do nothing but try next host
            } finally {
                if (valid) return true;
            }
        }
        return false;
    }
}
