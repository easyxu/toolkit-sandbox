package com.phoenix.dal;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;

import com.phoenix.dal.hibernate.Converter;
import com.phoenix.dal.hibernate.ConverterException;
import com.phoenix.dal.hibernate.Converters;

@SuppressWarnings("unchecked")
public class DynaClassResultTransformer<T> implements ResultTransformer {

    /**
     *
     */
    private static final long serialVersionUID = 6137782890170818570L;

    private Class<?> tClazz;
    private SessionFactory sessionFactory;
    public DynaClassResultTransformer(Class<?> clazz,SessionFactory sessionFactory){
        tClazz=clazz;
        this.sessionFactory=sessionFactory;
    }
    List<T> list=new ArrayList<T>();

    public List<?> transformList(List list) {
        return list;
    }

    public Object transformTuple(Object[] objs, String[] names) {

        Method[] ms= tClazz.getMethods();

        Object obj=null;

            try {
                obj =tClazz.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }

        Map<String,Method> map=new HashMap<String, Method>();
        if(ms.length>0)
        {
            for(Method m:ms){
                if(m.getName().indexOf("set")!=-1)
                map.put(m.getName().substring(3).toUpperCase(), m);
            }
        }

        Object o=null;
        Converter<?> convert=null;
        Class<?> type=null;
        for(int i=0;i<names.length;i++){
            if(map.containsKey(names[i])){
                try {
                    Method method=map.get(names[i]);

                    o=objs[i];

                    type= method.getParameterTypes()[0];
                    if(type!=null){
                        convert= Converters.getConverter(type);
                    }

                    if(convert!=null)
                    {
                        o=convert.cast(o);
                    }
                    else
                    {
                        if(sessionFactory.getClassMetadata(type)!=null){
                            o= Converters.getConverter(sessionFactory.getClassMetadata(type).getIdentifierType().getReturnedClass()).cast(o);
                            o=sessionFactory.getCurrentSession().load(type, (Serializable) o);
                        }
                    }

                    method.invoke(obj, o);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ConverterException e) {
                    e.printStackTrace();
                }
            }

        }
        list.add((T) obj);
        return obj;
    }
}

