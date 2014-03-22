package net.dorokhov.pony.core.dao.impl;

import net.dorokhov.pony.core.dao.SongDaoCustom;
import net.dorokhov.pony.core.domain.Song;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;
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
	public List<Song> search(String aText, Pageable aPageable) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Song.class).get();

		BooleanQuery luceneQuery = new BooleanQuery();

		for (String word : aText.trim().split("\\s+")) {

			Query nameQuery = queryBuilder.keyword().wildcard().onField("file.name").matching(word + "*").createQuery();
			//Query albumQuery = queryBuilder.keyword().wildcard().onField("file.album").matching(word + "*").createQuery();
			//Query artistQuery = queryBuilder.keyword().wildcard().onField("file.artist").matching(word + "*").createQuery();

			luceneQuery.add(nameQuery, BooleanClause.Occur.MUST);
			//luceneQuery.add(albumQuery, BooleanClause.Occur.SHOULD);
			//luceneQuery.add(artistQuery, BooleanClause.Occur.SHOULD);
		}

		FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Song.class);

		jpaQuery.setFirstResult(aPageable.getOffset());
		jpaQuery.setMaxResults(aPageable.getPageSize());

		return (List<Song>)jpaQuery.getResultList();
	}
}
