package com.easysoft.ecommerce.model.lucene;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.bridge.StringBridge;
import org.hibernate.search.bridge.TwoWayStringBridge;

/**
 * To support search VNese, we will add one more word if that word has sign in it.
 * <p/>
 * User: Vu Tran
 * Date: Jan 3, 2011
 * Time: 1:34:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnsignBridge implements TwoWayStringBridge {

    /**
     * If a word has sign. We store both sign and unsign
     * If a word don't have sign. Return original word.
     */
    @Override
    public String objectToString(Object object) {

        if (StringUtils.isEmpty((String) object)) return "";

        if (object instanceof String) {
            String str = (String) object;
            String word[] = str.split(" ");
            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < word.length; i++) {
                String unSign = convertToUnsign(word[i]);
                if (!(word[i]).equalsIgnoreCase(unSign)) {
                    temp.append(" ").append(unSign);
                } else {
                    //do nothing
                }
            }
            if (temp.toString().isEmpty()) {
                return str;
            } else {
                return str + " " + "#@" + temp.toString().trim() + "@#";
            }
        } else {
            return object.toString();
        }
    }

    @Override
    public Object stringToObject(String stringValue) {
        if (StringUtils.isEmpty(stringValue)) {
            return null;
        } else {
            return stringValue.replaceAll(" #@(.)*@#", "");
        }
    }



    /**
     * Convert VNese to Unsign VNese text
     *
     * @param input: unicode text
     *               Convert VNese to unsign VNese.
     * @return unsign VNese.
     */
    private String convertToUnsign(String input) {
        if (StringUtils.isEmpty(input)) return "";

        char[] arr = input.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            switch (arr[i]) {
                case '\u00e0':
                case '\u00e1':
                case '\u1ea1':
                case '\u1ea3':
                case '\u00e3':
                case '\u00e2':
                case '\u1ea7':
                case '\u1ea5':
                case '\u1ead':
                case '\u1ea9':
                case '\u1eab':
                case '\u0103':
                case '\u1eb1':
                case '\u1eaf':
                case '\u1eb7':
                case '\u1eb3':
                case '\u1eb5':
                case '\u00c0':
                case '\u00c1':
                case '\u1ea0':
                case '\u1ea2':
                case '\u00c3':
                case '\u00c2':
                case '\u1ea6':
                case '\u1ea4':
                case '\u1eac':
                case '\u1ea8':
                case '\u1eaa':
                case '\u0102':
                case '\u1eb0':
                case '\u1eae':
                case '\u1eb6':
                case '\u1eb2':
                case '\u1eb4':
                    input = input.replace(arr[i], 'a');
                    break;

                case '\u00e8':
                case '\u00e9':
                case '\u1eb9':
                case '\u1ebb':
                case '\u1ebd':
                case '\u00ea':
                case '\u1ec1':
                case '\u1ebf':
                case '\u1ec7':
                case '\u1ec3':
                case '\u1ec5':
                case '\u00c8':
                case '\u00c9':
                case '\u1eb8':
                case '\u1eba':
                case '\u1ebc':
                case '\u00ca':
                case '\u1ec0':
                case '\u1ebe':
                case '\u1ec6':
                case '\u1ec2':
                case '\u1ec4':
                    input = input.replace(arr[i], 'e');
                    break;

                case '\u00ec':
                case '\u00ed':
                case '\u1ecb':
                case '\u1ec9':
                case '\u0129':
                case '\u00cc':
                case '\u00cd':
                case '\u1eca':
                case '\u1ec8':
                case '\u0128':
                    input = input.replace(arr[i], 'i');
                    break;

                case '\u00f2':
                case '\u00f3':
                case '\u1ecd':
                case '\u1ecf':
                case '\u00f5':
                case '\u00f4':
                case '\u1ed3':
                case '\u1ed1':
                case '\u1ed9':
                case '\u1ed5':
                case '\u1ed7':
                case '\u01a1':
                case '\u1edd':
                case '\u1edb':
                case '\u1ee3':
                case '\u1edf':
                case '\u1ee1':
                case '\u00d2':
                case '\u00d3':
                case '\u1ecc':
                case '\u1ece':
                case '\u00d5':
                case '\u00d4':
                case '\u1ed2':
                case '\u1ed0':
                case '\u1ed8':
                case '\u1ed4':
                case '\u1ed6':
                case '\u01a0':
                case '\u1edc':
                case '\u1eda':
                case '\u1ee2':
                case '\u1ede':
                case '\u1ee0':
                    input = input.replace(arr[i], 'o');
                    break;

                case '\u00f9':
                case '\u00fa':
                case '\u1ee5':
                case '\u1ee7':
                case '\u0169':
                case '\u01b0':
                case '\u1eeb':
                case '\u1ee9':
                case '\u1ef1':
                case '\u1eed':
                case '\u1eef':
                case '\u00d9':
                case '\u00da':
                case '\u1ee4':
                case '\u1ee6':
                case '\u0168':
                case '\u01af':
                case '\u1eea':
                case '\u1ee8':
                case '\u1ef0':
                case '\u1eec':
                case '\u1eee':
                    input = input.replace(arr[i], 'u');
                    break;

                case '\u1ef3':
                case '\u00fd':
                case '\u1ef5':
                case '\u1ef7':
                case '\u1ef9':
                case '\u1ef2':
                case '\u00dd':
                case '\u1ef4':
                case '\u1ef6':
                case '\u1ef8':
                    input = input.replace(arr[i], 'y');
                    break;

                case '\u0111':
                case '\u0110':
                    input = input.replace(arr[i], 'd');
                    break;

//                case ' ': input = input.replace(arr[i], '-'); break;

                default:
                    break;
            }

        }
        return input;
    }

}
