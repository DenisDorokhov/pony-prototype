package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.SearchIndexService;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class SearchIndexServiceImpl implements SearchIndexService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager aEntityManager) {
		entityManager = aEntityManager;
	}

	@Override
	public void createIndex() {

		Session session = (Session)entityManager.getDelegate();

		FullTextSession fullTextSession = Search.getFullTextSession(session);

		try {
			fullTextSession.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void clearIndex() {

		Session session = (Session)entityManager.getDelegate();

		FullTextSession fullTextSession = Search.getFullTextSession(session);

		fullTextSession.purgeAll(Song.class);
		fullTextSession.getSearchFactory().optimize(Song.class);
	}
}
