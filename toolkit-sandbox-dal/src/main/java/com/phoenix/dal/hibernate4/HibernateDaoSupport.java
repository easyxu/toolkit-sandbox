package com.phoenix.dal.hibernate4;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.phoenix.dal.BaseHibernateDao;
import com.phoenix.model.Pager;

public class HibernateDaoSupport<T extends Serializable, ID extends Serializable>  extends BaseHibernateDao<T,ID>  {


	public HibernateDaoSupport(Class<T> t, Class<ID> e) {
		super(t, e);
		this.t = t;
	}


	@Override
	public void deleteByID(ID pk) {
		delete(findById(pk));
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
		return pageableFindByHQL(pager, hql, null);
	}

	@Override
	public List<T> findByHQL(String hql, Map<String, Object> queryParams) {
		return pageableFindByHQL(null, hql, queryParams);
	}



	@Override
	public List<T> pageableFindByHQL(Pager pager, String hql,
			Map<String, Object> queryParams) {
		return pageableFindByHQL(pager, hql, queryParams, null);
	}



	@Override
	public List<?> findByNativeSql(String sql, Map<String, Object> queryParams) {
		return this.pageableFindByNativeSql(null, sql, queryParams);
	}

	@Override
	public List<?> pageableFindByNativeSql(Pager pager, String sql,
			Map<String, Object> queryParams) {
		return pageableFindByNativeSql(pager, sql, queryParams,null);
	}



	@Override
	public List<?> pageCacheFindByNativeSql(Pager pager, String sql,
			Map<String, Object> queryParams, Class<?> clazz) {
		return null;
	}












}
