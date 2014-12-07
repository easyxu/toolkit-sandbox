package com.phoenix.dal.h4;


import com.google.common.base.Preconditions;
import com.phoenix.dal.DynaClassResultTransformer;
import com.phoenix.dal.hibernate.BaseDao;
import com.phoenix.model.Pager;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by xux on 14-12-7.
 */
public class HibernateDaoSupport <T extends Serializable, ID extends Serializable> extends org.springframework.orm.hibernate4.support.HibernateDaoSupport implements
        BaseDao<T, ID> {

    protected Class<T> t; // model类

    public HibernateDaoSupport(Class<T> t, Class<ID> e) {
        this.t = t;
    }

    @Override
    public int executeUpdate(final String hql,final Map<String, Object> params) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {

            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                setParameter(query, params);
                return query.executeUpdate();
            }
        });
    }

    @Override
    public void saveOrUpdate(T model) {
        currentSession().saveOrUpdate(model);
    }

    @Override
    public void save(T model) {
        currentSession().save(model);
    }

    @Override
    public void update(T model) {
        currentSession().update(model);
    }

    @Override
    public void delete(T model) {
        currentSession().delete(model);
    }

    @Override
    public T findById(ID pk) {
        return (T) currentSession().get(t,pk);
    }

    @Override
    public List<T> findAll() {
        String hql = "select o from " + t.getName() + " o";
        return this.pageableFindByHQL(null, hql);
    }

    @Override
    public List<T> findByHQL(String hql) {
        return this.pageableFindByHQL(null, hql);
    }

    @Override
    public List<T> pageableFindByHQL(Pager pager, String hql) {
        return pageableFindByHQL(pager,hql,null);
    }

    @Override
    public List<T> findByHQL(String hql, Map<String, Object> queryParams) {
        return pageableFindByHQL(null,hql,queryParams);
    }

    @Override
    public List<T> pageableFindByHQL(final Pager pager, final String hql,final  Map<String, Object> queryParams) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                return pageFindByHql(pager, hql, session, queryParams, null);
            }
        });
    }


    /**
     * 分页返回Map集合
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<Map> pageableFindByHql(final Pager pager, final String sql,final  Map<String, Object> queryParams){

        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map>>() {
            @Override
            public List<Map> doInHibernate(Session session) throws HibernateException {
                return pageFindByHql(pager, sql, session, queryParams, Transformers.ALIAS_TO_ENTITY_MAP);
            }
        });
    }

    /**
     * 分页返回List集合
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<List> pageableFindByHqlList(final Pager pager, final String sql,final  Map<String, Object> queryParams){

        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<List>>() {
            @Override
            public List<List> doInHibernate(Session session) throws HibernateException {
                return pageFindByHql(pager, sql, session, queryParams, Transformers.TO_LIST);
            }
        });
    };

    @Override
    public <ANY> List<ANY> pageableFindByHQL(final Pager pager,final String hql, final Map<String, Object> queryParams, final Class<ANY> clazz) {
        return  getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<ANY>>() {
            @Override
            public List<ANY> doInHibernate(Session session) throws HibernateException {
                return pageFindBySql(pager,hql,session,queryParams,new DynaClassResultTransformer(clazz,session.getSessionFactory()));
//                return pageFindByHql(pager, hql, session, queryParams, Transformers.aliasToBean(clazz));
            }
        });
    }

    @Override
    public List<?> findByNativeSql(String sql, Map<String, Object> queryParams) {
        return pageableFindByNativeSql(null,sql,queryParams);
    }

    @Override
    public List<?> pageableFindByNativeSql(final Pager pager, final String sql, final Map<String, Object> queryParams) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<?>>() {
            @Override
            public List<?> doInHibernate(Session session) throws HibernateException {
                return pageFindBySql(pager, sql, session, queryParams, null);
            }
        });
    }


    /**
     * 分页返回Map集合
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<Map> pageableFindBySql(final Pager pager, final String sql,final  Map<String, Object> queryParams){

        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map>>() {
            @Override
            public List<Map> doInHibernate(Session session) throws HibernateException {
                return pageFindBySql(pager, sql, session, queryParams, Transformers.ALIAS_TO_ENTITY_MAP);
            }
        });
    }

    /**
     * 分页返回List集合
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<List> pageableFindBySqlList(final Pager pager, final String sql,final  Map<String, Object> queryParams){

        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<List>>() {
            @Override
            public List<List> doInHibernate(Session session) throws HibernateException {
                return pageFindBySql(pager, sql, session, queryParams, Transformers.TO_LIST);
            }
        });
    };



    @Override
    public <ANY> List<ANY> pageableFindByNativeSql(final Pager pager, final String sql,final  Map<String, Object> queryParams,final Class<ANY> clazz) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<ANY>>() {
            @Override
            public List<ANY> doInHibernate(Session session) throws HibernateException {
                return pageFindBySql(pager,sql,session,queryParams,new DynaClassResultTransformer(clazz,session.getSessionFactory()));
//                return pageFindBySql(pager, sql, session, queryParams, Transformers.aliasToBean(clazz));
            }
        });
    }

    /**
     * 根据HQL分页查询
     * @param pager
     * @param hql
     * @param session
     * @param queryParams
     * @param transformer
     * @return
     */
    private List pageFindByHql(Pager pager, String hql,Session session, Map<String, Object> queryParams,ResultTransformer transformer){
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
            queryCount = session.createQuery(countSql);
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
                return null;//fly DaoModel特殊处理
            }
        }

        // 分页的结果查询
        Query queryList = session.createQuery(hql);
        setParameter(queryList, queryParams);
        if (pager != null) {
            // 设置查询的开始位置
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());
        }

        if(transformer != null){
            queryList.setResultTransformer(transformer);
        }
        // 返回查询的结果
        return  queryList.list();
    }

    /**
     * 根据SQL分页查询
     * @param pager
     * @param sql
     * @param session
     * @param queryParams
     * @param transformer
     * @return
     */
    private List pageFindBySql(Pager pager, String sql,Session session, Map<String, Object> queryParams,ResultTransformer transformer){
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
        if(transformer != null){
            queryList.setResultTransformer(transformer);
        }
        // 返回查询的结果
        return queryList.list();
    }
    @Override
    public List<?> pageCacheFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams, Class<?> clazz) {
        return null;
    }

    @Override
    public Object uniqueResult(String hql, Map<String, Object> queryParams) {
        Query query = currentSession().createQuery(hql);
        //传入参数
        if (queryParams != null && !queryParams.isEmpty()) {
            String[] namedParams = query.getNamedParameters();
            for (String key : namedParams) {
                query.setParameter(key, queryParams.get(key));
            }
        }
        return query.uniqueResult();
    }

    @Override
    public List<?> pagerFindByExample(Pager pager, T example) {
        Criteria criteria = currentSession().createCriteria(t.getClass()).add(Example.create(example));
        if (pager != null) {
            criteria.setFirstResult(pager.getStart());
            criteria.setMaxResults(pager.getPageSize());
        }
        return criteria.list();
    }

    @Override
    public SessionFactory sessionFactory() {
        return getSessionFactory();
    }

    @Override
    public Session session(boolean allowCreate) {
        if(allowCreate){
            return getSessionFactory().openSession();
        }
        return currentSession();
    }

    @Override
    public Session session() {
        return currentSession();
    }

    @Override
    public void releaseToSession(Session session) {
        SessionFactoryUtils.closeSession(session);
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
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedQuery(String queryName, Object value){
        return getHibernateTemplate().findByNamedQuery(queryName, value);
    }

    /**
     * 命名查询 没有参数
     *
     * @param queryName
     * @return
     * @throws org.springframework.dao.DataAccessException
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
     * @throws org.springframework.dao.DataAccessException
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
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedParam(String queryString, String[] paramNames,
                                    Object[] values)  {
        Preconditions.checkArgument(paramNames.length != values.length,"参数与之相对应的值，个数不相等");
        return getHibernateTemplate().findByNamedParam(queryString, paramNames,
                values);
    }
    /**
     *
     * @param queryString
     * @return
     * @throws org.springframework.dao.DataAccessException
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
}
