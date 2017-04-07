package com.easysoft.ecommerce.service.impl;

import com.easysoft.ecommerce.service.IndexService;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private SessionFactory sessionFactory;
    //TODO: Build index strategy http://docs.jboss.org/hibernate/search/3.2/reference/en-US/html/manual-index-changes.html#search-batchindex
	@Override
	public void index() throws Exception {
        FullTextSession fullTextSession = Search.getFullTextSession(this.sessionFactory.getCurrentSession());
        fullTextSession.createIndexer().startAndWait();//while this statement is running, cannot query index
//        fullTextSession.createIndexer().
	}

}
