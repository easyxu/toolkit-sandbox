package com.phoenix.common.collection;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SortItem<T> {

	private T obj;

	private List<SortItem> around = new ArrayList<SortItem>();
	private List<SortItem> within = new ArrayList<SortItem>();

	public SortItem(T obj) {
		this.obj = obj;
	}

	public T getObj() {
		return obj;
	}

	public List<SortItem> getAround() {
		return around;
	}

	public List<SortItem> getWithin() {
		return within;
	}

}