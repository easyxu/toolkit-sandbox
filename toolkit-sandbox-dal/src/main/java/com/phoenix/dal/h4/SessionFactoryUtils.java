package com.phoenix.dal.h4;

import com.phoenix.common.reflect.ClassUtils;
import com.phoenix.common.reflect.ReflectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.Wrapped;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * Created by xux on 14-12-7.
 */
public abstract class SessionFactoryUtils {

    public static final int CONNECTION_SYNCHRONIZATION_ORDER = 1000;

    public static final int SESSION_SYNCHRONIZATION_ORDER =CONNECTION_SYNCHRONIZATION_ORDER - 100;

    static final Log logger = LogFactory.getLog(SessionFactoryUtils.class);


    private static final Method getConnectionProviderMethod =
            ClassUtils.getMethodIfAvailable(SessionFactoryImplementor.class, "getConnectionProvider");



    public static DataSource getDataSource(SessionFactory sessionFactory) {
        if (getConnectionProviderMethod != null && sessionFactory instanceof SessionFactoryImplementor) {
            Wrapped cp = (Wrapped) ReflectionUtils.invokeMethod(getConnectionProviderMethod, sessionFactory);
            if (cp != null) {
                return cp.unwrap(DataSource.class);
            }
        }
        return null;
    }


    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            }
            catch (HibernateException ex) {
                logger.debug("Could not close Hibernate Session", ex);
            }
            catch (Throwable ex) {
                logger.debug("Unexpected exception on closing Hibernate Session", ex);
            }
        }
    }
}
