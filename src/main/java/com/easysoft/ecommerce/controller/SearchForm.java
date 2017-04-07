package com.easysoft.ecommerce.controller;

import java.util.List;
import java.util.Map;

import com.easysoft.ecommerce.model.Product;

public class SearchForm {

    private String keyword;
    private String sortField;
    private List<Long> categoryId;
    private List<String> refinement;
    private boolean reverse;
    private int page;
    private int maxResult;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public int getStartPos() {
        if (page < 1) return 0;
        return (page - 1) * getMaxResult();
    }

    public int getMaxResult() {
        if (maxResult <= 0) return Constants.MAX_ITEM_PER_PAGE;
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Long> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Long> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getRefinement() {
        return refinement;
    }

    public void setRefinement(List<String> refinement) {
        this.refinement = refinement;
    }
}
