package net.dorokhov.pony.core.dao.impl;

import java.io.InputStream;
import java.sql.DatabaseMetaData;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import net.dorokhov.pony.core.dao.InstallationDao;
import net.dorokhov.pony.core.utility.SqlSplitter;
import net.dorokhov.pony.core.domain.Installation;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Installation DAO - JPA implementation.
 */
@Repository
public class InstallationDaoImpl implements InstallationDao {

	/**
	 * Java package to read the scripts from.
	 */
	public final static String SCRIPT_PACKAGE = "/net/dorokhov/pony/core/dao";

	private EntityManager entityManager;

	private DataSource dataSource;

	@PersistenceContext
	public void setEntityManager(EntityManager aEntityManager) {
		entityManager = aEntityManager;
	}

	@Autowired
	public void setDataSource(DataSource aDataSource) {
		dataSource = aDataSource;
	}

	/**
	 * Finds database installation.
	 *
	 * It tries to make a select query for Installation entity ignoring any errors except NonUniqueResultException.
	 *
	 * @return database installation or null if the database is not installed
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Installation findInstallation() {

		Installation installation = null;

		try {
			installation = entityManager.createQuery("SELECT i FROM Installation i", Installation.class).getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			// Do nothing here to prevent errors because of not created tables.
		}

		return installation;
	}

	/**
	 * Installs the database.
	 *
	 * 1) Finds the installation script "install.sql" in SCRIPT_PACKAGE/DBMS_PRODUCT_NAME.
	 * 2) Splits the script into SQL statements.
	 * 3) Runs SQL statements one by one.
	 */
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void install() {

		try {

			SqlSplitter splitter = new SqlSplitter();

			for (String statement : splitter.splitScript(fetchScriptContents("install.sql"))) {
				entityManager.createNativeQuery(statement).executeUpdate();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Uninstalls the database.
	 *
	 * 1) Finds the uninstallation script "uninstall.sql" in SCRIPT_PACKAGE/DBMS_PRODUCT_NAME.
	 * 2) Splits the script into SQL statements.
	 * 3) Runs SQL statements one by one.
	 */
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void uninstall() {

		try {

			SqlSplitter splitter = new SqlSplitter();

			for (String statement : splitter.splitScript(fetchScriptContents("uninstall.sql"))) {
				entityManager.createNativeQuery(statement).executeUpdate();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String fetchScriptContents(String aName) throws Exception {

		InputStream inputStream = this.getClass().getResourceAsStream(buildScriptPath(aName));

		if (inputStream == null) {
			throw new Exception("Script not found.");
		}

		return IOUtils.toString(inputStream, "UTF-8");
	}

	private String buildScriptPath(String aName) throws Exception {

		DatabaseMetaData metaData = dataSource.getConnection().getMetaData();

		StringBuilder builder = new StringBuilder(SCRIPT_PACKAGE);

		builder.append("/").append(metaData.getDatabaseProductName().toLowerCase()).append("/").append(aName);

		return builder.toString();
	}
}
