package com.easysoft.ecommerce.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import com.easysoft.ecommerce.model.*;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.*;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.jsoup.nodes.Document;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * This is an utility class that provide utility methods for entire web application.
 * <p/>
 * User: Vu Tran
 * Date: Jul 20, 2010
 * Time: 12:05:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {

    public static String POS_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static String getRelationType(int relationType) {
        switch (relationType) {
            case 1:
                return "San pham";
            case 2:
                return "Phu kien";
            default:
                return "";
        }
    }

    public static boolean isChecked(String value) {
        return value != null && !(value.trim().equalsIgnoreCase("false") ||
                value.trim().equalsIgnoreCase("f") ||
                value.trim().equalsIgnoreCase("no") ||
                value.trim().equalsIgnoreCase("n") ||
                value.trim().equalsIgnoreCase("0"));

    }

    public static Map<String, String> getRefinementMap(List<String> refinementKey, QueryString params) {
        Map<String, String> refinements = new HashMap<String, String>();
        if (refinementKey != null) {
            for (String key : refinementKey) {
                refinements.put(key, params.getParameter(key));
            }
        }
        return refinements;
    }

    public static Map getColorProductVariant(List<ProductVariant> productVariants) {
        Map<String, ProductVariant> colorMap = new HashMap<String, ProductVariant>();

        if (productVariants != null && !productVariants.isEmpty() && productVariants.size() > 1) {
            for (ProductVariant pv : productVariants) {
                colorMap.put(pv.getColorCode(), pv);
            }
        }
        return colorMap;
    }

    /**
     * get the range or promo price. If promo price = 0 then ignore
     *
     * @param list
     * @return
     */
    public static String generateDisplayPromoPrice(List<Object[]> list) {
        if (list != null) {
            if (list.size() > 1) {
                Long min = Long.MAX_VALUE;
                Long max = 0l;
                boolean noPromo = true;
                for (Object[] obj : list) {
                    Long price = (Long) obj[0];
                    Long pricePromo = (Long) obj[1];
                    if (pricePromo > 0) noPromo = false;
                    min = Math.min(generatePriceMin(price, pricePromo), min);
                    max = Math.max(generatePriceMin(price, pricePromo), max);
                }
                if (noPromo) {
                    return "";
                } else {
                    if (min.longValue() == max.longValue()) {
                        return min.toString();
                    } else {
                        return min + "-" + max;
                    }
                }
            } else if (list.size() == 1) {
                Object[] obj = list.get(0);
                Long pricePromo = (Long) obj[1];
                if (pricePromo == 0) {
                    return "";
                } else {
                    return pricePromo.toString();
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Return display price. Base on price of product variant.
     * @param minPrice
     * @param maxPrice
     * @return
     */
    public static String generateDisplayPrice(Long minPrice, Long maxPrice) {
        if (minPrice.longValue() == maxPrice.longValue()) {
            return minPrice.toString();
        } else {
            return minPrice + "-" + maxPrice;
        }
    }

    /**
     * Return min of 2 prices. If one price = 0, return the rest.
     *
     * @param minPrice      - this value is used for sort by price
     * @param minPricePromo
     * @return
     */
    public static long generatePriceMin(Long minPrice, Long minPricePromo) {
        long result = 0;
        if (minPricePromo <= 0) {
            result = minPrice;
        } else if (minPrice > minPricePromo) {
            result = minPricePromo;
        } else {
            result = minPrice;
        }
        //Price can be zero in the case it is free.
        //if (result == 0) throw new IllegalArgumentException("MinPrice is invalid. It should be greater zero");
        return result;
    }

    /**
     * Calculate price min base on display price and display price promo.
     *
     * @param displayPrice
     * @param displayPricePromo
     * @return
     */
    public static long generatePriceMin(String displayPrice, String displayPricePromo) {
        long result = 0;
        if (StringUtils.isEmpty(displayPrice)) {
            throw new IllegalArgumentException("Display price cannot null");
        }
        if (StringUtils.isEmpty(displayPricePromo)) {
            String prices[] = displayPrice.split("-");
            if (prices.length >= 1) {
                result = Long.valueOf(prices[0]);
            }
        } else {
            String prices[] = displayPrice.split("-");
            String pricesPromo[] = displayPricePromo.split("-");
            if (prices.length >= 1 && pricesPromo.length >= 1) {
                result = generatePriceMin(Long.valueOf(prices[0]), Long.valueOf(pricesPromo[0]));
            }
        }
        return result;
    }

    public static List<ProductVariant> getInventoryProductVariants(List<ProductVariant> variants) {
        if (variants != null && !variants.isEmpty()) {
            List<ProductVariant> vars = new ArrayList<ProductVariant>();
            for (ProductVariant variant : variants) {
                if (variant.getInventory() > 0) {
                    vars.add(variant);
                }
            }
            return vars;
        } else {
            return variants;
        }
    }

    /**
     * Return variant group base on product and product variant information.
     *
     * @param product
     * @return
     */
    public static String getVariantGroup (Product product, List<ProductVariant> variants ) {
        if (product.getId() == null) return "N";
        if (variants == null) return "N";
        if (variants.size() == 1) {
            ProductVariant variant = variants.get(0);
            if (!StringUtils.isEmpty(variant.getColorCode()) && !StringUtils.isEmpty(variant.getSizeCode())) {
                return "B";
            } else if (!StringUtils.isEmpty(variant.getColorCode())) {
                return "C";
            } else if (!StringUtils.isEmpty(variant.getSizeCode())) {
                return "S";
            } else {
                return "N";
            }
        } else {
            int color = 0;
            int size  = 0;
            for (ProductVariant variant : variants) {
                if (!StringUtils.isEmpty(variant.getColorCode())) {
                    color++;
                }
                if (!StringUtils.isEmpty(variant.getSizeCode())) {
                    size++;
                }
            }
            if (color >= 2 && size >= 2) {
                return "B";
            } else if (color >= 2) {
                return "C";
            } else if (size >= 2) {
                return "S";
            } else {
                return "N";
            }
        }
    }

    /**
     * Generate a random String suitable for use as a temporary password.
     *
     * @return String suitable for use as a temporary password
     * @since 2.4
     */
    public static String generateRandomPassword(int length) {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
        Random RANDOM = new SecureRandom();
        String pw = "";
        for (int i = 0; i < length; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    /**
     * Convert VNese to Unsign VNese text
     *
     * @param input: unicode text
     * Convert VNese to unsign VNese.
     * /(�|�|?|?|�|�|?|?|?|?|?|?|?|?|?|?|?|�|�|?|?|�|�|?|?|?|?|?|?|?|?|?|?|?)/
     * /(�|�|?|?|?|�|?|?|?|?|?|�|�|?|?|?|�|?|?|?|?|?)/
     * /(�|�|?|?|?|�|�|?|?|?)/
     * /(�|�|?|?|�|�|?|?|?|?|?|?|?|?|?|?|?|�|�|?|?|�|�|?|?|?|?|?|?|?|?|?|?|?)/
     * /(�|�|?|?|?|?|?|?|?|?|?|�|�|?|?|?|?|?|?|?|?|?)/
     * /(?|�|?|?|?|?|�|?|?|?)/
     * /(?|?)/
     *
     * @return unsign VNese.
     */
    public static String getURI (String input) {
        if (StringUtils.isEmpty(input)) return "";

        input = input.replaceAll("[.,'\"!><&@#$%*()=+;:?/]","-");

        char[] arr = input.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            switch (arr[i]) {
                case '\u00e0': case '\u00e1': case '\u1ea1': case '\u1ea3':
                case '\u00e3': case '\u00e2': case '\u1ea7': case '\u1ea5':
                case '\u1ead': case '\u1ea9': case '\u1eab': case '\u0103':
                case '\u1eb1': case '\u1eaf': case '\u1eb7': case '\u1eb3':
                case '\u1eb5': case '\u00c0': case '\u00c1': case '\u1ea0':
                case '\u1ea2': case '\u00c3': case '\u00c2': case '\u1ea6':
                case '\u1ea4': case '\u1eac': case '\u1ea8': case '\u1eaa':
                case '\u0102': case '\u1eb0': case '\u1eae': case '\u1eb6':
                case '\u1eb2': case '\u1eb4': input = input.replace(arr[i], 'a');  break;

                case '\u00e8': case '\u00e9': case '\u1eb9': case '\u1ebb':
                case '\u1ebd': case '\u00ea': case '\u1ec1': case '\u1ebf':
                case '\u1ec7': case '\u1ec3': case '\u1ec5': case '\u00c8':
                case '\u00c9': case '\u1eb8': case '\u1eba': case '\u1ebc':
                case '\u00ca': case '\u1ec0': case '\u1ebe': case '\u1ec6':
                case '\u1ec2': case '\u1ec4': input = input.replace(arr[i], 'e'); break;

                case '\u00ec': case '\u00ed': case '\u1ecb': case '\u1ec9':
                case '\u0129': case '\u00cc': case '\u00cd': case '\u1eca':
                case '\u1ec8': case '\u0128': input = input.replace(arr[i], 'i'); break;

                case '\u00f2': case '\u00f3': case '\u1ecd': case '\u1ecf':
                case '\u00f5': case '\u00f4': case '\u1ed3': case '\u1ed1':
                case '\u1ed9': case '\u1ed5': case '\u1ed7': case '\u01a1':
                case '\u1edd': case '\u1edb': case '\u1ee3': case '\u1edf':
                case '\u1ee1': case '\u00d2': case '\u00d3': case '\u1ecc':
                case '\u1ece': case '\u00d5': case '\u00d4': case '\u1ed2':
                case '\u1ed0': case '\u1ed8': case '\u1ed4': case '\u1ed6':
                case '\u01a0': case '\u1edc': case '\u1eda': case '\u1ee2':
                case '\u1ede': case '\u1ee0': input = input.replace(arr[i], 'o'); break;

                case '\u00f9': case '\u00fa': case '\u1ee5': case '\u1ee7':
                case '\u0169': case '\u01b0': case '\u1eeb': case '\u1ee9':
                case '\u1ef1': case '\u1eed': case '\u1eef': case '\u00d9':
                case '\u00da': case '\u1ee4': case '\u1ee6': case '\u0168':
                case '\u01af': case '\u1eea': case '\u1ee8': case '\u1ef0':
                case '\u1eec': case '\u1eee': input = input.replace(arr[i], 'u'); break;

                case '\u1ef3': case '\u00fd': case '\u1ef5': case '\u1ef7':
                case '\u1ef9': case '\u1ef2': case '\u00dd': case '\u1ef4':
                case '\u1ef6': case '\u1ef8': input = input.replace(arr[i], 'y'); break;

                case '\u0111': case '\u0110': input = input.replace(arr[i], 'd'); break;

                case ' ': input = input.replace(arr[i], '-'); break;

                default : break;
            }

        }
        input = input.replaceAll("--+", "-");
        return input.toLowerCase();
    }

    public static boolean renameFile (String source, String dest) {
        // File (or directory) with old name
        File file = new File(source);
        // Destination directory
        File file2 = new File(dest);
        // File (or directory) with new name
        return file.renameTo(file2);
    }

    public static void writeFile (String fileName, String value) {
        File file = new File(fileName);
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(value);
            output.close();
        } catch (IOException ignored) {

        }
    }

    public static boolean reOrderValidation (String orderList, String currentItem) {
        String []array = orderList.split(",");
        String [] currItem = currentItem.split("-");
        if (StringUtils.isEmpty(orderList) || StringUtils.isEmpty(currentItem)) {
            return false;
        }

        /*identify previous next item after current one is moved*/
        String previous = "";
        String next = "";
        for (int i=0; i < array.length; i++) {
            String item = array[i];
            if (currentItem.equals(item)) {
                if (i + 1 < array.length) {
                    next = array[i+1];
                }
                break;
            } else {
                previous = item;
            }
        }
        String [] prevArray = previous.split("-");
        String [] nextArray = next.split("-");

        String []subItems = currentItem.split("-");

        // Check moving sub item to parent item. This is invalid
        if (subItems.length == 3) {
            if (StringUtils.isEmpty(previous)) {
                return false;
            } else {
                if (!currItem[0].equals(prevArray[0])) {
                    return false;
                }
            }
        } else if (subItems.length == 2) { // Check moving parent to become sub item. This is invalid
            if (prevArray.length == 3 && nextArray.length == 3) {
                return false;
            }
        }
        return true;
    }

    public static Float getSequence (List <String> listOrder, String currentItem) {
        Float seq = 0f;
        int index = listOrder.indexOf(currentItem);
        if (index == 0) {
            if (listOrder.size() > 1) {
                String[] temp = listOrder.get(1).split("-s");
                if (temp.length == 2) {
                    seq = Float.valueOf(temp[1]) / 2f;
                }
            }
        } else if (index == listOrder.size() - 1) {
            if (listOrder.size() > 1) {
                String[] temp = listOrder.get(listOrder.size() - 2).split("-s");
                if (temp.length == 2) {
                    seq = Float.valueOf(temp[1]) + 1;
                }
            }
        } else if (index > 0 && index < listOrder.size() - 1) {
            String [] prev = listOrder.get(index - 1).split("-s");
            String [] next = listOrder.get(index + 1).split("-s");
            if (prev.length == 2 && next.length == 2) {
                seq = (Float.valueOf(prev[1]) + Float.valueOf(next[1]))/2f;
            }
        }
        return seq;
    }

    public static List<Video> convertFromJson (Map data, Site site, String videoName) {
        List <Video> result = null;
        List<Map> items = (List) data.get("items");
        if (items != null && items.size() > 0) {
            try {
                result = new ArrayList<Video>();
                for (Map videoMap:  items) {
                    Video video = new Video();
                    video.setActive("Y");
                    video.setSource("Youtube");
                    video.setUpdatedDate(new Date());
                    video.setSite(site);
                    String name = null;
                    if (StringUtils.isEmpty(videoName)) {
                        name = ((Map) videoMap.get("snippet")).get("title") + "";
                    } else {
                        name = videoName;
                    }
                    video.setUri(WebUtil.getURI(name));
                    video.setName(name);
                    video.setVideoId(videoMap.get("id")+"");
                    String description = ((Map) videoMap.get("snippet")).get("description")+"";
                    if (!StringUtils.isEmpty(description)) {
                        if (description.length() > 9900) {
                            video.setDescription(description.substring(0, 9900) + "...");
                        } else {
                            video.setDescription(description);
                        }
                    }
                    video.setThumbImgUrl(((Map)((Map)((Map) videoMap.get("snippet")).get("thumbnails")).get("default")).get("url") + "");
                    video.setMediumImgUrl(((Map) ((Map) ((Map) videoMap.get("snippet")).get("thumbnails")).get("medium")).get("url") + "");
                    video.setLargeImgUrl(((Map) ((Map) ((Map) videoMap.get("snippet")).get("thumbnails")).get("high")).get("url") + "");
                    String duration = ((Map) videoMap.get("contentDetails")).get("duration") + "";
                    if (!StringUtils.isEmpty(duration)) {
                        duration = convertJodaToDuration(duration);
                    }
                    video.setDuration(duration);
                    result.add(video);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    public static String convertJodaToDuration (String jodaTime) {
        if (StringUtils.isEmpty(jodaTime)) return null;

        PeriodFormatter formatter = ISOPeriodFormat.standard();
        Period p = formatter.parsePeriod(jodaTime);
        int seconds = p.toStandardSeconds().getSeconds();
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        StringBuilder duration = new StringBuilder();
        if (hours > 0) {
            duration.append(hours);
        }
        if (!StringUtils.isEmpty(duration.toString())) {
            duration.append(":").append(minute).append(":").append(second);
        } else {
            duration.append(minute).append(":").append(second);
        }

        return duration.toString();
    }

    public static String convertActiveFlag(String active) {
        if ("on".equalsIgnoreCase(active) || "Y".equalsIgnoreCase(active) || "true".equals(active)) {
            return "Y";
        } else {
            return "N";
        }
    }

    public static String getTextFromHtml (String html) {
        Document doc = org.jsoup.Jsoup.parse(html);
        return doc.body().text();
    }

    /*This encryption is used for delete image only*/
    static String key = "9283wu827HS*&977"; // 128 bit key
    public static String encrypt (String input) {
        String output = null;
        try {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(input.getBytes());
            output = new BASE64Encoder().encode(encrypted);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String decrypt (String input) {
        String output = null;
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(input);
            output = new String(cipher.doFinal(decordedValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static boolean containCategory(List<Category> categories, Category current) {
        if (current != null) {
            for (Category category: categories) {
                //If current category in the list of categories, return true
                if (current.getId().equals(category.getId())) return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Utility methods for recaptcha
     * @param recaptchaResponse
     * @param secret
     * @param ipAddress
     * @return
     */
    public static boolean isValidCaptcha (String recaptchaResponse, String secret, String ipAddress ) {
        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isEmpty(ipAddress)) {
            ipAddress = "&remoteip=" + ipAddress;
        } else {
            ipAddress = "";//assign empty when it is null
        }
        try {
            Map jsonData = mapper.readValue(new URL("https://www.google.com/recaptcha/api/siteverify?secret="+secret+"&response="+recaptchaResponse+ipAddress).openStream(), HashMap.class);
            return "true".equalsIgnoreCase(jsonData.get("success") + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS5Padding";
    public static String createSToken(String siteSecret) {
        String sessionId = UUID.randomUUID().toString();
        String jsonToken = createJsonToken(sessionId);
        return encryptAes(jsonToken, siteSecret);
    }
    private static String createJsonToken(String sessionId) {
        JsonObject obj = new JsonObject();
        obj.addProperty("session_id", sessionId);
        obj.addProperty("ts_ms", System.currentTimeMillis());
        return new Gson().toJson(obj);
    }
    private static String encryptAes(String input, String siteSecret) {
        try {
            SecretKeySpec secretKey = getKey(siteSecret);
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return BaseEncoding.base64Url().omitPadding().encode(cipher.doFinal(input.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String decryptAes(String input, String key) throws Exception {
        SecretKeySpec secretKey = getKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(
                BaseEncoding.base64Url().omitPadding().decode(input)), "UTF-8");
    }
    private static SecretKeySpec getKey(String siteSecret){
        try {
            byte[] key = siteSecret.getBytes("UTF-8");
            key = Arrays.copyOf(MessageDigest.getInstance("SHA").digest(key), 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseVelocityContent (Map data, String velocityContent) {
        if (!StringUtils.isEmpty(velocityContent)) {
            StringWriter write = new StringWriter();

            //Create Velocity Template
            RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
            StringReader reader = new StringReader(velocityContent);
            SimpleNode node = null;
            try {
                node = runtimeServices.parse(reader, data.hashCode()+"");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Template template = new Template();
            template.setRuntimeServices(runtimeServices);
            template.setData(node);
            template.initDocument();
            template.setEncoding("UTF-8");
            VelocityContext velocityContext = new VelocityContext(data);
            template.merge(velocityContext, write);
            return write.toString();
        }
        return "";
    }

    public static String getEmbedYoutubeVideo (String url) {
        if (!StringUtils.isEmpty(url)) {
            //https://www.youtube.com/watch?v=-5PnhYSuidM
            if (url.contains("watch")) {
                String embedUrl = "https://www.youtube.com/embed/";
                int start = url.indexOf("?");
                if (start < url.length()) {
                    String qs = url.substring(start + 1);
                    QueryString queryString = new QueryString(qs);
                    String videoId = queryString.getParameter("v");
                    embedUrl += videoId;
                    return embedUrl;
                }
            }
        }
        return url;
    }

    public static boolean isToday (Date date) {
        return new DateTime(date).toLocalDate().equals(new LocalDate());
    }
    public static boolean isToday (String d, String format) {
        if (StringUtils.isEmpty(d)) return false;
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(d);
        } catch (java.text.ParseException e) {
            return false;
        }
        return new DateTime(date).toLocalDate().equals(new LocalDate());
    }

    public static String dateToString (Date date, String format) {
        String result = "";
        if (date != null) {
            if (StringUtils.isEmpty(format)) {
                format = "dd/MM/yyyy";
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            result = formatter.format(date);
        }
        return result;
    }

    public static Date stringToDate (String s, String format, Date defaultDate) {
        Date date = stringToDate(s, format);
        if (date == null) {
            date = defaultDate;
        }
        return date;
    }
    public static Date stringToDate (String s, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
//        java.util.Date date1 = new java.util.Date( 1390276603054L );
//        DateTime dateTimeUtc = new DateTime( date1, DateTimeZone.UTC );
        return date;
    }

    public static Calendar getCurrentStartDate() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }
    public static Calendar getStartDate(Date date) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        return startTime;
    }
    public static Calendar getEndDate(Date date) {
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(date);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MILLISECOND, 999);
        return endTime;
    }

    public static String convertHours2AMPM (String input, String format) {
        if (StringUtils.isEmpty(format)) {
            format = "hh:mm";
        }
        String toFormat = "h:ma";
        if (!StringUtils.isEmpty(input)) {
            if(input.endsWith(":00")) {
                toFormat = "ha";
            }
            Date date = stringToDate(input, format);
            return dateToString(date, toFormat).toLowerCase();
        }
        return "";
    }
//    public static void main(String[] args) {

//        ////Get day of week/////
//        Calendar dateOfWeek = Calendar.getInstance();
//        System.out.println(WebUtil.dateToString(dateOfWeek.getTime(), "EEE"));
//        ////Timeslot processing/////
//        Calendar startTime = WebUtil.getCurrentStartDate();
//        startTime.set(Calendar.HOUR_OF_DAY, 10); //start time
//
//        Calendar endTime = WebUtil.getCurrentStartDate();
//        endTime.set(Calendar.HOUR_OF_DAY, 21); //end time
//
//        List<NailCustomerAppointment> appointments = new ArrayList<NailCustomerAppointment>();
//
//        NailCustomerAppointment appointment = new NailCustomerAppointment();
//        Calendar startAppointment = WebUtil.getCurrentStartDate();
//        startAppointment.set(Calendar.HOUR_OF_DAY, 10);
//        startAppointment.set(Calendar.MINUTE, 15);
//        Calendar endAppointment = WebUtil.getCurrentStartDate();
//        endAppointment.set(Calendar.HOUR_OF_DAY, 11);
//        endAppointment.set(Calendar.MINUTE, 15);
//        appointment.setStartTime(startAppointment.getTime());
//        appointment.setEndTime(endAppointment.getTime());
//        appointments.add(appointment);
//
//        appointment = new NailCustomerAppointment();
//        startAppointment = WebUtil.getCurrentStartDate();
//        startAppointment.set(Calendar.HOUR_OF_DAY, 12);
//        startAppointment.set(Calendar.MINUTE, 0);
//        endAppointment = WebUtil.getCurrentStartDate();
//        endAppointment.set(Calendar.HOUR_OF_DAY, 13);
//        endAppointment.set(Calendar.MINUTE, 0);
//        appointment.setStartTime(startAppointment.getTime());
//        appointment.setEndTime(endAppointment.getTime());
//        appointments.add(appointment);
//
//        //all available timeslots
//        Set <Date> timeslots = new LinkedHashSet<Date>();
//        while (!startTime.after(endTime)) {
//            timeslots.add(startTime.getTime());
//            startTime.add(Calendar.MINUTE, 15);
//        }
//
//        //remove booked timeslots
//        for (NailCustomerAppointment appt : appointments) {
//            Calendar time = Calendar.getInstance();
//            time.setTime(appt.getStartTime());
//            while (timeslots.contains(time.getTime()) && time.getTime().before(appt.getEndTime())) {
//                timeslots.remove(time.getTime());
//                time.add(Calendar.MINUTE, 15);
//            }
//        }
//
//        //remove timeslots that cannot book because is not enough time range. Start at a timeslot, if ahead of time have 1 hour available, then the timeslot is valid
//        Set <Date> timeslotsClone = new LinkedHashSet<Date>(timeslots);
//        //available slot if enough 1 hour
//        int slotSteps = 3;
//        for (Date date : timeslotsClone) {
//            Calendar time = Calendar.getInstance();
//            time.setTime(date);
//            for (int i = 0; i < slotSteps; i++) {
//                time.add(Calendar.MINUTE, 15);
//                if (timeslots.contains(time.getTime())) {
//                    //do nothing
//                } else {
//                    timeslots.remove(date);
//                }
//            }
//        }
//
//        //print remain timeslots
//        for (Date ts : timeslots) {
//            System.out.println(dateToString(ts, "HH:mm a"));
//        }
//    }

    private static AmazonSNS snsClient = AmazonSNSClient
            .builder()
            .withRegion("us-east-1")
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("AKIASWZE6T22RYNJBGOL", "Iu024Ggl/7v9ox6ol+T87A5gsbds+ZerLf5uxy+N")))
            .build();
    public static void sendSMSMessage(String message,
                                      String phoneNumber) {
        if (message != null && message.length() >= 160) {
            message = message.substring(0, 159);
        }
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("mySenderID").withDataType("String")); //The sender ID shown on the device.
//        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number")); //Sets the max price to 0.50 USD.
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional").withDataType("String")); //Sets the type to promotional.

        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }

    public static void main(String[] args) {
        String message = "My SMS message";
        String phoneNumber = "+17343532648";
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue("mySenderID").withDataType("String")); //The sender ID shown on the device.
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue("0.50").withDataType("Number")); //Sets the max price to 0.50 USD.
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional").withDataType("String")); //Sets the type to promotional.
        //<set SMS attributes>
        sendSMSMessage(message, phoneNumber);
    }


    public static void setDefaultSmsAttributes(AmazonSNSClient snsClient) {
        SetSMSAttributesRequest setRequest = new SetSMSAttributesRequest()
                .addAttributesEntry("DefaultSenderID", "mySenderID")
                .addAttributesEntry("MonthlySpendLimit", "1")
                .addAttributesEntry("DeliveryStatusIAMRole",
                        "arn:aws:iam::186371710645:user/vtran20SNSAdmin")
                .addAttributesEntry("DeliveryStatusSuccessSamplingRate", "10")
                .addAttributesEntry("DefaultSMSType", "Transactional")
                .addAttributesEntry("UsageReportS3Bucket", "sns-sms-daily-usage");
        snsClient.setSMSAttributes(setRequest);
        Map<String, String> myAttributes = snsClient.getSMSAttributes(new GetSMSAttributesRequest())
                .getAttributes();
        System.out.println("My SMS attributes:");
        for (String key : myAttributes.keySet()) {
            System.out.println(key + " = " + myAttributes.get(key));
        }
    }
}
