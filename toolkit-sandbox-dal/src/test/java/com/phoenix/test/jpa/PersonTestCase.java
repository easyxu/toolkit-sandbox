package com.phoenix.test.jpa;


import com.phoenix.model.Pager;
import com.phoenix.test.dal.hibernate4.objects.Vo;
import com.phoenix.test.jpa.biz.PersonBo;
import com.phoenix.test.jpa.objects.CreditCard;
import com.phoenix.test.jpa.objects.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xux on 14-12-7.
 */
@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jpa/dataSource.xml","classpath:jpa/dao.xml"})
public class PersonTestCase {

    @Resource
    PersonBo personBo;
    @Test
    public void save(){
        Person person = new Person();
        person.setName("妈妈");
        CreditCard card1 = new CreditCard();
        card1.setName("农业银行");
        card1.setAmount(new BigDecimal("40000"));
        card1.setPerson(person);
        person.getCard().add(card1);

        personBo.savePerson(person);
    }

    @Test
    public void pageNativePerson(){
        Pager p = new Pager();
        p.setStart(0);
        p.setPageSize(15);

        List<Object[]> list = personBo.findNativePerson(p);
        for(Object[] p1 : list){
            for(Object o : p1){
                System.out.println(o);
            }
        }
        System.out.println(p.getRecordCount());
    }
}
