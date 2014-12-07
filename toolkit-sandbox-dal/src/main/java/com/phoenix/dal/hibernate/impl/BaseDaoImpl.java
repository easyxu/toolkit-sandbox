package com.phoenix.dal.hibernate.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.phoenix.dal.DynaClassResultTransformer;
import com.phoenix.dal.hibernate.BaseDao;
import com.phoenix.model.Pager;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T extends Serializable, E extends Serializable> extends HibernateDaoSupport implements
        BaseDao<T, E> {
    private Class<T> t; // model类

    public BaseDaoImpl(Class<T> t, Class<E> e) {
        this.t = t;
    }

    /*
    * 删除单个实体
    *
    */
    public void delete(T model) {
        this.getHibernateTemplate().delete(model);

    }

    @Override
    public void deleteByID(E pk) {
        this.getHibernateTemplate().delete(this.findById(pk));
    }

    /*
    * 查询所有结果
    *
    */
    public List<T> findAll() {

        String hql = "select o from " + t.getName() + " o";
        return this.pageableFindByHQL(null, hql);
    }

    /*
    * 通过hql语句来查询
    *
    */
    public List<?> findByHQL(String hql) {

        return this.pageableFindByHQL(null, hql);
    }

    /*
    * 根据PK查询
    *
    */

    public T findById(E pk) {
        return (T) this.getHibernateTemplate().get(this.t, pk);
    }

    public List<T> findByHQL(String hql, Map<String, Object> queryParams) {
        return pageableFindByHQL(null, hql, queryParams);
    }

    /**
    * HQL分页查询主要方法
    */
    public List<T> pageableFindByHQL(Pager pager, String hql, Map<String, Object> queryParams) {
        return pageableFindByHQL(pager, hql, queryParams, this.t);
    }
    public <ANY> List<ANY> pageableFindByHQL(final Pager pager, final String hql,
			final Map<String, Object> queryParams,final Class<ANY> clazz){

        return hibernateTemplate().executeWithNativeSession(new HibernateCallback<List<ANY>>() {
            @Override
            public List<ANY> doInHibernate(Session session) throws HibernateException, SQLException {
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

                    String countHql = "select count(o) from " + tableName + where;
                    // 得到所有的记录条数目
                    queryCount = session.createQuery(countHql);
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
                Query queryList = session.createQuery(hql);
                setParameter(queryList, queryParams);
//        if (queryParams != null && !queryParams.isEmpty()) {
//            String[] namedParams = queryList.getNamedParameters();
//            for (String key : namedParams) {
//                queryList.setParameter(key, queryParams.get(key));
//            }
//        }
                if (pager != null) {
                    // 设置查询的开始位置
                    queryList.setFirstResult(pager.getStart());
                    queryList.setMaxResults(pager.getPageSize());
                }

                if(clazz != null){
                    queryList.setResultTransformer(new DynaClassResultTransformer<>(clazz, getSessionFactory()));
                }
                // 返回查询的结果
                return  queryList.list();
            }
        });

    }
    public void setParameter(Query query,Map<String,Object> queryParams){
    	if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = query.getNamedParameters();
            for (String key : namedParams) {
            	Object obj = queryParams.get(key);
            	if(obj instanceof Collection<?>){
            		query.setParameterList(key, (Collection<?>)obj);
            	}else if(obj instanceof Object[]){
            		query.setParameterList(key, (Object[])obj);
            	}else{
            		query.setParameter(key, obj);
            	}

            }
        }
    }
    /*
    * 分页查询
    */

    public List<T> pageableFindByHQL(Pager pager, String hql) {
        return pageableFindByHQL(pager, hql, null);
    }

    public void update(T model) {
        this.getHibernateTemplate().update(model);
    }

    public void save(T model) {
        this.getHibernateTemplate().save(model);
    }

    public void saveOrUpdate(T model) {
        this.getHibernateTemplate().saveOrUpdate(model);
    }

    public List findByNativeSql(String sql, Map<String, Object> queryParams) {
        return this.pageableFindByNativeSql(null, sql, queryParams);
    }

    /**
     * 嵌入查询总数
     * @param pager
     * @param sql
     * @param queryParams
     * @param clazz
     * @return
     */
    public List<Object[]> pageableFindByNativeNestedCount(final Pager pager,final  String sql, final Map<String, Object> queryParams,final Class clazz){

        return hibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Object[]>>() {
            @Override
            public List<Object[]> doInHibernate(Session session) throws HibernateException, SQLException {
                // 分页的结果查询
                SQLQuery queryList = session.createSQLQuery(sql);
                if (queryParams != null && !queryParams.isEmpty()) {
                    String[] namedParams = queryList.getNamedParameters();
                    for (String key : namedParams) {
                        queryList.setParameter(key, queryParams.get(key));
                    }
                }
                if (pager != null) {
                    String lowerCaseSql = sql.toLowerCase();
//            int index = lowerCaseSql.indexOf(" where ");
//            index = tablePostIndex(lowerCaseSql, index);
//            int fromIndex = lowerCaseSql.indexOf(" from ");
//
                    String tableName = "("+lowerCaseSql+") o ";
                    String where = " ";
//            if (index != -1) {
//                where = sql.substring(index);
//                tableName = sql.substring(fromIndex + 6, index);
//            } else {
//                tableName = sql.substring(fromIndex + 6);
//            }
                    String countSql = "select count(*) from " + tableName + where;
                    // 得到所有的记录条数目
                    SQLQuery queryCount = session.createSQLQuery(countSql);
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

                if(clazz != null){
//        	 queryList.setResultTransformer(new DynaClassResultTransformer(clazz, this.getSessionFactory()));
                    // 返回查询的结果
                    return queryList.addEntity("o",clazz).list();
                }


                // 返回查询的结果
                return queryList.list();
            }
        });

    }
    /**
    * 无缓存SQL分页查询主要方法
    */
    public  <ANY> List<ANY> pageableFindByNativeSql(final Pager pager,final  String sql,final Map<String, Object> queryParams, final Class<ANY> clazz) {

        return hibernateTemplate().executeWithNativeSession(new HibernateCallback<List<ANY>>() {
            @Override
            public List<ANY> doInHibernate(Session session) throws HibernateException, SQLException {
                // 分页的结果查询
                SQLQuery queryList = session.createSQLQuery(sql);
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
                    SQLQuery queryCount = session.createSQLQuery(countSql);
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

                if(clazz != null){
                    queryList.setResultTransformer(new DynaClassResultTransformer(clazz, getSessionFactory()));
                }
                // 返回查询的结果
                return queryList.list();
            }
        });

    }


    public List pageableFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams) {
        return this.pageableFindByNativeSql(pager, sql, queryParams, null);
    }

    /**
    * 特殊：设置缓存SQL分页查询主要方法
    */
    public List pageCacheFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams, Class<?> clazz) {
        // 分页的结果查询
        SQLQuery queryList = SessionFactoryUtils.getSession(this.sessionFactory(),this.hibernateTemplate().isAllowCreate()).createSQLQuery(sql);
//        queryList.addScalar("TASKNAME", Hibernate.STRING);
//        queryList.addScalar("PROCESSINSTANCEID", Hibernate.LONG);

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

            SQLQuery queryCount = SessionFactoryUtils.getSession(this.sessionFactory(),this.hibernateTemplate().isAllowCreate()).createSQLQuery(countSql);
            pager.setRecordCount(Integer.parseInt(queryCount.uniqueResult().toString()));

            // 设置查询的开始位置
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());

        }
        queryList.setResultTransformer(new DynaClassResultTransformer(clazz, this.getSessionFactory()));

        // 返回查询的结果
        return queryList.list();
    }

    private int tablePostIndex(String lowerCaseSql, int index) {
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

    public Object uniqueResult(String hql, Map<String, Object> queryParams) {
        Query query = SessionFactoryUtils.getSession(this.sessionFactory(),this.hibernateTemplate().isAllowCreate()).createQuery(hql);

        if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = query.getNamedParameters();
            for (String key : namedParams) {
                query.setParameter(key, queryParams.get(key));
            }
        }
        return query.uniqueResult();
    }

    public int executeUpdate(String hql, Map params) {
        // 创建语句
        Query query = SessionFactoryUtils.getSession(this.sessionFactory(), this.hibernateTemplate().isAllowCreate()).createQuery(hql);
        setParameter(query, params);
        // 传入参数
//        if (params != null && !params.isEmpty()) {
//            String[] namedParams = query.getNamedParameters();
//            for (String key : namedParams) {
//                query.setParameter(key, params.get(key));
//            }
//        }
        // 执行更新,返回受影响记录数量
        return query.executeUpdate();
    }

    public List pagerFindByExample(Pager pager, T example) {

        Criteria criteria = SessionFactoryUtils.getSession(this.sessionFactory(),this.hibernateTemplate().isAllowCreate()).createCriteria(t.getClass()).add(Example.create(example));
        if (pager != null) {
            criteria.setFirstResult(pager.getStart());
            criteria.setMaxResults(pager.getPageSize());
        }
        return criteria.list();
    }

    public SessionFactory sessionFactory(){
    	return this.getSessionFactory();
    }
    public Session session(boolean allowCreate) {

		return SessionFactoryUtils.getSession(this.sessionFactory(),allowCreate);
	}
    public Session session() {

		return SessionFactoryUtils.getSession(this.sessionFactory(),this.hibernateTemplate().isAllowCreate());
	}
    public void releaseToSession(Session session) {
        SessionFactoryUtils.releaseSession(session, getSessionFactory());
	}
    public HibernateTemplate hibernateTemplate() {

		return this.getHibernateTemplate();
	}

    /**
	 * 命名查询 --数组 findByNamedQueryAndNamedParam(queryName, paramNames, values)
	 * queryName为查询名 paramNames String[]paramNames = new String[]{"xu","xiang"};
	 * Object[] values = new Object[]{徐,翔};
	 *
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @return
	 */
	public List<?> findByNamedQueryAndNamedParam(String queryName,
			String[] paramNames, Object[] values) {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("参数与之相对应的值，个数不相等");
		}

		return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName,
				paramNames, values);
	}

	/**
	 * 命名查询 queryName为查询名 paramNames 如果查询语句里的参数是 from Persion p where xu=:xu
	 * ,paramNames 就是 xu
	 *
	 * @param queryName
	 * @param paramName
	 * @param value
	 * @return
	 */
	public List<?> findByNamedQueryAndNamedParam(String queryName,
			String paramName, Object value)  {

		return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName,
				paramName, value);
	}

	/**
	 * 命名查询 paramName只有一个时，直接输入值即可
	 *
	 * @param queryName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedQuery(String queryName, Object value){
		return getHibernateTemplate().findByNamedQuery(queryName, value);
	}

	/**
	 * 命名查询 没有参数
	 *
	 * @param queryName
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedQuery(String queryName) {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	/**
	 * 命名查询 valueBean 为Bean对象
	 *
	 * @param queryName
	 * @param valueBean
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedQueryAndValueBean(String queryName, T valueBean){
		return getHibernateTemplate().findByNamedQueryAndValueBean(queryName,
				valueBean);
	}

	/**
	 * queryString是hql语句
	 *
	 * @param queryString
	 * @param paramName
	 * @param value
	 * @return
	 */
	public List<?> findByNamedParam(String queryString, String paramName,
			Object value)  {
		return getHibernateTemplate().findByNamedParam(queryString, paramName,
				value);
	}

	/**
	 * queryString是hql语句
	 *
	 * @param queryString
	 * @param paramNames
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedParam(String queryString, String[] paramNames,
			Object[] values)  {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("参数与之相对应的值，个数不相等");
		}
		return getHibernateTemplate().findByNamedParam(queryString, paramNames,
				values);
	}
	/**
	 *
	 * @param queryString
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedParam(String queryString,Object... values)  {

		return getHibernateTemplate().find(queryString,values);
	}
	/**
	 * queryString是hql语句 valueBean是bean对象
	 *
	 * @param queryString
	 * @param valueBean
	 * @return
	 */
	public List<?> findByValueBean(String queryString, T valueBean) {
		return getHibernateTemplate().findByValueBean(queryString, valueBean);
	}



}
