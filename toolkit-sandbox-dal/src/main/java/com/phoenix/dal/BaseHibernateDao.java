package com.phoenix.dal;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.phoenix.model.Pager;

public abstract class BaseHibernateDao<T extends Serializable, ID extends Serializable> implements HibernateDao<T, ID>{

	private static Logger LOG = LoggerFactory.getLogger(BaseHibernateDao.class);
	
	private boolean cacheQueries = false;

	private String queryCacheRegion;

	private int fetchSize = 0;

	private int maxResults = 0;
	
	protected Class<T> t; // model类
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	
	public BaseHibernateDao(Class<T> t, Class<ID> e) {
		this.t = t;
	}
	
	/******************************************************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public T findById(ID pk) {
		return (T) session().get(this.t, pk);
	}
	@Override
	public int executeUpdate(String hql, Map<String,Object> params) {
		Query query = session().createQuery(hql);
		applyParameter(query, params);
		return query.executeUpdate();
	}

	@Override
	public void saveOrUpdate(T model) {
		session().saveOrUpdate(model);
	}

	@Override
	public void save(T model) {
		session().save(model);
	}

	@Override
	public void update(T model) {
		session().update(model);
	}

	@Override
	public void delete(T model) {
		session().delete(model);
	}
	@Override
	public Object uniqueResult(String hql, Map<String, Object> queryParams) {
		Query query = session().createQuery(hql);
		if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = query.getNamedParameters();
            for (String key : namedParams) {
                query.setParameter(key, queryParams.get(key));
            }
        }
        return query.uniqueResult();
	}
	
	
	/*****************************Pager*********************************************/
	
	/**
	 * 
	 * @param pager			分页
	 * @param hql			语句
	 * @param queryParams	查询参数
	 * @param clazz			动态转换类型
	 * @return
	 */
	@Override
	public <ANY> List<ANY> pageableFindByHQL(Pager pager, String hql,
			Map<String, Object> queryParams,Class<ANY> clazz){
		//查询总数
        Query queryCount = null;
        if ((pager != null) && ((!pager.isLexUse()) || (pager.isOnlyCount()))) {
            int index = hql.toLowerCase().indexOf(" where ");
            int fromIndex = hql.toLowerCase().indexOf("from ") + 5;
            String where = " ";
            String tableName = "";
            if (index != -1) {
                where = hql.substring(index);
                tableName = hql.substring(fromIndex, index);
            } else {
                tableName = hql.substring(fromIndex);
            }

            String countSql = "select count(o) from " + tableName + where;
            // 得到所有的记录条数目
            queryCount = session().createQuery(countSql);
            if (queryParams != null && !queryParams.isEmpty()) {
                String[] namedParams = queryCount.getNamedParameters();
                for (String key : namedParams) {
                	Object obj = queryParams.get(key);
                	if(obj instanceof Collection<?>){
                		queryCount.setParameterList(key, (Collection<?>)obj);
                	}else if(obj instanceof Object[]){
                		queryCount.setParameterList(key, (Object[])obj);
                	}else{
                		queryCount.setParameter(key, obj);
                	}
                    
                }
            }
            pager.setRecordCount(Integer.parseInt(queryCount.uniqueResult().toString()));
            if (pager.isOnlyCount()) {
                return null;//对lex DaoModel特殊处理
            }
        }

        // 分页的结果查询
        Query queryList = session().createQuery(hql);
//        setParameter(queryList, queryParams);
        if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = queryList.getNamedParameters();
            for (String key : namedParams) {
                queryList.setParameter(key, queryParams.get(key));
            }
        }
        if (pager != null) {
            // 设置查询的开始位置
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());
        }
        if(clazz != null){
        	queryList.setResultTransformer(new DynaClassResultTransformer<>(clazz, this.sessionFactory));
        }
        // 返回查询的结果
        return queryList.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <ANY> List<ANY> pageableFindByNativeSql(Pager pager, String sql,
			Map<String, Object> queryParams, Class<ANY> entityClass) {
		 // 分页的结果查询
        SQLQuery queryList = session().createSQLQuery(sql);
        if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = queryList.getNamedParameters();
            for (String key : namedParams) {
                queryList.setParameter(key, queryParams.get(key));
            }
        }
        if (pager != null) {
            String lowerCaseSql = sql.toLowerCase();
            int index = lowerCaseSql.indexOf(" where ");
            index = tablePostIndex(lowerCaseSql, index);
            int fromIndex = lowerCaseSql.indexOf(" from ");

            String tableName = "";
            String where = " ";
            if (index != -1) {
                where = sql.substring(index);
                tableName = sql.substring(fromIndex + 6, index);
            } else {
                tableName = sql.substring(fromIndex + 6);
            }
            String countSql = "select count(*) from " + tableName + where;
            // 得到所有的记录条数目
            SQLQuery queryCount = session().createSQLQuery(countSql);
            if (queryParams != null && !queryParams.isEmpty()) {
                String[] namedParams = queryCount.getNamedParameters();
                for (String key : namedParams) {
                	queryCount.setParameter(key, queryParams.get(key));
                }
            }
            pager.setRecordCount(Integer.parseInt(queryCount.uniqueResult().toString()));
            if (pager.isOnlyCount()) {
                return null;//对lex DaoModel特殊处理
            }
            // 设置查询的开始位置
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());

        }
        
        if(entityClass != null){
        	 queryList.setResultTransformer(new DynaClassResultTransformer(entityClass, this.sessionFactory));
        	 // 返回查询的结果
//             return queryList.addEntity("o",entityClass).list();
        }
       

        // 返回查询的结果
        return queryList.list();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQueryAndNamedParam(String queryName,
			String[] paramNames, Object[] values) {
		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		Query queryObject = session().getNamedQuery(queryName);
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	@Override
	public List<T> findByNamedQueryAndNamedParam(String queryName,
			String paramName, Object value) {
		return findByNamedQueryAndNamedParam(queryName, new String[] {paramName}, new Object[] {value});
	}

	@Override
	public List<T> findByNamedQuery(String queryName, Object value) {
		
		return findByNamedQuery(queryName, new Object[] {value});
	}

	@Override
	public List<T> findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, (Object[]) null);
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> findByNamedQuery(final String queryName, final Object... values){
		Query queryObject = session().getNamedQuery(queryName);
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQueryAndValueBean(String queryName, T valueBean) {
		Query queryObject = session().getNamedQuery(queryName);
		prepareQuery(queryObject);
		queryObject.setProperties(valueBean);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByValueBean(String queryString, T valueBean) {
		Query queryObject = session().createQuery(queryString);
		prepareQuery(queryObject);
		queryObject.setProperties(valueBean);
		return queryObject.list();
	}
	
	
	@Override
	public List<T> findByNamedParam(String queryString, String paramName,
			Object value) {
		return findByNamedParam(queryString, new String[] {paramName}, new Object[] {value});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedParam(String queryString, String[] paramNames,
			Object[] values) {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		Query queryObject = session().createQuery(queryString);
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedParam(String queryString,Object... values){
		Query queryObject = session().createQuery(queryString);
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}
	
	//-------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	//-------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public List<T> pagerFindByExample(Pager pager, T entityName) {
		Criteria criteria = session().createCriteria(entityName.getClass());
		criteria.add(Example.create(entityName));
		prepareCriteria(criteria);
        if (pager != null) {
            criteria.setFirstResult(pager.getStart());
            criteria.setMaxResults(pager.getPageSize());
        }
        return criteria.list();
	}
	
	public List<T> findByCriteria(DetachedCriteria criteria) throws DataAccessException {
		return findByCriteria(criteria, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	List<T> findByCriteria(DetachedCriteria criteria,int firstResult, int maxResults){
		Criteria executableCriteria = criteria.getExecutableCriteria(session());
		prepareCriteria(executableCriteria);
		if (firstResult >= 0) {
			executableCriteria.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			executableCriteria.setMaxResults(maxResults);
		}
		return executableCriteria.list();
	}
	
	
	
	
	protected int tablePostIndex(String lowerCaseSql, int index) {
		if (index == -1) {
			index = lowerCaseSql.indexOf(" group ");
		}
		if (index == -1) {
			index = lowerCaseSql.indexOf(" having ");
		}
		if (index == -1) {
			index = lowerCaseSql.indexOf(" order ");
		}
		return index;
	}

	protected void prepareCriteria(Criteria criteria) {
		if (isCacheQueries()) {
			criteria.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				criteria.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			criteria.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			criteria.setMaxResults(getMaxResults());
		}
	}
	
	protected void prepareQuery(Query queryObject) {
		if (isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			queryObject.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			queryObject.setMaxResults(getMaxResults());
		}
	}
	protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value)
			throws HibernateException {
		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection<?>) value);
		}
		else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else {
			queryObject.setParameter(paramName, value);
		}
	}
	protected void applyParameter(Query query, Map<String, Object> queryParams) {
		if (queryParams != null && !queryParams.isEmpty()) {
			String[] namedParams = query.getNamedParameters();
			for (String key : namedParams) {
				Object obj = queryParams.get(key);
				if (obj instanceof Collection<?>) {
					query.setParameterList(key, (Collection<?>) obj);
				} else if (obj instanceof Object[]) {
					query.setParameterList(key, (Object[]) obj);
				} else {
					query.setParameter(key, obj);
				}

			}
		}
	}

	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	
	
	@Override
	public SessionFactory sessionFactory() {
		return sessionFactory;
	}

	@Override
	public Session session(boolean allowCreate) {

		return allowCreate ? sessionFactory.openSession() : session();
	}

	@Override
	public Session session() {
		try{
			return sessionFactory.getCurrentSession();
		}catch(HibernateException ex){
			LOG.debug("There is no transaction, so opening a new Hibernate Session");
			return sessionFactory.openSession();
		}
		
	}
	



	@Override
	public void releaseToSession(Session session) {

		if (session != null) {
			LOG.debug("Closing Hibernate Session");
			try {
				session.close();
			}catch (HibernateException ex) {
				LOG.debug("Could not close Hibernate Session", ex);
			}
			catch (Throwable ex) {
				LOG.debug("Unexpected exception on closing Hibernate Session", ex);
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/******************************/
	public boolean isCacheQueries() {
		return cacheQueries;
	}

	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	public String getQueryCacheRegion() {
		return queryCacheRegion;
	}

	public void setQueryCacheRegion(String queryCacheRegion) {
		this.queryCacheRegion = queryCacheRegion;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
