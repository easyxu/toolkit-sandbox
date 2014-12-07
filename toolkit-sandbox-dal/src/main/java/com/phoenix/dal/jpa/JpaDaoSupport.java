package com.phoenix.dal.jpa;

import com.phoenix.dal.DynaClassResultTransformer;
import com.phoenix.dal.GenericDao;
import com.phoenix.model.Pager;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xux on 14-12-7.
 */
public class JpaDaoSupport<T extends Serializable, ID extends Serializable> implements GenericDao<T,ID>{

    @PersistenceContext
    protected EntityManager em;
    private Class<T> entity; // model类

    public JpaDaoSupport(Class<T> entity) {
        this.entity = entity;
    }

    public JpaDaoSupport(){
//        Type t = getClass().getGenericSuperclass();
//        ParameterizedType pt = (ParameterizedType) t;
//        entity = (Class) pt.getActualTypeArguments()[0];
    }
    @Override
    public int executeUpdate(String hql, Map<String, Object> params) {
        Query query = em.createQuery(hql);
        setParameter(query,params);
        return query.executeUpdate();
    }

    @Override
    public void saveOrUpdate(T model) {

    }

    @Override
    public void save(T model) {
        this.em.persist(model);
    }

    @Override
    public void update(T model) {
        this.em.merge(model);
    }

    @Override
    public void delete(T model) {
        this.em.remove(model);
//        model = this.em.merge(model);
//        this.em.remove(model);
    }

    @Override
    public void deleteByID(final ID pk) {
        this.em.remove(this.em.getReference(entity,pk));

    }
    @Override
    public T findById(ID pk) {
        return this.em.find(entity,pk);
    }

    @Override
    public List<T> findAll() {
        final StringBuffer queryString = new StringBuffer(
                "SELECT o from ");
        queryString.append(entity.getSimpleName()).append(" o ");
        final Query query = this.em.createQuery(queryString.toString());

        return query.getResultList();
    }

    @Override
    public List<?> findByHQL(String jpql) {
        final Query query = this.em.createQuery(jpql);
        return query.getResultList();
    }

    @Override
    public List<T> pageableFindByHQL(Pager pager, String jpql) {
        return pageableFindByHQL(pager,jpql,null);
    }

    @Override
    public List<T> findByHQL(String jpql, Map<String, Object> queryParams) {
        final Query query = this.em.createQuery(jpql);
        setParameter(query,queryParams);
        return query.getResultList();
    }

    @Override
    public List<T> pageableFindByHQL(Pager pager, String hql, Map<String, Object> queryParams) {
        return this.pageableFindByHQL(pager,hql,queryParams,entity);
    }

    /**
     * 根据jpql分页返回Map集合
     * @param pager
     * @param jpql
     * @param queryParams
     * @return
     */
    public  List<Map> pageableFindByJpqlMap(final Pager pager, final String jpql,final  Map<String, Object> queryParams){
        return this.pageFindByJpql(pager,jpql,queryParams,Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * 根据jpql分页返回list集合
     * @param pager
     * @param jpql
     * @param queryParams
     * @return
     */
    public  List<List> pageableFindByJpqlList(final Pager pager, final String jpql,final  Map<String, Object> queryParams){
        return this.pageFindByJpql(pager,jpql,queryParams,Transformers.TO_LIST);
    }
    @Override
    public <ANY> List<ANY> pageableFindByHQL(Pager pager, String jpql, Map<String, Object> queryParams, Class<ANY> clazz) {
        return pageFindByJpql(pager,jpql,queryParams,new com.phoenix.dal.jpa.DynaClassResultTransformer(clazz));
    }

    @Override
    public List<?> findByNativeSql(String sql, Map<String, Object> queryParams) {
        Query query = this.em.createNativeQuery(sql);
        setParameter(query,queryParams);
        return query.getResultList();
    }

    @Override
    public List<Object[]> pageableFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams) {
        return pageFindBySql(pager,sql,queryParams,null);
    }

    @Override
    public <ANY> List<ANY> pageableFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams, Class<ANY> entityClass) {

        return pageFindBySql(pager,sql,queryParams,new com.phoenix.dal.jpa.DynaClassResultTransformer(entityClass));
    }

    /**
     * 根据sql 分页返回Map
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<Map> pageableFindBySqlMap(final Pager pager, final String sql,final  Map<String, Object> queryParams){
        return pageFindBySql(pager,sql,queryParams, Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * 根据sql 分页返回List
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    public  List<List> pageableFindBySqlList(final Pager pager, final String sql,final  Map<String, Object> queryParams){
        return pageFindBySql(pager,sql,queryParams, Transformers.TO_LIST);
    }

    @Override
    public List<?> pageCacheFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams, Class<?> clazz) {
        return null;
    }

    /**
     * 根据JPQL 分页查询数据
     * @param pager
     * @param jpql
     * @param queryParams
     * @param transformer
     * @return
     */
    protected List pageFindByJpql(Pager pager,String jpql,Map<String, Object> queryParams,ResultTransformer transformer){
        Query queryCount = null;
        if ((pager != null) && ((!pager.isLexUse()) || (pager.isOnlyCount()))) {
            int index = jpql.toLowerCase().indexOf(" where ");
            int fromIndex = jpql.toLowerCase().indexOf("from ") + 5;
            String where = null,tableName;
            if (index != -1) {
                where = jpql.substring(index);
                tableName = jpql.substring(fromIndex, index);
            } else {
                tableName = jpql.substring(fromIndex);
            }

            String countJpql = "select count(o) from " + tableName + where;
            queryCount = this.em.createQuery(countJpql);
            if (queryParams != null && !queryParams.isEmpty()) {
                Set<Parameter<?>> namedParams = queryCount.getParameters();
                for (Parameter<?> key : namedParams) {
                    Object obj = queryParams.get(key);
                    queryCount.setParameter(key.getName(), obj);

                }
            }
            //获取总记录数
            pager.setRecordCount(((Long)queryCount.getSingleResult()).intValue());
            if (pager.isOnlyCount()) {
                return null;//对lex DaoModel特殊处理
            }
        }

        //获取数据
        Query queryList = this.em.createQuery(jpql);
        if (pager != null) {
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());
        }
        if(transformer != null){
            applyTransformer(queryList,transformer);
        }
        return queryList.getResultList();
    }

    /**
     * 根据SQL 分页查询数据
     * @param pager
     * @param sql
     * @param queryParams
     * @param transformer
     * @return
     */
    protected List pageFindBySql(Pager pager,String sql,Map<String, Object> queryParams,ResultTransformer transformer){
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
            Query queryCount = this.em.createNativeQuery(countSql);
            if (queryParams != null && !queryParams.isEmpty()) {
                Set<Parameter<?>> namedParams = queryCount.getParameters();
                for (Parameter<?> key : namedParams) {
                    queryCount.setParameter(key.getName(), queryParams.get(key));
                }
            }
            pager.setRecordCount(((BigInteger)queryCount.getSingleResult()).intValue());
            if (pager.isOnlyCount()) {
                return null;//对lex DaoModel特殊处理
            }


        }

        // 分页的结果查询
        Query queryList = this.em.createNativeQuery(sql);
        if (queryParams != null && !queryParams.isEmpty()) {
            Set<Parameter<?>> namedParams = queryList.getParameters();
            for (Parameter<?> key : namedParams) {
                queryList.setParameter(key.getName(), queryParams.get(key));
            }
        }
        if(pager != null){
            // 设置查询的开始位置
            queryList.setFirstResult(pager.getStart());
            queryList.setMaxResults(pager.getPageSize());
        }
        if(transformer != null){
            applyTransformer(queryList,transformer);
        }

        // 返回查询的结果
        return queryList.getResultList();
    }


    @Override
    public Object uniqueResult(String jpql, Map<String, Object> queryParams) {
        Query query = this.em.createQuery(jpql);
        setParameter(query,queryParams);
        return query.getSingleResult();
    }



    @Override
    public List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) {
        Query query = this.em.createNamedQuery(queryName);
        for(int i = 1 ; i <= paramNames.length;i++){
            query.setParameter(i,values[i]);
        }
        return query.getResultList();
    }

    @Override
    public List<?> findByNamedQueryAndNamedParam(String queryName, String paramName, Object value) {
        Query query = this.em.createNamedQuery(queryName).setParameter(paramName,value);
        return query.getResultList();
    }



    @Override
    public List<?> findByNamedQuery(String queryName) {
        return this.em.createNamedQuery(queryName).getResultList();
    }



    @Override
    public List<T> findByNamedParam(String queryString, String paramName, Object value) {
        TypedQuery<T> query = this.em.createQuery(queryString,entity);
        query.setParameter(paramName,value);
        return query.getResultList();
    }

    @Override
    public List<T> findByNamedParam(String queryString, String[] paramNames, Object... values) {
        TypedQuery<T> query = this.em.createQuery(queryString,entity);
        for(int i = 1; i <= paramNames.length;i++){
            query.setParameter(i,values[i]);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findByNamedParam(String queryString, Object... values) {
        TypedQuery<T> query = this.em.createQuery(queryString,entity);
        Set<Parameter<?>> parameters = query.getParameters();
        int i = 1;
        for(Parameter<?> parameter : parameters){
            query.setParameter(i,values[i]);
            i++;
        }

        return query.getResultList();
    }

    public EntityManager entityManager(){
        return this.em;
    }


    protected void setParameter(Query query,Map<String,Object> queryParams){
        if (queryParams != null && !queryParams.isEmpty()) {
            Set<Parameter<?>> namedParams = query.getParameters();
            for (Parameter<?> key : namedParams) {
                Object obj = queryParams.get(key.getName());
                query.setParameter(key.getName(), obj);

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

    public void applyTransformer(Query jpaQuery, ResultTransformer transformer) {
        if(transformer == null){
            return ;
        }

        try{
            Method method = org.hibernate.jpa.internal.QueryImpl.class.getMethod("getHibernateQuery",null);
            org.hibernate.Query hibernateQuery = (org.hibernate.Query)Proxy.getInvocationHandler(jpaQuery).invoke(jpaQuery,method,null);
            hibernateQuery.setResultTransformer(transformer);
        }catch (ClassCastException e){
            throw new IllegalArgumentException(
                    "You must use Hibernate as the JPAProvider");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }



}
