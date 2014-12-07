package com.phoenix.base.privilege.dao;

import java.util.List;



public interface BaseDao extends com.phoenix.base.dal.dao.BaseDao{
    public static final String    PVG    = "PVG";

    public Object getObj(Object obj, PrivilegeObj pvgObj);

    public Object getObj(Object obj, PrivilegeObj pvgObj, String idPostfix);

    public Object getObj(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public Object getObj(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int getSelectCount(Object ojb, PrivilegeObj pvgObj, String idPostfix);

    public int getSelectCount(String nameSpace, Object ojb, PrivilegeObj pvgObj, String idPostfix);

    public int getSelectCount(Class<?> clazz, Object ojb, PrivilegeObj pvgObj, String idPostfix);

    public List<Object> getObjList(Object obj, PrivilegeObj pvgObj, String idPostfix);

    public List<Object> getObjList(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public List<Object> getObjList(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int update(Object obj, PrivilegeObj pvgObj);

    public int update(Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int update(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int update(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int updateBatch(List<?> obj, PrivilegeObj pvgObj);

    public int updateBatch(List<?> obj, PrivilegeObj pvgObj, String idPostfix);

    public int updateBatch(String nameSpace, List<?> obj, PrivilegeObj pvgObj, String idPostfix);

    public int updateBatch(Class<?> clazz, List<?> obj, PrivilegeObj pvgObj, String idPostfix);

    public int delete(Object obj, PrivilegeObj pvgObj);

    public int delete(Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int delete(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int delete(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix);

    public int deleteBatch(List<?> obj, PrivilegeObj pvgObj);

    public int deleteBatch(List<?> obj, PrivilegeObj pvgObj, String idPostfix);

    public int deleteBatch(String nameSpace, List<?> obj, PrivilegeObj pvgObj, String idPostfix);

    public int deleteBatch(Class<?> clazz, List<?> obj, PrivilegeObj pvgObj, String idPostfix);
}
