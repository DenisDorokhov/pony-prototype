package net.dorokhov.pony.core.dao.impl;

import net.dorokhov.pony.core.dao.SongDaoCustom;
import net.dorokhov.pony.core.domain.Song;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class SongDaoImpl implements SongDaoCustom {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager aEntityManager) {
		entityManager = aEntityManager;
	}

	@Override
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Song> search(String aText, int aMaxResults) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Song.class).get();

		Query luceneQuery = queryBuilder.keyword().
				onFields("file.name", "file.album", "file.artist").matching(aText).createQuery();

		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Song.class);

		jpaQuery.setFirstResult(0);
		jpaQuery.setMaxResults(aMaxResults);

		return (List<Song>)jpaQuery.getResultList();
	}
}
