package com.phoenix.base.dal.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.orm.ibatis.SqlMapClientCallback;
//import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.BatchResult;
import com.phoenix.base.dal.dataobject.BaseDo;
import com.phoenix.base.dal.dataobject.DoConstant;



/*public class IbatisBaseDaoImpl  extends SqlMapClientDaoSupport implements BaseDao{
    static String   UNDERSCORE = "_";
    static String   DOT        = ".";
    static String   EMPTY_STR  = "";
    static Logger   log        = LoggerFactory.getLogger(IbatisBaseDaoImpl.class);
    OperatorAdapter operatorAdapter;

    public void setOperatorAdapter(OperatorAdapter operatorAdapter) {
        this.operatorAdapter = operatorAdapter;
    }

    public static String toUpperCaseWithUnderscores(String str) {
        StringBuffer result = new StringBuffer();
        if (str != null) {
            boolean lastIsNum = false;
            result.append(str.charAt(0));
            for (int i = 1; i < str.length(); i++) {
                char aChar = str.charAt(i);
                boolean thisIsNum = (aChar >= '0' && aChar <= '9');
                if ((aChar >= 'A' && aChar <= 'Z') || (lastIsNum != thisIsNum)) {
                    result.append('_');
                }
                lastIsNum = thisIsNum;
                result.append(Character.toUpperCase(aChar));
            }
        }
        return result.toString();
    }

    public int delete(Class<?> clazz, Object obj, String idPostfix) {
        return delete(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int delete(Object obj) {
        return delete(obj, null);
    }

    public int delete(Object obj, String idPostfix) {
        isBaseDo(obj);
        return delete(obj.getClass(), obj, idPostfix);
    }

    public int delete(String nameSpace, Object obj, String idPostfix) {
        return delete(nameSpace, obj, idPostfix, null);
    }

    protected int delete(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        setDefaultValues(obj, DELETE_OP);
        return getSqlMapClientTemplate().delete(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + DELETE
                        + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public int deleteBatch(Class<?> clazz, List<?> obj, String idPostfix) {
        return deleteBatch(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int deleteBatch(List<?> obj) {
        return deleteBatch(obj, null);
    }

    public int deleteBatch(List<?> obj, String idPostfix) {
        if (obj != null && obj.size() > 0) {
            Object objItem = obj.get(0);
            isBaseDo(objItem);
            return deleteBatch(objItem.getClass(), obj, idPostfix);
        }
        return 0;
    }

    public int deleteBatch(String nameSpace, List<?> obj, String idPostfix) {
        return deleteBatch(nameSpace, obj, idPostfix, null);
    }

    @SuppressWarnings("unchecked")
    protected int deleteBatch(String nameSpace, List<?> obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        final String sqlMapId = nameSpace + DOT
                + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + DELETE
                + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase()));
        final List<Object> objList = (List<Object>) obj;
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (Object objItem : objList) {
                    setDefaultValues(objItem, DELETE_OP);
                    executor.delete(sqlMapId, objItem);
                }
                try {
                    return executor.executeBatchDetailed();
                } catch (BatchException e) {
                    log.error(EMPTY_STR, e);
                    throw new SQLException("Batch Delete failed!");
                }
            }
        };
        return batchResult((List<BatchResult>) this.getSqlMapClientTemplate().execute(callback));
    }

    public Object getObj(Class<?> clazz, Object obj, String idPostfix) {
        return getObj(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public Object getObj(Object obj) {
        return this.getObj(obj, null);
    }

    public Object getObj(Object obj, String idPostfix) {
        isBaseDo(obj);
        return getObj(obj.getClass(), obj, idPostfix);
    }

    public Object getObj(String nameSpace, Object obj, String idPostfix) {
        return getObj(nameSpace, obj, idPostfix, null);
    }

    protected Object getObj(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        return getSqlMapClientTemplate().queryForObject(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE))
                        + (idPostfix == null ? SELECT_BY_PK : (SELECT + UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public List<Object> getObjList(Class<?> clazz, Object obj, String idPostfix) {
        return getObjList(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public List<Object> getObjList(Object obj, String idPostfix) {
        isBaseDo(obj);
        return getObjList(obj.getClass(), obj, idPostfix);
    }

    public List<Object> getObjList(String nameSpace, Object obj, String idPostfix) {
        return getObjList(nameSpace, obj, idPostfix, null);
    }

    @SuppressWarnings("unchecked")
    protected List<Object> getObjList(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        return (List<Object>) getSqlMapClientTemplate().queryForList(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + SELECT
                        + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public int getSelectCount(Class<?> clazz, Object obj, String idPostfix) {
        return getSelectCount(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int getSelectCount(Object obj, String idPostfix) {
        isBaseDo(obj);
        return getSelectCount(obj.getClass(), obj, idPostfix);
    }

    public int getSelectCount(String nameSpace, Object obj, String idPostfix) {
        return getSelectCount(nameSpace, obj, idPostfix, null);
    }

    protected int getSelectCount(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        return (Integer) getSqlMapClientTemplate().queryForObject(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + SELECT_COUNT
                        + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public Object insert(Class<?> clazz, Object obj, String idPostfix) {
        return insert(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public Object insert(Object obj) {
        return insert(obj, null);
    }

    public Object insert(Object obj, String idPostfix) {
        isBaseDo(obj);
        return insert(obj.getClass(), obj, idPostfix);
    }

    public Object insert(String nameSpace, Object obj, String idPostfix) {
        return insert(nameSpace, obj, idPostfix, null);
    }

    protected Object insert(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        setDefaultValues(obj, INSERT_OP);
        return getSqlMapClientTemplate().insert(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + INSERT
                        + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public int insertBatch(Class<?> clazz, List<?> obj, String idPostfix) {
        return insertBatch(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int insertBatch(List<?> objs) {
        return insertBatch(objs, null);
    }

    public int insertBatch(List<?> obj, String idPostfix) {
        if (obj != null && obj.size() > 0) {
            Object objItem = obj.get(0);
            isBaseDo(objItem);
            return insertBatch(objItem.getClass(), obj, idPostfix);
        }
        return 0;
    }

    public int insertBatch(String nameSpace, List<?> obj, String idPostfix) {
        return insertBatch(nameSpace, obj, idPostfix, null);
    }

    @SuppressWarnings("unchecked")
    protected int insertBatch(String nameSpace, List<?> obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        final String sqlMapId = nameSpace + DOT
                + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + INSERT
                + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase()));
        final List<Object> objList = (List<Object>) obj;
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (Object objItem : objList) {
                    setDefaultValues(objItem, INSERT_OP);
                    executor.insert(sqlMapId, objItem);
                }
                try {
                    return executor.executeBatchDetailed();
                } catch (BatchException e) {
                    log.error(EMPTY_STR, e);
                    throw new SQLException("Batch Delete failed!");
                }
            }
        };
        return batchResult((List<BatchResult>) this.getSqlMapClientTemplate().execute(callback));
    }

    protected void isBaseDo(Object obj) {
        if (!(obj instanceof BaseDo)) {
            throw new RuntimeException("BaseDo expected! Or you MUST give me a NameSpace for Ibtis SQL mapping.");
        }
    }

    protected void checkNameSpace(String nameSpace) {
        if (nameSpace == null) {
            throw new RuntimeException("MUST give me a NameSpace for Ibtis SQL mapping.");
        }
    }

    @SuppressWarnings("unchecked")
    protected void setDefaultValues(Object obj, int operation) {
        String operator = null;
        if (operatorAdapter == null) {
            log.info("No operator information configured in DAO.");
        } else {
            operator = operatorAdapter.getOperator();
        }
        try {
            Date now = new Date();
            if (obj instanceof BaseDo) {
                BaseDo data = (BaseDo) obj;
                if (data.isDefaultBiz()) {
                    switch (operation) {
                    case INSERT_OP:
                        data.setGmtCreate(now);
                        data.setGmtModified(now);
                        data.setIsDeleted(DoConstant.NO);
                        if (!(operator == null || "".equals(operator))) {
                            data.setCreator(operator);
                            data.setModifier(operator);
                        }
                        break;
                    case UPDATE_OP:
                    case DELETE_OP:
                        data.setGmtModified(new Date());
                        if (!(operator == null || "".equals(operator))) {
                            data.setModifier(operator);
                        }
                        break;
                    }
                }
            } else if (obj instanceof Map) {
                Map data = (Map) obj;
                if (!data.containsKey(DEFAULT_BIZ) || Boolean.TRUE.equals(data.get(DEFAULT_BIZ))) {
                    switch (operation) {
                    case INSERT_OP:
                        data.put(DoConstant.GMT_CREATE, now);
                        data.put(DoConstant.GMT_MODIFIED, now);
                        if (!(operator == null || "".equals(operator))) {
                            data.put(DoConstant.CREATOR, operator);
                            data.put(DoConstant.MODIFIER, operator);
                        }
                        data.put(DoConstant.IS_DELETED, DoConstant.NO);
                        break;
                    case UPDATE_OP:
                    case DELETE_OP:
                        data.put(DoConstant.GMT_MODIFIED, now);
                        if (!(operator == null || "".equals(operator))) {
                            ((Map) obj).put(DoConstant.MODIFIER, operator);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Set default field failed!", e);
        }
    }

    *//**
     * 从批处理结果返回处理成功的记录数
     * @param batchResultList
     * @return
     *//*
    protected int batchResult(List<BatchResult> batchResultList) {
        BatchResult result = batchResultList.get(0);
        int count = 0;
        for (int i : result.getUpdateCounts()) {
            if (i == java.sql.Statement.SUCCESS_NO_INFO) {
                count++;
            } else {
                count += i;
            }
        }
        return count;
    }

    @Test
    public void tt() {
        log.info(toUpperCaseWithUnderscores("AbcDef123"));
    }

    public int update(Class<?> clazz, Object obj, String idPostfix) {
        return update(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int update(Object obj) {
        return update(obj, null);
    }

    public int update(Object obj, String idPostfix) {
        isBaseDo(obj);
        return update(obj.getClass(), obj, idPostfix);
    }

    public int update(String nameSpace, Object obj, String idPostfix) {
        return update(nameSpace, obj, idPostfix, null);
    }

    public int update(String nameSpace, Object obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        setDefaultValues(obj, UPDATE_OP);
        return getSqlMapClientTemplate().update(
                nameSpace + DOT + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + UPDATE
                        + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase())), obj);
    }

    public int updateBatch(Class<?> clazz, List<?> obj, String idPostfix) {
        return updateBatch(toUpperCaseWithUnderscores(clazz.getSimpleName()), obj, idPostfix);
    }

    public int updateBatch(List<?> obj) {
        return updateBatch(obj);
    }

    public int updateBatch(List<?> obj, String idPostfix) {
        if (obj != null && obj.size() > 0) {
            Object objItem = obj.get(0);
            isBaseDo(objItem);
            return updateBatch(objItem.getClass(), obj, idPostfix);
        }
        return 0;
    }

    public int updateBatch(String nameSpace, List<?> obj, String idPostfix) {
        return updateBatch(nameSpace, obj, idPostfix, null);
    }

    @SuppressWarnings("unchecked")
    public int updateBatch(String nameSpace, List<?> obj, String idPostfix, String idPrefix) {
        checkNameSpace(nameSpace);
        final String sqlMapId = nameSpace + DOT
                + (idPrefix == null ? EMPTY_STR : (idPrefix.toUpperCase() + UNDERSCORE)) + UPDATE
                + (idPostfix == null ? EMPTY_STR : (UNDERSCORE + idPostfix.toUpperCase()));
        final List<Object> objList = (List<Object>) obj;
        SqlMapClientCallback callback = new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (Object objItem : objList) {
                    setDefaultValues(objItem, UPDATE_OP);
                    executor.update(sqlMapId, objItem);
                }
                try {
                    return executor.executeBatchDetailed();
                } catch (BatchException e) {
                    log.error(EMPTY_STR, e);
                    throw new SQLException("Batch update failed!");
                }
            }
        };
        return batchResult((List<BatchResult>) this.getSqlMapClientTemplate().execute(callback));
    }
}*/
