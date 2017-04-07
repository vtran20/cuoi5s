package com.easysoft.ecommerce.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="site_question_answer")
public class SiteQuestionAnswer extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    private String question;
    private String answer;
    private String active;
    private float sequence;
    private Site site;

    @Column(name = "question", length = 500)
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Column(name = "answer", length = 5000)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public float getSequence() {
        return sequence;
    }

    public void setSequence(float sequence) {
        this.sequence = sequence;
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.NO)
    @Column(name = "active", nullable = true, length = 1)
    @org.hibernate.annotations.Index(name = "activeIndex")
    public String getActive() {
        return convertActiveFlag(active);
    }

    public void setActive(String active) {
        this.active = convertActiveFlag(active);
    }

    @ManyToOne
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
