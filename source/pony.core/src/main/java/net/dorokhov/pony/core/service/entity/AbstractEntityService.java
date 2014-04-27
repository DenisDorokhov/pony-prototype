package net.dorokhov.pony.core.service.entity;

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

/**
 * Abstract entity service.
 *
 * This is a base class for services implementing basic methods for working with abstract entities.
 *
 * @param <EntityType> entity type
 * @param <IdType> entity primary key type
 * @param <RepositoryType> DAO type
 */
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

	/**
	 * Proxies count() call to the DAO.
	 *
	 * @return number of entities
	 */
	@Transactional(readOnly = true)
	public long getCount() {
		return dao.count();
	}

	/**
	 * Proxies findAll(Pageable) call to the DAO.
	 *
	 * @param aPageable pagination option
	 * @return all entities
	 */
	@Transactional(readOnly = true)
	public Page<EntityType> getAll(Pageable aPageable) {
		return dao.findAll(aPageable);
	}

	/**
	 * Proxies findOne(IdType) call to the DAO.
	 *
	 * @param aId entity ID
	 * @return entity with the given ID or null if none found
	 */
	@Transactional(readOnly = true)
	public EntityType getById(IdType aId) {
		return dao.findOne(aId);
	}

	/**
	 * Saves entity in the database.
	 *
	 * 1) Validates entity.
	 * 2) Normalizes entity.
	 * 3) If entity ID is not null, checks if entity exists. If it doesn't, throws an exception.
	 * 4) Initializes entity creation and update dates.
	 * 5) Saves the entity.
	 *
	 * @param aEntity entity to save
	 * @return saved entity
	 * @throws ConstraintViolationException in case entity is not valid
	 */
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

	/**
	 * Deletes entity by ID.
	 *
	 * @param aId entity ID
	 */
	@Transactional
	public void deleteById(IdType aId) {
		dao.delete(aId);
	}

	/**
	 * Validates entity with Bean Validation.
	 *
	 * @param aEntity entity to validate
	 * @throws ConstraintViolationException in case the entity is not valid
	 */
	public void validate(EntityType aEntity) throws ConstraintViolationException {
		ValidationUtility.validate(aEntity, validator);
	}

	/**
	 * Normalizes entity.
	 *
	 * This method is useful when some activities must be performed on entity before storing it in the database (e.g.
	 * trimming string values, nullify empty values, etc.). This method does nothing by default.
	 *
	 * @param aEntity
	 */
	protected void normalize(EntityType aEntity) {
		// Do nothing by default
	}

}
