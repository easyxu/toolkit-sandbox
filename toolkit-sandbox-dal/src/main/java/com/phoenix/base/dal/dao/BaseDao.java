package com.phoenix.base.dal.dao;

import java.util.List;

public interface BaseDao {
    public static final String DEFAULT_BIZ  = "__defaultBiz";
    public static final String DELETE       = "DELETE";
    public static final int    DELETE_OP    = 4;
    public static final String INSERT       = "INSERT";
    public static final int    INSERT_OP    = 1;
    public static final String SELECT       = "SELECT";
    public static final String SELECT_BY_PK = "SELECT_BY_PK";
    public static final String SELECT_COUNT = "SELECT_COUNT";
    public static final String UPDATE       = "UPDATE";
    public static final int    UPDATE_OP    = 2;

    public int delete(Class<?> clazz, Object obj, String idPostfix);

    public int delete(Object obj);

    public int delete(Object obj, String idPostfix);

    public int delete(String nameSpace, Object obj, String idPostfix);

    public int deleteBatch(Class<?> clazz, List<?> obj, String idPostfix);

    public int deleteBatch(List<?> obj);

    public int deleteBatch(List<?> obj, String idPostfix);

    public int deleteBatch(String nameSpace, List<?> obj, String idPostfix);

    public Object getObj(Class<?> clazz, Object obj, String idPostfix);

    public Object getObj(Object obj);

    public Object getObj(Object obj, String idPostfix);

    public Object getObj(String namaSpace, Object obj, String idPostfix);

    public List<Object> getObjList(Class<?> clazz, Object obj, String idPostfix);

    public List<Object> getObjList(Object obj, String idPostfix);

    public List<Object> getObjList(String nameSpace, Object obj, String idPostfix);

    public int getSelectCount(Class<?> clazz, Object ojb, String idPostfix);

    public int getSelectCount(Object ojb, String idPostfix);

    public int getSelectCount(String nameSpace, Object ojb, String idPostfix);

    public Object insert(Class<?> clazz, Object obj, String idPostfix);

    public Object insert(Object obj);

    public Object insert(Object obj, String idPostfix);

    public Object insert(String nameSpace, Object obj, String idPostfix);

    public int insertBatch(Class<?> clazz, List<?> obj, String idPostfix);

    public int insertBatch(List<?> objs);

    public int insertBatch(List<?> obj, String idPostfix);

    public int insertBatch(String nameSpace, List<?> obj, String idPostfix);

    public int update(Class<?> clazz, Object obj, String idPostfix);

    public int update(Object obj);

    public int update(Object obj, String idPostfix);

    public int update(String nameSpace, Object obj, String idPostfix);

    public int updateBatch(Class<?> clazz, List<?> obj, String idPostfix);

    public int updateBatch(List<?> obj);

    public int updateBatch(List<?> obj, String idPostfix);

    public int updateBatch(String nameSpace, List<?> obj, String idPostfix);
}
