package sk.stuba.fei.weblab.web.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import sk.stuba.fei.weblab.web.model.User;

public class GenericDataAccessService<T> {

	@PersistenceContext
	private EntityManager em;

	private GenericDataAccessService() {
	}

	private Class<T> type;

	/**
	 * Default constructor
	 * 
	 * @param type
	 *            entity class
	 */
	public GenericDataAccessService(Class<T> type) {
		this.type = type;
	}

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param T
	 *            Object
	 * @return
	 */
	public T create(T t) {
		this.em.persist(t);
		this.em.flush();
		this.em.refresh(t);			

		return t;
	}

	/**
	 * Retrieves an entity instance that was previously persisted to the
	 * database
	 * 
	 * @param T
	 *            Object
	 * @param id
	 * @return
	 */
	public T find(Object id) {
		return this.em.find(this.type, id);
	}

	/**
	 * Removes the record that is associated with the entity instance
	 * 
	 * @param type
	 * @param id
	 */
	public void delete(Object id) {
		Object ref = this.em.getReference(this.type, id);
		this.em.remove(ref);
	}

	/**
	 * Removes the number of entries from a table
	 * 
	 * @param <T>
	 * @param items
	 * @return
	 */
	public boolean deleteItems(T[] items) {
		for (T item : items) {
			if (item instanceof User) {
				User user = (User) item;
				if (user.getId() == 1) {
					continue;
				}
			}
			em.remove(em.merge(item));
		}
		return true;
	}

	/**
	 * Updates the entity instance
	 * 
	 * @param <T>
	 * @param t
	 * @return the object that is updated
	 */
	public T update(T item) {
		if(item instanceof User){
			User user = (User)item;
			if(user.getId() == 1){
				 return item;
			}
		}
		return this.em.merge(item);

	}

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param namedQueryName
	 * @return List
	 */
	public List<T> findWithNamedQuery(String namedQueryName) {
		return this.em.createNamedQuery(namedQueryName, type).getResultList();
	}

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param namedQueryName
	 * @param parameters
	 * @return List
	 */
	public List<T> findWithNamedQuery(String namedQueryName,
			Map<String, Object> parameters) {
		return findWithNamedQuery(namedQueryName, parameters, 0);
	}

	/**
	 * Returns the number of records with result limit
	 * 
	 * @param queryName
	 * @param resultLimit
	 * @return List
	 */
	public List<T> findWithNamedQuery(String queryName, int resultLimit) {
		return this.em.createNamedQuery(queryName, type).setMaxResults(resultLimit)
				.getResultList();
	}

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param <T>
	 * @param sql
	 * @param type
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByNativeQuery(String sql) {
		return this.em.createNativeQuery(sql, type).getResultList();
	}

	/**
	 * Returns the number of total records
	 * 
	 * @param namedQueryName
	 * @return int
	 */
	public int countTotalRecord(String namedQueryName) {
		Query query = em.createNamedQuery(namedQueryName);
		Number result = (Number) query.getSingleResult();
		return result.intValue();
	}

	/**
	 * Returns the number of records that meet the criteria with parameter map
	 * and result limit
	 * 
	 * @param namedQueryName
	 * @param parameters
	 * @param resultLimit
	 * @return List
	 */
	public List<T> findWithNamedQuery(String namedQueryName,
			Map<String, Object> parameters, int resultLimit) {
		TypedQuery<T> query = this.em.createNamedQuery(namedQueryName, type);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	/**
	 * Returns the number of records that will be used with lazy loading /
	 * pagination
	 * 
	 * @param namedQueryName
	 * @param start
	 * @param end
	 * @return List
	 */
	public List<T> findWithNamedQuery(String namedQueryName, int start, int end) {
		TypedQuery<T> query = this.em.createNamedQuery(namedQueryName, type);
		query.setMaxResults(end - start);
		query.setFirstResult(start);
		return query.getResultList();
	}

	public List<T> findAll() {
		CriteriaQuery<T> cq = this.em.getCriteriaBuilder().createQuery(type);
		cq.select(cq.from(type));	
		return this.em.createQuery(cq).getResultList();
	}
}
