package com.phoenix.test.jpa.biz;


import com.phoenix.dal.GenericDao;
import com.phoenix.model.Pager;
import com.phoenix.test.dal.hibernate4.objects.Vo;
import com.phoenix.test.jpa.objects.Person;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


/**
 * Created by xux on 14-12-7.
 */
@Service
public class PersonBo {

    @Resource
    private GenericDao<Person,Integer> personDao;



    @Transactional
    public void savePerson(Person person){
        personDao.save(person);
    }


    public List findNativePerson(Pager pager){
        return  personDao.pageableFindByNativeSql(pager,
                "select person.name name,card.name cardName,card.amount from person person join creditCard card on person.id=card.personid", null,null);
    }
}
