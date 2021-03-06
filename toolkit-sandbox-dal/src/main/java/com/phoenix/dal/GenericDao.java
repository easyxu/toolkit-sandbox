package com.phoenix.dal;

import com.phoenix.model.Pager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xux on 14-12-7.
 */
public interface GenericDao<T extends Serializable,ID extends Serializable>  {
    /**
     * 执行hql语句执行更新操作，可以为新增，修改，删除，但不能是查询,返回受此操作影响的记录数量
     * @param hql
     * @param params
     */
    int executeUpdate(String hql, Map<String, Object> params);

    /**
     *
     * @param model
     */
    void saveOrUpdate(T model);

    /**
     *
     * @param model
     */
    void save(T model);

    /**
     *
     * @param model
     */
    void update(T model);

    /**
     *
     * @param model
     */
    void delete(T model);

    /**
     *
     * @param pk
     */
    void deleteByID(ID pk);

    /**
     *
     * @param pk
     * @return
     */
    T findById(ID pk);


    /**
     *
     * @return
     */
    List<T> findAll();
    /**
     *
     * @param hql
     * @return
     */
    List<?> findByHQL(String hql);


//    /**
//     * 嵌入查询总数
//     * @param pager
//     * @param sql
//     * @param queryParams
//     * @param clazz
//     * @return
//     */
//    List<Object[]> pageableFindByNativeNestedCount(Pager pager, String sql, Map<String, Object> queryParams,Class clazz);
    /**
     *
     * @param pager
     * @param hql
     * @return
     */
    List<T> pageableFindByHQL(Pager pager, String hql);
    /**
     *
     * @param hql			语句
     * @param queryParams	查询参数
     * @return
     */
    List<T> findByHQL(String hql, Map<String, Object> queryParams);
    /**
     *
     * @param pager			分页
     * @param hql			语句
     * @param queryParams	查询参数
     * @return
     */
    List<T> pageableFindByHQL(Pager pager, String hql, Map<String, Object> queryParams);

    /**
     *
     * @param pager			分页
     * @param hql			语句
     * @param queryParams	查询参数
     * @param clazz			动态转换类型
     * @return
     */
    <ANY> List<ANY> pageableFindByHQL(Pager pager, String hql,
                                      Map<String, Object> queryParams, Class<ANY> clazz);
    /**
     *
     * @param sql
     * @param queryParams
     * @return
     */
    List<?> findByNativeSql(String sql, Map<String, Object> queryParams);
    /**
     *
     * @param pager
     * @param sql
     * @param queryParams
     * @return
     */
    List<?> pageableFindByNativeSql(Pager pager, String sql, Map<String, Object> queryParams);
    /**
     *
     * @param pager
     * @param sql
     * @param queryParams
     * @param entityClass
     * @return
     */
    <ANY> List<ANY> pageableFindByNativeSql(Pager pager, String sql,
                                            Map<String, Object> queryParams, Class<ANY> entityClass);

    /**
     * 定制的查询方法
     * @param pager
     * @param sql
     * @param queryParams
     * @param clazz
     * @return
     */
    List<?> pageCacheFindByNativeSql(Pager pager, String sql,
                                     Map<String, Object> queryParams, Class<?> clazz);

    /**
     * 得到唯一的结果值
     * @param hql
     * @param queryParams
     * @return
     */
    Object uniqueResult(String hql, Map<String, Object> queryParams) ;








    /**
     * 命名查询 --数组 findByNamedQueryAndNamedParam(queryName, paramNames, values)
     * queryName为查询名 paramNames String[]paramNames = new String[]{"xu","xiang"};
     * Object[] values = new Object[]{徐,翔};
     *
     * @param queryName
     * @param paramNames
     * @param values
     * @return
     */
    public List<?> findByNamedQueryAndNamedParam(String queryName,
                                                 String[] paramNames, Object[] values);


    /**
     * 命名查询 queryName为查询名 paramNames 如果查询语句里的参数是 from Persion p where xu=:xu
     * ,paramNames 就是 xu
     *
     * @param queryName
     * @param paramName
     * @param value
     * @return
     */
    public List<?> findByNamedQueryAndNamedParam(String queryName,
                                                 String paramName, Object value) ;




    /**
     * 命名查询 没有参数
     *
     * @param queryName
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedQuery(String queryName);





    /**
     * queryString是hql语句
     *
     * @param queryString
     * @param paramName
     * @param value
     * @return
     */
    public List<?> findByNamedParam(String queryString, String paramName,
                                    Object value) ;

    /**
     * queryString是hql语句
     *
     * @param queryString
     * @param paramNames
     * @param values
     * @return
     * @throws org.springframework.dao.DataAccessException
     */
    public List<?> findByNamedParam(String queryString, String[] paramNames,
                                    Object... values) ;


    /**
     *
     * @param queryString hql语句
     * @param values
     * @return
     */
    List<?> findByNamedParam(String queryString, Object... values);


}
