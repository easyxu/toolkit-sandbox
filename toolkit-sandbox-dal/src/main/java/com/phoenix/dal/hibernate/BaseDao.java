package com.phoenix.dal.hibernate;

import java.io.Serializable;

public interface BaseDao<T extends Serializable,ID extends Serializable> extends com.phoenix.dal.HibernateDao<T, ID> {


}
