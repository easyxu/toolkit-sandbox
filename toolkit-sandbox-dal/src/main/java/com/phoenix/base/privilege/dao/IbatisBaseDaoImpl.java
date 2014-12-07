package com.phoenix.base.privilege.dao;

import java.util.ArrayList;
import java.util.List;



/*
public class IbatisBaseDaoImpl extends com.phoenix.base.dal.dao.IbatisBaseDaoImpl implements BaseDao{
    public int delete(Object obj, PrivilegeObj pvgObj) {
        return delete(obj, pvgObj, null);
    }

    public int delete(Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return delete(obj.getClass(), obj, pvgObj, idPostfix);
    }

    public int delete(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return delete(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public int delete(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        setDefaultValues(obj, DELETE_OP);
        pvgObj.merge(obj);
        return delete(nameSpace, (Object) pvgObj, idPostfix, PVG);
    }

    public List<Object> getObjList(Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getObjList(obj.getClass(), obj, pvgObj, idPostfix);
    }

    public List<Object> getObjList(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getObjList(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public List<Object> getObjList(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        pvgObj.merge(obj);
        return getObjList(nameSpace, (Object) pvgObj, idPostfix, PVG);
    }

    public Object getObj(Object obj, PrivilegeObj pvgObj) {
        return this.getObj(obj, pvgObj, null);
    }

    public Object getObj(Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getObj(obj.getClass(), obj, pvgObj, idPostfix);
    }

    public Object getObj(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getObj(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public Object getObj(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        pvgObj.merge(obj);
        return getObj(nameSpace, (Object) pvgObj, idPostfix, PVG);
    }

    public int getSelectCount(Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getSelectCount(obj.getClass(), obj, pvgObj, idPostfix);
    }

    public int getSelectCount(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return getSelectCount(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public int getSelectCount(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        pvgObj.merge(obj);
        return getSelectCount(nameSpace, (Object) pvgObj, idPostfix, PVG);
    }

    public int update(Object obj, PrivilegeObj pvgObj) {
        return update(obj, pvgObj, null);
    }

    public int update(Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return update(obj.getClass(), obj, pvgObj, idPostfix);
    }

    public int update(Class<?> clazz, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        return update(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public int update(String nameSpace, Object obj, PrivilegeObj pvgObj, String idPostfix) {
        setDefaultValues(obj, UPDATE_OP);
        pvgObj.merge(obj);
        return update(nameSpace, (Object) pvgObj, idPostfix, PVG);
    }

    public int deleteBatch(List<?> obj, PrivilegeObj pvgObj) {
        return deleteBatch(obj, pvgObj, null);
    }

    public int deleteBatch(List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        if (obj != null && obj.size() > 0) {
            Object objItem = obj.get(0);
            isBaseDo(objItem);
            return deleteBatch(objItem.getClass(), obj, pvgObj, idPostfix);
        }
        return 0;
    }

    public int deleteBatch(String nameSpace, List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        return deleteBatch(nameSpace, getMergedList(obj, pvgObj), idPostfix, PVG);
    }

    protected List<PrivilegeObj> getMergedList(List<?> obj, PrivilegeObj pvgObj) {
        List<PrivilegeObj> pvgObjs = new ArrayList<PrivilegeObj>(obj.size());
        for (Object objItem : obj) {
            PrivilegeObj pvg = new PrivilegeObj(pvgObj);
            pvg.merge(objItem);
            pvgObjs.add(pvg);
        }
        return pvgObjs;
    }

    public int deleteBatch(Class<?> clazz, List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        return deleteBatch(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }

    public int updateBatch(List<?> obj, PrivilegeObj pvgObj) {
        return updateBatch(obj, pvgObj, null);
    }

    public int updateBatch(List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        if (obj != null && obj.size() > 0) {
            Object objItem = obj.get(0);
            isBaseDo(objItem);
            return updateBatch(objItem.getClass(), obj, pvgObj, idPostfix);
        }
        return 0;
    }

    public int updateBatch(String nameSpace, List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        return updateBatch(nameSpace, getMergedList(obj, pvgObj), idPostfix, PVG);
    }

    public int updateBatch(Class<?> clazz, List<?> obj, PrivilegeObj pvgObj, String idPostfix) {
        return updateBatch(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, pvgObj, idPostfix);
    }
}
*/
