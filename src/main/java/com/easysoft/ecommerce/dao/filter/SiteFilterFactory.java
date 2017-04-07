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

public class SiteFilterFactory {
    private Long siteId;

    /**
     * injected parameter
     */
    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        if (siteId != null) key.addParameter(siteId);
        return key;
    }

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery( new Term("site.id", siteId.toString() ) );
        return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }
}
