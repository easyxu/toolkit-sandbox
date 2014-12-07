package com.phoenix.test.dal.hibernate4;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phoenix.model.Pager;
import com.phoenix.test.dal.hibernate4.biz.PersonBo;
import com.phoenix.test.dal.hibernate4.objects.CreditCard;
import com.phoenix.test.dal.hibernate4.objects.Person;
import com.phoenix.test.dal.hibernate4.objects.Vo;


@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:h4/dataSource.xml","classpath:h4/dao.xml"})
public class PersonTestCase {

	@Autowired
	PersonBo personBo;
	
	@Test
	public void save(){
		Person person = new Person();
		person.setName("xux");
		CreditCard card1 = new CreditCard();
		card1.setName("招商银行");
		card1.setAmount(new BigDecimal("13000"));
		card1.setPerson(person);
		person.getCard().add(card1);
		
		personBo.savePerson(person);
	}

	@Test
	public void findById(){
		System.out.println(personBo.findPerson(1));
	}
	
	@Test
	public void insertBath(){
		for(int i = 0; i<1000;i++){
			Person person = new Person();
			person.setName("xux"+i);
			CreditCard card1 = new CreditCard();
			card1.setName("招商银行"+i);
			card1.setAmount(new BigDecimal(i));
			card1.setPerson(person);
			person.getCard().add(card1);
			personBo.savePerson(person);
		}
	}
	
	@Test
	public void pagePerson(){
		Pager p = new Pager();
		p.setStart(0);
		p.setPageSize(15);
		
		List<Person> list = personBo.findPerson(p);
		for(Person p1 : list){
			System.out.println("**************************");
			System.out.println(p1);
//			for(CreditCard card:p1.getCard()){
//				System.out.println(card);
//			}
		}
		System.out.println(p.getRecordCount());
	}
	@Test
	public void pageNativePerson(){
		Pager p = new Pager();
		p.setStart(0);
		p.setPageSize(15);
		
		List<Vo> list = personBo.findNativePerson(p);
		for(Vo p1 : list){
			System.out.println(p1);
		}
		System.out.println(p.getRecordCount());
	}
	@Test
	public void pageNativePersonMap(){
		Pager p = new Pager();
		p.setStart(0);
		p.setPageSize(15);

		List<Map> list = personBo.findNativePersonMap(p);
		for(Map p1 : list){
			System.out.println(p1);
		}
		System.out.println(p.getRecordCount());
	}

	@Test
	public void pageNativePersonList(){
		Pager p = new Pager();
		p.setStart(0);
		p.setPageSize(15);

		List<List> list = personBo.findNativePersonList(p);
		for(List p1 : list){
			for(Object v : p1){
				System.out.println(v);
			}
		}
		System.out.println(p.getRecordCount());
	}
	@Test
	public void say(){
		
	}
}
