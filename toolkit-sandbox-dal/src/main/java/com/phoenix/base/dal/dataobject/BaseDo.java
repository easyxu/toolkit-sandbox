package com.phoenix.base.dal.dataobject;

import java.util.Date;



public class BaseDo {
    public static final String ID           = "id";
    public static final String GMT_CREATE   = "gmtCreate";
    public static final String GMT_MODIFIED = "gmtModified";
    public static final String CREATOR      = "creator";
    public static final String MODIFIER     = "modifier";
    public static final String IS_DELETED   = "isDeleted";
    public static final String PAGE_START   = "pageStart";
    public static final String PAGE_SIZE    = "pageSize";
    public static final String PAGE_ORDER   = "pageOrder";

    protected Integer          id;
    protected Date             gmtCreate;
    protected Date             gmtModified;
    protected String           creator;
    protected String           modifier;
    protected String           isDeleted;

    protected boolean          defaultBiz   = true;

    //分页参数
    protected Integer          pageStart;
    protected Integer          pageSize;
    //排序参数
    protected String[]         pageOrder;

    /**
     * 是否要填写缺省值，针对creator,gmt_create,modifier,gmt_modified,is_deleted 几个字段.
     * @return
     */
    public boolean isDefaultBiz() {
        return defaultBiz;
    }

    /**
     * 设置是否要填写缺省值.
     * @param defaultBiz
     * @see isDefaultBiz
     */
    public void setDefaultBiz(boolean defaultBiz) {
        this.defaultBiz = defaultBiz;
    }

    /**
     * 一般作为主键.
     * @return
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 记录创建者.
     * @return
     */
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 记录创建时间.
     * @return
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 记录最后修改时间.
     * @return
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 是否逻辑删除了.
     * @return
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 最后修改者.
     * @return
     */
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 排序参数
     * @return
     */
    public String[] getPageOrder() {
        return pageOrder;
    }

    public void setPageOrder(String[] pageOrder) {
        this.pageOrder = pageOrder;
    }

    /**
     * 分页查询开始序号，是页号，不是记录号.
     * @return
     */
    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    /**
     * 分页查询页大小.
     * @param pageStart
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
