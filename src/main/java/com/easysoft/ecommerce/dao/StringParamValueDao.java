package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.StringParam;
import com.easysoft.ecommerce.model.StringParamValue;

import java.util.List;

public interface StringParamValueDao extends GenericDao<StringParamValue, Long> {
    List<StringParamValue> getStringParamValues (String stringParam, String language);
    StringParamValue getStringParamValue (String stringParam, String language, String stringParamKey);
}