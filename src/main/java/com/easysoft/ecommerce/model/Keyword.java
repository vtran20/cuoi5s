package com.easysoft.ecommerce.model;

import org.hibernate.annotations.Index;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Indexed
@Entity
@Table (name = "keyword")
public class Keyword extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String keyword;
    private String groupBy;
    private Site site;
    private int count;
    private int sequence;

    public Keyword () {
        super();
    }
    
    @Column(name="keyword", nullable = false, length = 50)
    @Index(name = "keywordIndex")
    @Field(index= org.hibernate.search.annotations.Index.UN_TOKENIZED, store= Store.YES)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Index(name = "groupIndex")
    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    @Field(index= org.hibernate.search.annotations.Index.UN_TOKENIZED, store= Store.YES)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
