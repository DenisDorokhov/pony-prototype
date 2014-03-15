package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.AbstractEntityIdentified;
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

public abstract class AbstractEntityService<EntityType extends AbstractEntityIdentified<IdType>, IdType extends Serializable, RepositoryType extends PagingAndSortingRepository<EntityType, IdType>> {

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
	public Long getCount() {
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
	@SuppressWarnings("unckecked")
	public EntityType save(EntityType aEntity) throws ConstraintViolationException {

		validate(aEntity);
		normalize(aEntity);

		Date currentDate = new Date();

		if (aEntity.getId() != null) {

			EntityType storedProject = dao.findOne(aEntity.getId());

			if (storedProject == null) {
				throw new RuntimeException("Entity does not exist.");
			}

			aEntity.setCreationDate(storedProject.getCreationDate());

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
