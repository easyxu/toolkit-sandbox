package com.phoenix.model;

import java.io.Serializable;

public class Pager implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 2741712226754231881L;
    private Integer pageCount;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer recordCount;
    private Integer start;
    private boolean isOnlyCount = false;//是否只取总数
    private boolean isLexUse = false;//是否是Lex使用
    public boolean isLexUse() {
		return isLexUse;
	}
	public void setLexUse(boolean isLexUse) {
		this.isLexUse = isLexUse;
	}
	public Integer getPageCount() {
        return pageCount;
    }
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer computePageCount(){
        Double v=new Double(Math.ceil((recordCount+0.0) / pageSize));
        this.pageCount=v.intValue() ;
        return this.pageCount;
    }
    public boolean isOnlyCount() {
        return isOnlyCount;
    }
    public void setOnlyCount(boolean isOnlyCount) {
        this.isOnlyCount = isOnlyCount;
    }
 

}
