package com.phoenix.common.collection;

import java.util.Iterator;

/**
 * 将一个<code>Iterator</code>中的值转换成另一个值的过滤器.
 * 
 * @version $Id: TransformIterator.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings("unchecked")
public class TransformIterator extends FilterIterator {
	private Transformer transformer;

	/**
	 * 创建一个过滤器.
	 * 
	 * @param iterator
	 *            被过滤的<code>Iterator</code>
	 * @param transformer
	 *            转换器
	 */
	public TransformIterator(Iterator iterator, Transformer transformer) {
		super(iterator);
		this.transformer = transformer;
	}

	/**
	 * 取得转换器.
	 * 
	 * @return 转换器对象
	 */
	public Transformer getTransformer() {
		return transformer;
	}

	/**
	 * 取得下一个对象.
	 * 
	 * @return 下一个经过转换的对象
	 */
	public Object next() {
		return transform(super.next());
	}

	/**
	 * 转换对象.
	 * 
	 * @param input
	 *            输入对象
	 * 
	 * @return 转换后的对象
	 */
	private Object transform(Object input) {
		Transformer transformer = getTransformer();

		if (transformer != null) {
			return transformer.transform(input);
		}

		return input;
	}
}
