package com.easysoft.ecommerce.dao.filter;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.CachingWrapperFilter;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

public class CategoryFilterFactory {
    private Long categoryId;


    /**
     * injected parameter
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        if (categoryId > 0) key.addParameter(categoryId);
        return key;
    }

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery( new Term("categories.id", categoryId.toString() ) );
        return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }
}
