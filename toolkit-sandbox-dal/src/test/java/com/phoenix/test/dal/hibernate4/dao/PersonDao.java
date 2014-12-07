package com.phoenix.test.dal.hibernate4.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phoenix.test.dal.hibernate4.objects.Person;

@Component("personDao")
public class PersonDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void save(Person model){
		sessionFactory.getCurrentSession().save(model);
	}

}
