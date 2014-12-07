package com.phoenix.common.regexp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.phoenix.common.collection.Predicate;

/**
 * 最佳匹配策略, 总是试图匹配最长的一项. 如果有多项具有相同的匹配长度, 则返回第一个匹配项.
 *
 * @author Michael Zhou
 * @version $Id: BestMatchStrategy.java 509 2004-02-16 05:42:07Z baobao $
 */
@SuppressWarnings("unchecked")
public class BestMatchStrategy implements MatchStrategy {
    /** 按匹配长度从大到小排序的比较器. */
    private static final Comparator MATCH_LENGTH_COMPARATOR = new Comparator() {
            public int compare(Object item1, Object item2) {
                return ((MatchItem) item2).length() - ((MatchItem) item1).length();
            }
        };

    /**
     * 试图匹配指定的输入值. 如果成功, 则返回<code>true</code>.  调用者可以通过<code>context.getMatchItem()</code>来取得匹配项.
     *
     * @param context 匹配上下文
     *
     * @return 如果匹配成功, 则返回<code>true</code>
     */
	public boolean matches(MatchContext context) {
        Predicate predicate = context.getPredicate();

        // 如果没有predicate, 则选择使用更高效的策略
        if (predicate == null) {
            return matchWithoutPredicate(context);
        }

        Collection patterns      = context.getPatterns();
        List       matchItemList = new ArrayList(patterns.size());

        for (Iterator i = patterns.iterator(); i.hasNext();) {
            MatchPattern pattern = (MatchPattern) i.next();

            if (pattern.matches(context)) {
                matchItemList.add(context.getLastMatchItem());
            }
        }

        // 不匹配, 则直接返回null
        if (matchItemList.size() == 0) {
            return false;
        }

        // 按匹配长度由大到小排序(稳定)
        Collections.sort(matchItemList, MATCH_LENGTH_COMPARATOR);

        // 通过指定的predicate过滤所有匹配项
        for (Iterator i = matchItemList.iterator(); i.hasNext();) {
            MatchItem item = (MatchItem) i.next();

            if (predicate.evaluate(item)) {
                context.setLastMatchItem(item);
                return true;
            }
        }

        return false;
    }

    /**
     * 试图匹配指定的输入值, 不判断predicate, 具有较高的效率.
     *
     * @param context 匹配上下文
     *
     * @return 如果匹配成功, 则返回<code>true</code>
     */
    private boolean matchWithoutPredicate(MatchContext context) {
        MatchItem bestMatchItem   = null;
        int       bestMatchLength = -1;

        for (Iterator i = context.getPatterns().iterator(); i.hasNext();) {
            MatchPattern pattern = (MatchPattern) i.next();

            if (pattern.matches(context)) {
                MatchItem matchItem   = context.getLastMatchItem();
                int       matchLength = matchItem.length();

                if (matchLength > bestMatchLength) {
                    bestMatchItem       = matchItem;
                    bestMatchLength     = matchLength;
                }
            }
        }

        if (bestMatchItem != null) {
            context.setLastMatchItem(bestMatchItem);
            return true;
        }

        return false;
    }
}
