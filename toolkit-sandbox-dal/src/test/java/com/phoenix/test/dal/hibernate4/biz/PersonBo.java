package com.phoenix.test.dal.hibernate4.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phoenix.dal.h4.HibernateDaoSupport;
import com.phoenix.model.Pager;
import com.phoenix.test.dal.hibernate4.objects.Person;
import com.phoenix.test.dal.hibernate4.objects.Vo;


@Service("personBo")
public class PersonBo {

	@Autowired
	HibernateDaoSupport<Person, Integer> personDao;
	
	public void savePerson(Person person){
		personDao.save(person);
	}
	
	public Person findPerson(Integer id){
		return personDao.findById(id);
	}

	public List<Person> findPerson(Pager pager){
		return personDao.pageableFindByHQL(pager, "select o from "+Person.class.getName() + " o join o.card");
	}
	
	public List<Vo> findNativePerson(Pager pager){
		return  personDao.pageableFindByNativeSql(pager, "select person.name name,card.name cardName,card.amount from person person join creditCard card on person.id=card.personid", null, Vo.class);
	}
	public List<Map> findNativePersonMap(Pager pager){
		return  personDao.pageableFindBySql(pager, "select person.name name,card.name cardName,card.amount from person person join creditCard card on person.id=card.personid", null);
	}

	public List<List> findNativePersonList(Pager pager){
		return  personDao.pageableFindBySqlList(pager, "select person.name name,card.name cardName,card.amount from person person join creditCard card on person.id=card.personid", null);
	}
}
