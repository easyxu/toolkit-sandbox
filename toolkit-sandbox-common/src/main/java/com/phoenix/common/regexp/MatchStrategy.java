package com.phoenix.common.regexp;

/**
 * 匹配策略.
 *
 * @author Michael Zhou
 * @version $Id: MatchStrategy.java 509 2004-02-16 05:42:07Z baobao $
 */
public interface MatchStrategy {
    /** 最佳匹配策略, 总是试图匹配最长的一项. 如果有多项具有相同的匹配长度, 则返回第一个匹配项. */
    MatchStrategy BEST_MATCH_STRATEGY = new BestMatchStrategy();

    /**
     * 试图匹配指定的输入值, 如果成功, 则返回<code>true</code>.  调用者可以通过<code>context.getMatchItem()</code>来取得匹配项.
     *
     * @param context 匹配上下文
     *
     * @return 如果匹配成功, 则返回<code>true</code>
     */
    boolean matches(MatchContext context);
}

