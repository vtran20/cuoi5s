package com.easysoft.ecommerce.dao.filter;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.util.DocIdBitSet;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.*;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * User: Vu Tran
 *
 * Date: Sep 15, 2010
 * Time: 10:48:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceRangeFilter {

    private String fieldName;
    private String lowerTerm, upperTerm;
    private Boolean includeLower, includeUpper;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLowerTerm() {
        return lowerTerm;
    }

    public void setLowerTerm(String lowerTerm) {
        this.lowerTerm = lowerTerm;
    }

    public String getUpperTerm() {
        return upperTerm;
    }

    public void setUpperTerm(String upperTerm) {
        this.upperTerm = upperTerm;
    }

    public Boolean isIncludeLower() {
        return includeLower;
    }

    public void setIncludeLower(Boolean includeLower) {
        this.includeLower = includeLower;
    }

    public Boolean isIncludeUpper() {
        return includeUpper;
    }

    public void setIncludeUpper(Boolean includeUpper) {
        this.includeUpper = includeUpper;
    }

    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        if (fieldName != null)
        key.addParameter(fieldName);
        if (lowerTerm != null)
        key.addParameter(lowerTerm);
        if (upperTerm != null)
        key.addParameter(upperTerm);
        key.addParameter(includeLower);
        key.addParameter(includeUpper);
        return key;
    }

    @Factory
    public Filter getFilter() {
        return new CachingWrapperFilter(new TermRangeFilter(fieldName,lowerTerm, upperTerm, includeLower, includeUpper));
    }

}
