package com.phoenix.dal.h4;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by xux on 14-12-7.
 */
public interface HibernateCallback<T> {
    T doInHibernate(Session session) throws HibernateException;
}
