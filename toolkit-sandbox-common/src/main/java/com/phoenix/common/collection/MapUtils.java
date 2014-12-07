package com.phoenix.common.collection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;

// @ThreadSafe
@SuppressWarnings("unchecked")
public class MapUtils extends org.apache.commons.collections.MapUtils
{

    /**
     * Convenience method for CollectionUtil#mapWithKeysAndValues(Class, Iterator,
     * Iterator); keys and values can be null or empty.
     */
   
	public static Map mapWithKeysAndValues(Class mapClass, Object[] keys, Object[] values)
    {
        Collection keyCollection = (keys != null ? Arrays.asList(keys) : Collections.EMPTY_LIST);
        Collection valuesCollection = (values != null ? Arrays.asList(values) : Collections.EMPTY_LIST);
        return mapWithKeysAndValues(mapClass, keyCollection.iterator(), valuesCollection.iterator());
    }

    /**
     * Convenience method for CollectionUtil#mapWithKeysAndValues(Class, Iterator,
     * Iterator); keys and values can be null or empty.
     */
    public static Map mapWithKeysAndValues(Class mapClass, Collection keys, Collection values)
    {
        keys = (keys != null ? keys : Collections.EMPTY_LIST);
        values = (values != null ? values : Collections.EMPTY_LIST);
        return mapWithKeysAndValues(mapClass, keys.iterator(), values.iterator());
    }

    /**
     * Create & populate a Map of arbitrary class. Populating stops when either the
     * keys or values iterator is null or exhausted.
     * 
     * @param mapClass the Class of the Map to instantiate
     * @param keys iterator for Objects ued as keys
     * @param values iterator for Objects used as values
     * @return the instantiated Map
     */
    public static Map mapWithKeysAndValues(Class mapClass, Iterator keys, Iterator values)
    {
        Map m = null;

        if (mapClass == null)
        {
            throw new IllegalArgumentException("Map class must not be null!");
        }

        try
        {
            m = (Map) mapClass.newInstance();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        if (keys != null && values != null)
        {
            while (keys.hasNext() && values.hasNext())
            {
                m.put(keys.next(), values.next());
            }
        }

        return m;
    }

    /**
     * Creates a String representation of the given Map, with optional newlines
     * between elements.
     * 
     * @param props the map to format
     * @param newline indicates whether elements are to be split across lines
     * @return the formatted String
     */
    public static String toString(Map map, boolean newline)
    {
        if (map == null || map.isEmpty())
        {
            return "{}";
        }
        StringBuffer buf = new StringBuffer(map.size() * 32);
        buf.append('{');

        if (newline)
        {
            buf.append(SystemUtils.LINE_SEPARATOR);
        }
        Object[] entries = map.entrySet().toArray();
      
       
        for(Object O :entries )
        {
            Map.Entry property = (Map.Entry)O;
            buf.append(property.getKey());
            buf.append('=');
            buf.append(property.getValue());

            if (newline)
            {
                buf.append(SystemUtils.LINE_SEPARATOR);
            }
            else
            {
                buf.append(',');
            }
        }
        if(newline==false){
        	buf.delete(buf.length()-1, buf.length());
        }
        buf.append('}');
        return buf.toString();
    }
    
    /**
     * Map的key和Value分别放入list中，0为key,1为value
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List toList(Map map){
    	if(map.isEmpty()){
    		return null;
    	}
    	List list,list1,list2;
    	list=new LinkedList();
    	list1=new LinkedList();
    	list2=new LinkedList();
    	
    	Object[] entries = map.entrySet().toArray();
    	 for(Object O :entries )
         {
             Map.Entry property = (Map.Entry)O;
            list1.add(property.getKey());
            list2.add(property.getValue());
         }
    	list.add(list1);
    	list.add(list2);
    	list1.clear();
    	list2.clear();
    	return list;
    }
    
    /**
     * 得到Map的Key，返回的是Object[]
     * @param map
     * @return
     */
  	public static Object[] getMapKey(Map map){
  		Object[] key = new Object[map.size()];
  		Set setKey = map.keySet();
  		int i = 0;
  		for(Object o : setKey){
  			
  			key[i++]=o;
  		}
  		return key;
  	}
  	/**
  	 * 通过Map的key，得到value，返回的Object[]
  	 * @param data
  	 * @param key
  	 * @return
  	 */
  	public static  Object[] getMapValue(Map data,Object[] key){
  		
  		Object[] value=new Object[data.size()];
  		for(int i=0;i <data.size();i++){
  			value[i]=data.get(key[i]);
  		}
  		return value;
  	}

}

