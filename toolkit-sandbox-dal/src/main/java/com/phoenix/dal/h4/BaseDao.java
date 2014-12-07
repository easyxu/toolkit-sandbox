package com.phoenix.dal.h4;

import com.google.common.base.Preconditions;
import com.phoenix.dal.exceptions.DataAccessException;
import com.phoenix.exceptions.*;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 * Created by xux on 14-12-7.
 */
public class BaseDao {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected boolean exposeNativeSession = false;
    protected boolean cacheQueries = false;
    protected String queryCacheRegion;
    protected String[] filterNames;
    private int fetchSize = 0;
    private int maxResults = 0;
    @Resource
    private SessionFactory sessionFactory;


    public boolean isCacheQueries() {
        return cacheQueries;
    }

    public String getQueryCacheRegion() {
        return queryCacheRegion;
    }

    public boolean isExposeNativeSession() {
        return exposeNativeSession;
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

    public <T> T execute(HibernateCallback<T> action) throws DataAccessException {
        return doExecute(action, false);
    }


    public <T> T executeWithNativeSession(HibernateCallback<T> action) {
        try {
            return doExecute(action, true);
        } catch (DataAccessException e) {
            throw com.phoenix.exceptions.RuntimeException.wrap(e,ValidationCode.DATA_ACCESS);
        }
    }


    protected <T> T doExecute(HibernateCallback<T> action, boolean enforceNativeSession) throws DataAccessException {
        Preconditions.checkNotNull(action,"Callback object must not be null");
        Session session = null;
        boolean isNew = false;
        try {
            session = sessionFactory.getCurrentSession();
        }
        catch (HibernateException ex) {
            logger.debug("Could not retrieve pre-bound Hibernate session", ex);
        }
        if (session == null) {
            session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.MANUAL);
            isNew = true;
        }

        try {
            enableFilters(session);
            Session sessionToExpose =
                    (enforceNativeSession || isExposeNativeSession() ? session : createSessionProxy(session));
            return action.doInHibernate(sessionToExpose);
        }
        catch (HibernateException ex) {
            throw new DataAccessException(ex);
        }
        finally {
            if (isNew) {
                SessionFactoryUtils.closeSession(session);
            }
            else {
                disableFilters(session);
            }
        }
    }


    protected Session createSessionProxy(Session session) {
        return (Session) Proxy.newProxyInstance(
                session.getClass().getClassLoader(), new Class<?>[]{Session.class},
                new CloseSuppressingInvocationHandler(session));
    }


    protected void enableFilters(Session session) {
        String[] filterNames = getFilterNames();
        if (filterNames != null) {
            for (String filterName : filterNames) {
                session.enableFilter(filterName);
            }
        }
    }

    public void setFilterNames(String... filterNames) {
        this.filterNames = filterNames;
    }
    public String[] getFilterNames() {
        return this.filterNames;
    }
    /**
     * Disable the specified filters on the given Session.
     * @param session the current Hibernate Session
     * @see #setFilterNames
     * @see org.hibernate.Session#disableFilter(String)
     */
    protected void disableFilters(Session session) {
        String[] filterNames = getFilterNames();
        if (filterNames != null) {
            for (String filterName : filterNames) {
                session.disableFilter(filterName);
            }
        }
    }




    private class CloseSuppressingInvocationHandler implements InvocationHandler {

        private final Session target;

        public CloseSuppressingInvocationHandler(Session target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Invocation on Session interface coming in...

            if (method.getName().equals("equals")) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0]);
            }
            else if (method.getName().equals("hashCode")) {
                // Use hashCode of Session proxy.
                return System.identityHashCode(proxy);
            }
            else if (method.getName().equals("close")) {
                // Handle close method: suppress, not valid.
                return null;
            }

            // Invoke method on target Session.
            try {
                Object retVal = method.invoke(this.target, args);

                // If return value is a Query or Criteria, apply transaction timeout.
                // Applies to createQuery, getNamedQuery, createCriteria.
                if (retVal instanceof Query) {
                    prepareQuery(((Query) retVal));
                }
                if (retVal instanceof Criteria) {
                    prepareCriteria(((Criteria) retVal));
                }

                return retVal;
            }
            catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    protected void prepareQuery(Query query) {
        if (isCacheQueries()) {
            query.setCacheable(true);
            if (getQueryCacheRegion() != null) {
                query.setCacheRegion(getQueryCacheRegion());
            }
        }
        if (getFetchSize() > 0) {
            query.setFetchSize(getFetchSize());
        }
        if (getMaxResults() > 0) {
            query.setMaxResults(getMaxResults());
        }
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
}
