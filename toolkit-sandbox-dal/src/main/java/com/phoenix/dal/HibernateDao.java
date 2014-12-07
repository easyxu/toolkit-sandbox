package com.phoenix.dal;

import java.io.Serializable;
import java.util.List;

import com.phoenix.model.Pager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface HibernateDao<T extends Serializable,ID extends Serializable> extends GenericDao<T,ID>  {



    /**
     *  通过对象模版构造查询语句来查询
     * @param pager
     * @param example
     * @return List
     */
    List<?> pagerFindByExample(Pager pager, T example);

    /**
     * 命名查询 valueBean 为Bean对象
     *
     * @param queryName
     * @param valueBean
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedQueryAndValueBean(String queryName, T valueBean);

    /**
     * queryString是hql语句 valueBean是bean对象
     *
     * @param queryString
     * @param valueBean
     * @return
     */
    public List<?> findByValueBean(String queryString, T valueBean);


    /**
     * 命名查询 paramName只有一个时，直接输入值即可
     *
     * @param queryName
     * @param value
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedQuery(String queryName, Object value);


    /**
     * 获取hibernate sessionFactory
     * @return SessionFactory
     */
    public SessionFactory sessionFactory();

    /**
     * 获取hibernate session(是否允许创建)
     * @param allowCreate
     * @return Session
     */
    public Session session(boolean allowCreate);

    /**
     * 获取hibernate session
     * @return Session
     */
    public Session session();

    /**
     * 释放session
     * @param session
     */
    public void releaseToSession(Session session);
}
