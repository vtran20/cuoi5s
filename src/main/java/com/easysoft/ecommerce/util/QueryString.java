package com.easysoft.ecommerce.util;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * User: Vu Tran
 * Date: Sep 7, 2010
 * Time: 11:44:11 AM
 */
public class QueryString implements Cloneable {

    private Map<String, ArrayList> params = new LinkedHashMap<String, ArrayList>();

    public QueryString(String queryString) {
        if (!StringUtils.isEmpty(queryString)) {
            String temp[] = queryString.split("&");
            for (String str: temp) {
                String []param = str.split("=");
                if (param.length == 2) {
                    encode(param[0], param[1], false);
                }
            }
        }
    }

    public QueryString(String queryString, boolean removeTheSameParam) {
        if (!StringUtils.isEmpty(queryString)) {
            String temp[] = queryString.split("&");
            for (String str: temp) {
                String []param = str.split("=");
                if (param.length == 2) {
                    encode(param[0], param[1], removeTheSameParam);
                }
            }
        }
    }

    public QueryString(String name, String value) {
        encode(name, value, true);
    }

    public QueryString addAndKeepOrgParam(String name, String value) {
        encode(name, value, false);
        return this;
    }

    public QueryString add(String name, String value) {
        encode(name, value, true);
        return this;
    }

    public QueryString exclude(String name) {
        params.remove(name);
        return this;
    }

    public QueryString exclude(String name, String value) {
        ArrayList<String> values = params.get(name);
        for (String v: values) {
            if (v.equalsIgnoreCase(value)) {
                values.remove(v);
                break;
            }
        }
        if (values.isEmpty()) params.remove(name);
        return this;
    }

    private void encode(String name, String value, boolean removeTheSameParam) {
//        try {
//            name = URLEncoder.encode(name, "UTF-8");
//            value = URLEncoder.encode(value, "UTF-8");
//            name = StringEscapeUtils.escapeHtml(name);
//            value = StringEscapeUtils.escapeHtml(value);

//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException("Broken VM does not support UTF-8");
//        }
            ArrayList values = params.get(name);
            if (values == null) {
                values = new ArrayList<String>();
                params.put(name, values);
            }
            if (removeTheSameParam) {
                values.clear();
            }
            if (!StringUtils.isEmpty(value) && !values.contains(value)) {
                values.add(value);
            }
    }

    public String toString() {
        StringBuffer query = new StringBuffer();
//        params = sortByComparator(params);
        for (String key : params.keySet()) {
            List<String> list = params.get(key);
            for (String item: list) {
                if (!StringUtils.isEmpty(query.toString())) {
                    query.append("&");
                }
                query.append(key).append("=").append(item);
            }
        }
        return query.toString();
    }

    public String getParameter (String name) {
        if (params.get(name) != null && params.get(name).size() > 0) {
            return (String) params.get(name).get(0);
        } else {
            return "";
        }
    }

    private Map<String, ArrayList> getParams() {
        return params;
    }

    public Object clone () {
        try {
            QueryString v = (QueryString) super.clone();
            v.params = new LinkedHashMap<String, ArrayList>();
            for (String key: this.params.keySet()) {
                v.params.put(key, (ArrayList) this.params.get(key).clone());
            }
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    private Map sortByComparator(Map unsortMap) {

         List list = new LinkedList(unsortMap.keySet());

         //sort list based on comparator
         Collections.sort(list, new Comparator() {
              public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
              }
     });

         //put sorted list into map again
     Map <String, ArrayList>sortedMap = new LinkedHashMap<String, ArrayList>();
     for (Iterator it = list.iterator(); it.hasNext();) {
          String key = (String) it.next();
          sortedMap.put(key, (ArrayList) unsortMap.get(key));
     }
     return sortedMap;
    }

}
