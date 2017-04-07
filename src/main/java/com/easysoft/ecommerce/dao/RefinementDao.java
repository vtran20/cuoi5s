package com.easysoft.ecommerce.dao;

import com.easysoft.ecommerce.model.CmsArea;
import com.easysoft.ecommerce.model.Refinement;
import com.easysoft.ecommerce.model.Site;

import java.util.List;
import java.util.Map;


public interface RefinementDao extends GenericDao<Refinement, Long> {

    Map getAllRefinements(Site site, Long categoryId);
    Map getRefinements(Site site);
    Map getRefinements(Long categoryId);
}