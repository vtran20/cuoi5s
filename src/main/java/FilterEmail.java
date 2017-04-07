import org.apache.commons.lang.StringUtils;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auto email sending
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 */

public class FilterEmail {

    public static int NUM_EMAIL_LOAD = 5000;
    public static Map<String, List> cache = new HashMap<String, List>();
    public static String[] excludes = {".ca", ".uk", ".sg", ".tw", ".hk", ".cn",
            ".nz", ".fr", "vnn.vn", "fpt.vn", ".dk", ".de", ".es", ".au"};
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );

    public static void main(String[] args) {
        //get all the file in current folder
        /*
        FilterEmail filterEmail = new FilterEmail();
        File files[] = filterEmail.finder("c:\\usr\\test\\");
        for (File file: files) {
            filterEmail.filterValidEmail(file.getAbsolutePath());
        }
        */

        FilterEmail filterEmail = new FilterEmail();
        filterEmail.findEmail();
    }

    public File[] finder(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".txt");
            }
        });

    }

    public boolean filterValidEmail(String emailsFile) {

        FileInputStream fs = null;
        try {
            fs = new FileInputStream(emailsFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            try {
                StringBuilder buf = new StringBuilder();
                String email;
                while ((email = br.readLine()) != null) {
                    if (rfc2822.matcher(email).matches() && !isExcluded(email) && isAddressValid(email)) {
                        buf.append(email).append('\n');
                    }
                }

                //Write the valid emails to the file
                File file = new File(emailsFile + ".val");
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

    private boolean isExcluded(String email) {
        if (email.length() < 35) {
            for (int i = 0, excludesLength = excludes.length; i < excludesLength; i++) {
                String exclude = excludes[i];
                if (email.trim().endsWith(exclude)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private int hear(BufferedReader in) throws IOException {
        String line = null;
        int res = 0;

        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') break;
        }

        return res;
    }

    private void say(BufferedWriter wr, String text)
            throws IOException {
        wr.write(text + "\r\n");
        wr.flush();

    }

    private ArrayList getMX(String hostName)
            throws NamingException {
        // Perform a DNS lookup for MX records in the domain
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial",
                "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext(env);
        Attributes attrs = ictx.getAttributes
                (hostName, new String[]{"MX"});
        Attribute attr = attrs.get("MX");

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[]{"A"});
            attr = attrs.get("A");
            if (attr == null)
                throw new NamingException
                        ("No match for name '" + hostName + "'");
        }
        // Huzzah! we have machines to try. Return them as an array list
        // NOTE: We SHOULD take the preference into account to be absolutely
        //   correct. This is left as an exercise for anyone who cares.
        ArrayList res = new ArrayList();
        NamingEnumeration en = attr.getAll();

        while (en.hasMore()) {
            String mailhost;
            String x = (String) en.next();
            String f[] = x.split(" ");
            //  THE fix *************
            if (f.length == 1)
                mailhost = f[0];
            else if (f[1].endsWith("."))
                mailhost = f[1].substring(0, (f[1].length() - 1));
            else
                mailhost = f[1];
            //  THE fix *************
            res.add(mailhost);
        }
        return res;
    }

    public boolean isAddressValid(String address) {
        // Find the separator for the domain name
        int pos = address.indexOf('@');

        // If the address does not contain an '@', it's not valid
        if (pos == -1) return false;

        // Isolate the domain/machine name and get a list of mail exchangers
        String domain = address.substring(++pos);
        List mxList = null;
        try {
            mxList = cache.get(domain);
            if (mxList == null) {
                mxList = getMX(domain);
                cache.put(domain, mxList);
            }
        } catch (NamingException ex) {
            return false;
        }

        // Just because we can send mail to the domain, doesn't mean that the
        // address is valid, but if we can't, it's a sure sign that it isn't
        return mxList.size() != 0;

        // Now, do the SMTP validation, try each mail exchanger until we get
        // a positive acceptance. It *MAY* be possible for one MX to allow
        // a message [store and forwarder for example] and another [like
        // the actual mail server] to reject it. This is why we REALLY ought
        // to take the preference into account.
        /*
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
        */
    }

    public void findEmail() {
        Pattern pattern = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
//        Pattern pattern = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
        String str = "asdf@yahoo.com abdc <asdf@yahoo.com> kjf < ssdkfj@yahoo.com > sdfkj";
        if (!StringUtils.isEmpty(str)) {
            Matcher matcher = pattern.matcher(str);
            // Get the match result
            String email = matcher.group();
            System.out.println("email:" + email);
        }
    }
}
