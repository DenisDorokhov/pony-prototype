package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.AbstractEntity;
import net.dorokhov.pony.core.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Date;

public abstract class AbstractEntityService<EntityType extends AbstractEntity<IdType>, IdType extends Serializable, RepositoryType extends PagingAndSortingRepository<EntityType, IdType>> {

	protected RepositoryType dao;

	protected Validator validator;

	@Autowired
	public void setDao(RepositoryType aDao) {
		dao = aDao;
	}

	@Autowired
	public void setValidator(Validator aValidator) {
		validator = aValidator;
	}

	@Transactional(readOnly = true)
	public long getCount() {
		return dao.count();
	}

	@Transactional(readOnly = true)
	public Page<EntityType> getAll(Pageable aPageable) {
		return dao.findAll(aPageable);
	}

	@Transactional(readOnly = true)
	public EntityType getById(IdType aId) {
		return dao.findOne(aId);
	}

	@Transactional
	public EntityType save(EntityType aEntity) throws ConstraintViolationException {

		validate(aEntity);
		normalize(aEntity);

		Date currentDate = new Date();

		if (aEntity.getId() != null) {

			EntityType storedEntity = dao.findOne(aEntity.getId());

			if (storedEntity == null) {
				throw new RuntimeException("Entity does not exist.");
			}

			aEntity.setCreationDate(storedEntity.getCreationDate());

		} else {
			aEntity.setCreationDate(currentDate);
		}

		aEntity.setUpdateDate(currentDate);

		return dao.save(aEntity);
	}

	@Transactional
	public void deleteById(IdType aId) {
		dao.delete(aId);
	}

	public void validate(EntityType aEntity) throws ConstraintViolationException {
		ValidationUtility.validate(aEntity, validator);
	}

	protected void normalize(EntityType aEntity) {
		// Do nothing by default
	}

}
