package com.easysoft.ecommerce.util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * Money Tester.
 *
 * @author <Authors name>
 * @since <pre>08/04/2010</pre>
 * @version 1.0
 */
public class MoneyTest extends TestCase {
    public MoneyTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetGetAmount() throws Exception {
    }

    public void testSetGetCurrencyFormat() throws Exception {
    }

    public void testSetGetCurrency() throws Exception {
    }

    public void testToString() throws Exception {
        //VND
        Money m = new Money (new BigDecimal(120000), "VND", "#,###,###");
        Money m2 = new Money (new BigDecimal(100000), "VND", "#,###,###");
        assertEquals(m.toString(), "120.000 VND");
        assertEquals(m.getMoneyValue(), "120.000");
//        assertEquals(m.getAmount().longValue(), 120000);
        assertEquals(m.plus(m2), new Money (new BigDecimal(220000), "VND", "#,###,###"));
        assertEquals(m.minus(m2), new Money (new BigDecimal(20000), "VND", "#,###,###"));

        //USA
        m = new Money (new BigDecimal(1200.01), "$", "####.00");
        m2 = new Money (new BigDecimal(1000.00), "$", "####.00");
        assertEquals(m.toString(), "1200.01 $");
        assertEquals(m.getMoneyValue(), "1200.01");
//        assertEquals(m.getAmount().doubleValue(), 1200.01d);
        assertEquals(m.plus(m2).toString(), new Money (new BigDecimal(2200.01), "$", "####.00").toString());
        assertEquals(m.minus(m2).toString(), new Money (new BigDecimal(200.01), "$", "####.00").toString());

        //Testing for MoneyRange for VND (=, >, <, null)
        MoneyRange mr = null;
        mr = MoneyRange.valueOf("120000-120000", "VND", "#,###,###");
        assertEquals("120.000 VND", mr.toString());
        mr = MoneyRange.valueOf("120000-121000", "VND", "#,###,###");
        assertEquals("120.000 - 121.000 VND", mr.toString());
        mr = MoneyRange.valueOf("-", "VND", "#,###,###");
        assertEquals("", mr.toString());
        mr = MoneyRange.valueOf("", "VND", "#,###,###");
        assertEquals("", mr.toString());
        mr = MoneyRange.valueOf("100000", "VND", "#,###,###");
        assertEquals("100.000 VND", mr.toString());
        mr = MoneyRange.valueOf("-100000", "VND", "#,###,###");
        assertEquals("100.000 VND", mr.toString());
        mr = MoneyRange.valueOf("100000-", "VND", "#,###,###");
        assertEquals("100.000 VND", mr.toString());

        

//        //Test for promotion
//        assertEquals(generatePriceMin(0l, 5l), new IllegalArgumentException("MinPrice is invalid. It should be greater zero"));
//        assertEquals(generatePriceMin(0l, 0l), new IllegalArgumentException("MinPrice is invalid. It should be greater zero"));
//        assertEquals(generatePriceMin(5l, 4l), 4);
//        assertEquals(generatePriceMin(5l, 5l), 5);
//        assertEquals(generatePriceMin(5l, 0l), 5);
//
//        assertEquals(generateDisplayPromoPrice(7l, 8l, 0l, 0l), "0");
//        assertEquals(generateDisplayPromoPrice(7l, 8l, 0l, 5l), "5-8");
//        assertEquals(generateDisplayPromoPrice(7l, 8l, 2l, 5l), "2-8");
//        assertEquals(generateDisplayPromoPrice(7l, 8l, 5l, 5l), "0");
//        assertEquals(generateDisplayPromoPrice(7l, 7l, 0l, 0l), "0");
//        assertEquals(generateDisplayPromoPrice(7l, 7l, 0l, 0l), "0");
//        assertEquals(generateDisplayPromoPrice(7l, 7l, 2l, 5l), "0");
//        assertEquals(generateDisplayPromoPrice(7l, 7l, 5l, 5l), "0");


    }

    public void submittingForm() throws Exception {
        final WebClient webClient = new WebClient();

        // Get the first page
        webClient.setThrowExceptionOnScriptError(false);
        final HtmlPage page1 = webClient.getPage("http://www.esnips.com/HtmlmailAction.ns");
//        try {
//
//        } catch (UnknownHostException e) {
//
//        } catch (ScriptException b) {
//
//        }
        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
        final HtmlForm form = page1.getFormByName("FormHtmlmail");

        final HtmlAnchor  button = page1.getHtmlElementById("sendButton");
        final HtmlTextInput from = form.getInputByName("from");
        final HtmlTextArea to = form.getTextAreaByName("to");
        final HtmlTextInput subject = form.getInputByName("subject");
        final HtmlTextArea message = form.getTextAreaByName("message");
        final HtmlHiddenInput style = form.getInputByName("style");

        // Change the value of the text field
        from.setValueAttribute("vuktx@yahoo.com");
        to.setText("vuktx1979@gmail.com");
        to.setDefaultValue("vuktx1979@gmail.com");
        subject.setValueAttribute("hello vuktx1979@gmail.com");
        message.setText("hello vuktx1979@gmail.com");
//        style.setValueAttribute("single-file-clean");

        // Now submit the form by clicking the button and get back the second page.
        webClient.setThrowExceptionOnScriptError(false);
        final HtmlPage page2 = button.click();
        System.out.println(page2.asXml());
        webClient.closeAllWindows();
    }

    public void sendMail() throws Exception {

        String url ="http://www.esnips.com/HtmlmailAction.ns";

        String __gads = "ID=8805d6cfb4aea36b:T=1302101468:S=ALNI_MaJuSNzLlpAzI5DPTmsZCZGWcXxDQ";
        String __utmz = "46480307.1302101600.1.1.utmccn=(direct)|utmcsr=(direct)|utmcmd=(none)";
        String JSESSIONID = "B84D34D1A598009691C052E35FC54008";
        String BIGipServerwww = "2533363884.36895.0000";
        String fbs_372851285608 = "";
        String __utmb = "46480307";
        String __utmc = "46480307";
        String __utma = "46480307.1820257717.1302101595.1302103744.1302107166.3";



    }

      public static String convertStreamToString(InputStream is) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      sb.append(line + "\n");
    }
    is.close();
    return sb.toString();
  }

        private String generateDisplayPromoPrice(Long minPrice, Long maxPrice, Long minPricePromo, Long maxPricePromo) {
        if (minPricePromo == 0 && maxPricePromo == 0) {
            return "0";
        } else {
            Long min = generatePriceMin(minPrice, minPricePromo);
            Long max = maxPrice > maxPricePromo ? maxPrice : maxPricePromo;
            if (min.longValue() == max.longValue()) {
                return min.toString();
            } else {
                return min + "-" + max;
            }
        }
    }

    private String generateDisplayPrice(Long minPrice, Long maxPrice) {
        if (minPrice.longValue() == maxPrice.longValue()) {
            return minPrice.toString();
        } else {
            return minPrice + "-" + maxPrice;
        }
    }

    private long generatePriceMin(Long minPrice, Long minPricePromo) {
        long result = 0;
        if (minPricePromo <= 0) {
            result = minPrice;
        } else if (minPrice > minPricePromo) {
            result = minPricePromo;
        } else {
            result = minPrice;
        }
        if (result == 0) throw new IllegalArgumentException("MinPrice is invalid. It should be greater zero");
        return result;
    }

    public static Test suite() {
        return new TestSuite(MoneyTest.class);
    }
}
