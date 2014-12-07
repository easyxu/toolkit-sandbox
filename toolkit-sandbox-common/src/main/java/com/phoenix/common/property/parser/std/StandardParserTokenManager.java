package com.phoenix.common.property.parser.std;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SuppressWarnings({"unchecked","unused"})
public class StandardParserTokenManager implements StandardParserConstants {
	// TokenManager记录器.
	protected static Logger log = LoggerFactory
			.getLogger("property.StandardParser.TokenManager");

	// 状态栈.
	
	private List stateStack = new ArrayList();

	/**
	 * 清除所有状态到初始值. 切记! 每次ReInit()以后, 一定要调用此方法, 才可继续解析文件.
	 */
	public void clearStateStack() {
		stateStack.clear();
	}

	/**
	 * 确保当<EOF>返回时，状态为DEFAULT.
	 */
	void CommonTokenAction(Token t) {
		if (log.isDebugEnabled()) {
			log.debug("Matched token "
					+ tokenImage[t.kind]
					+ (t.kind == EOF ? "" : ": \""
							+ TokenMgrError.addEscapes(t.image) + "\"."));
		}
		if (t.kind == EOF && curLexState != DEFAULT) {
			if (log.isDebugEnabled()) {
				log.debug("Unexpected End Of File.  Lexical state "
						+ lexStateNames[DEFAULT] + " expected, but was "
						+ lexStateNames[curLexState] + ".");
			}
			throw new TokenMgrError("Unexpected End Of File.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		}
	}

	/**
	 * 将当前状态压入栈，将当前状态设为指定的新状态.
	 * 
	 * @param newState
	 *            新状态.
	 */
	private void pushLexState(int newState) {
		stateStack.add(new Integer(curLexState));
		SwitchTo(newState);
	}

	/**
	 * 从栈中恢复状态. 如果栈为空, 则设置为DEFAULT状态.
	 * 
	 * @return 如果栈为空, 则返回false, 否则返回true.
	 */
	private boolean popLexState() {
		int lastIndex = stateStack.size() - 1;
		if (lastIndex < 0) {
			SwitchTo(DEFAULT);
			return false;
		}
		SwitchTo(((Integer) stateStack.remove(lastIndex)).intValue());
		return true;
	}

	/**
	 * 将换行符放回stream. 如果最后一次读取的不是换行符, 则不做任何事.
	 */
	private void putbackNewLine() {
		int length = image.length();
		if (length >= 1) {
			char c = image.charAt(length - 1); // 取得最后一个字符
			if (c == '\r' || c == '\n') {
				input_stream.backup(1);
			}
		}
	}

	/**
	 * 将"\n", "\r"之类换义字符串替换为它所表示的字符.
	 */
	private void unescapeChar() {
		int lastIndex = image.length() - 1;
		char c = image.charAt(lastIndex);
		char translated;
		switch (c) {
		case 'n':
			translated = '\n';
			break;
		case 't':
			translated = '\t';
			break;
		case 'b':
			translated = '\b';
			break;
		case 'r':
			translated = '\r';
			break;
		case 'f':
			translated = '\f';
			break;
		default:
			translated = c;
		}
		image.setCharAt(lastIndex - 1, translated);
		image.setLength(lastIndex);
	}

	/**
	 * 将"\x"替换成"x", 即忽略前置的反斜杠.
	 */
	private void unescapeCharSimple() {
		int lastIndex = image.length() - 1;
		char c = image.charAt(lastIndex);
		image.setCharAt(lastIndex - 1, c);
		image.setLength(lastIndex);
	}

	/**
	 * 文本缓冲区及相关变量. 用于辅助解析简单串, 单引号块, 双引号块.
	 */
	private StringBuffer textBlock = new StringBuffer();
	private String blockEndTag;
	private int lastNonspaceIndex = 0;
	private boolean multipartString = false;
	private int startIndex = 0;

	/**
	 * 保存文本缓冲区最后一个非空字符的索引.
	 */
	private void appendTextBlock(boolean forceUpdateNonspaceIndex) {
		char c = image.charAt(image.length() - 1);
		textBlock.append(c);
		if (forceUpdateNonspaceIndex || c > ' ') {
			lastNonspaceIndex = textBlock.length();
		}
	}

	public java.io.PrintStream debugStream = System.out;

	public void setDebugStream(java.io.PrintStream ds) {
		debugStream = ds;
	}

	private final int jjMoveStringLiteralDfa0_18() {
		return jjMoveNfa_18(0, 0);
	}

	private final void jjCheckNAdd(int state) {
		if (jjrounds[state] != jjround) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = jjround;
		}
	}

	private final void jjAddStates(int start, int end) {
		do {
			jjstateSet[jjnewStateCnt++] = jjnextStates[start];
		} while (start++ != end);
	}

	private final void jjCheckNAddTwoStates(int state1, int state2) {
		jjCheckNAdd(state1);
		jjCheckNAdd(state2);
	}

	private final void jjCheckNAddStates(int start, int end) {
		do {
			jjCheckNAdd(jjnextStates[start]);
		} while (start++ != end);
	}

	private final void jjCheckNAddStates(int start) {
		jjCheckNAdd(jjnextStates[start]);
		jjCheckNAdd(jjnextStates[start + 1]);
	}

	private final int jjMoveNfa_18(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 1;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L)
							kind = 74;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_15() {
		return jjMoveNfa_15(0, 0);
	}

	static final long[] jjbitVec0 = { 0x1ff00000fffffffeL, 0xffffffffffffc000L,
			0xffffffffL, 0x600000000000000L };
	static final long[] jjbitVec2 = { 0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL };
	static final long[] jjbitVec3 = { 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL, 0xffffffffffffffffL };
	static final long[] jjbitVec4 = { 0xffffffffffffffffL, 0xffffffffffffffffL,
			0xffffL, 0x0L };
	static final long[] jjbitVec5 = { 0xffffffffffffffffL, 0xffffffffffffffffL,
			0x0L, 0x0L };
	static final long[] jjbitVec6 = { 0x3fffffffffffL, 0x0L, 0x0L, 0x0L };
	static final long[] jjbitVec7 = { 0xfffffffffffffffeL, 0xffffffffffffffffL,
			0xffffffffffffffffL, 0xffffffffffffffffL };
	static final long[] jjbitVec8 = { 0x0L, 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL };

	private final int jjMoveNfa_15(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 13;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffefffffffffL & l) != 0L) {
							if (kind > 62)
								kind = 62;
						} else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 11;
						if ((0x2400L & l) != 0L)
							jjCheckNAdd(1);
						else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 10;
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 1:
						if ((0x3ff200000000000L & l) != 0L)
							jjCheckNAddStates(0, 2);
						break;
					case 2:
						if ((0x100001200L & l) != 0L)
							jjCheckNAddTwoStates(2, 3);
						break;
					case 3:
						if ((0x100a00002400L & l) != 0L && kind > 61)
							kind = 61;
						break;
					case 4:
						if (curChar == 10)
							jjCheckNAdd(1);
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 6:
						if ((0xffffffefffffffffL & l) != 0L && kind > 62)
							kind = 62;
						break;
					case 8:
						if ((0x1000109e00000000L & l) != 0L && kind > 63)
							kind = 63;
						break;
					case 9:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 10:
						if (kind > 64)
							kind = 64;
						break;
					case 12:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 11;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffffefffffffL & l) != 0L) {
							if (kind > 62)
								kind = 62;
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 8;
						break;
					case 1:
						if ((0x7fffffe87fffffeL & l) != 0L)
							jjAddStates(0, 2);
						break;
					case 3:
						if (curChar == 92 && kind > 61)
							kind = 61;
						break;
					case 6:
						if ((0xffffffffefffffffL & l) != 0L && kind > 62)
							kind = 62;
						break;
					case 7:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 8;
						break;
					case 8:
						if ((0x2814404438000000L & l) != 0L && kind > 63)
							kind = 63;
						break;
					case 10:
						if ((0xf7ffffffffffffffL & l) != 0L && kind > 64)
							kind = 64;
						break;
					case 11:
						if (curChar == 123 && kind > 65)
							kind = 65;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 62)
							kind = 62;
						break;
					case 1:
						if (jjCanMove_0(hiByte, i1, i2, l1, l2))
							jjAddStates(0, 2);
						break;
					case 10:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 64)
							kind = 64;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 13 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_13() {
		return jjMoveNfa_13(0, 0);
	}

	private final int jjMoveNfa_13(int startState, int curPos) {

		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 9;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (kind > 56)
							kind = 56;
						if ((0x2400L & l) != 0L)
							jjCheckNAdd(1);
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 1:
						if ((0x3ff200000000000L & l) != 0L)
							jjCheckNAddStates(0, 2);
						break;
					case 2:
						if ((0x100001200L & l) != 0L)
							jjCheckNAddTwoStates(2, 3);
						break;
					case 3:
						if ((0x100a00002400L & l) != 0L && kind > 55)
							kind = 55;
						break;
					case 4:
						if (curChar == 10)
							jjCheckNAdd(1);
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 8:
						if (kind > 56)
							kind = 56;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (kind > 56)
							kind = 56;
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 1:
						if ((0x7fffffe87fffffeL & l) != 0L)
							jjAddStates(0, 2);
						break;
					case 3:
						if (curChar == 92 && kind > 55)
							kind = 55;
						break;
					case 6:
						if (curChar == 92 && kind > 56)
							kind = 56;
						break;
					case 7:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 8:
						if (kind > 56)
							kind = 56;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 56)
							kind = 56;
						break;
					case 1:
						if (jjCanMove_0(hiByte, i1, i2, l1, l2))
							jjAddStates(0, 2);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 9 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_10() {
		return jjMoveNfa_10(0, 0);
	}

	private final int jjMoveNfa_10(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 4;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xfffffffbffffdbffL & l) != 0L) {
							if (kind > 37)
								kind = 37;
						} else if (curChar == 34) {
							if (kind > 39)
								kind = 39;
						}
						break;
					case 2:
						if ((0x1000109e00000000L & l) != 0L && kind > 38)
							kind = 38;
						break;
					case 3:
						if (curChar == 34 && kind > 39)
							kind = 39;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffffefffffffL & l) != 0L) {
							if (kind > 37)
								kind = 37;
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 1:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 2:
						if ((0x2814404438000000L & l) != 0L && kind > 38)
							kind = 38;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 37)
							kind = 37;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_8() {
		return jjMoveNfa_8(0, 0);
	}

	private final int jjMoveNfa_8(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 8;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L) {
							if (kind > 46)
								kind = 46;
						} else if (curChar == 60)
							jjstateSet[jjnewStateCnt++] = 5;
						else if (curChar == 34) {
							if (kind > 47)
								kind = 47;
						} else if (curChar == 39) {
							if (kind > 31)
								kind = 31;
						}
						if (curChar == 60)
							jjCheckNAdd(4);
						break;
					case 1:
						if ((0x100001200L & l) != 0L)
							kind = 46;
						break;
					case 2:
						if (curChar == 34)
							kind = 47;
						break;
					case 3:
						if (curChar == 60)
							jjCheckNAdd(4);
						break;
					case 4:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 52)
							kind = 52;
						jjCheckNAdd(4);
						break;
					case 5:
						if (curChar == 60)
							jjCheckNAdd(6);
						break;
					case 6:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 58)
							kind = 58;
						jjCheckNAdd(6);
						break;
					case 7:
						if (curChar == 60)
							jjstateSet[jjnewStateCnt++] = 5;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 4:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 52)
							kind = 52;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 6:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 58)
							kind = 58;
						jjstateSet[jjnewStateCnt++] = 6;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 4:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 52)
							kind = 52;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 6:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 58)
							kind = 58;
						jjstateSet[jjnewStateCnt++] = 6;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 8 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_12() {
		return jjMoveNfa_12(14, 0);
	}

	private final int jjMoveNfa_12(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 45;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						if ((0x100001200L & l) == 0L)
							break;
						if (kind > 41)
							kind = 41;
						jjAddStates(3, 5);
						break;
					case 2:
						if ((0x2400L & l) != 0L && kind > 41)
							kind = 41;
						break;
					case 3:
						if (curChar == 10 && kind > 41)
							kind = 41;
						break;
					case 4:
					case 5:
						if (curChar == 13)
							jjCheckNAdd(3);
						break;
					case 16:
						if ((0x100001200L & l) == 0L)
							break;
						if (kind > 42)
							kind = 42;
						jjAddStates(6, 8);
						break;
					case 17:
						if ((0x2400L & l) != 0L && kind > 42)
							kind = 42;
						break;
					case 18:
						if (curChar == 10 && kind > 42)
							kind = 42;
						break;
					case 19:
					case 20:
						if (curChar == 13)
							jjCheckNAdd(18);
						break;
					case 28:
						if ((0x100001200L & l) == 0L)
							break;
						if (kind > 43)
							kind = 43;
						jjAddStates(9, 11);
						break;
					case 29:
						if ((0x2400L & l) != 0L && kind > 43)
							kind = 43;
						break;
					case 30:
						if (curChar == 10 && kind > 43)
							kind = 43;
						break;
					case 31:
					case 32:
						if (curChar == 13)
							jjCheckNAdd(30);
						break;
					case 37:
						if ((0x100001200L & l) == 0L)
							break;
						if (kind > 44)
							kind = 44;
						jjAddStates(12, 14);
						break;
					case 38:
						if ((0x2400L & l) != 0L && kind > 44)
							kind = 44;
						break;
					case 39:
						if (curChar == 10 && kind > 44)
							kind = 44;
						break;
					case 40:
					case 41:
						if (curChar == 13)
							jjCheckNAdd(39);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 14:
						if ((0x400000004000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 43;
						else if ((0x400000004L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 34;
						else if ((0x20000000200L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 25;
						else if ((0x1000000010000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 13;
						break;
					case 0:
						if ((0x8000000080000L & l) != 0L)
							jjAddStates(15, 17);
						break;
					case 6:
						if ((0x2000000020L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 0;
						break;
					case 7:
						if ((0x20000000200L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 8:
						if ((0x10000000100000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 9:
						if ((0x4000000040000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 8;
						break;
					case 10:
						if ((0x2000000020L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 9;
						break;
					case 11:
						if ((0x1000000010000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 12:
						if ((0x800000008000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 11;
						break;
					case 13:
						if ((0x4000000040000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 12;
						break;
					case 15:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(18, 20);
						break;
					case 21:
						if ((0x1000000010L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 15;
						break;
					case 22:
						if ((0x20000000200000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 21;
						break;
					case 23:
						if ((0x100000001000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 22;
						break;
					case 24:
						if ((0x800000008L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 23;
						break;
					case 25:
						if ((0x400000004000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 24;
						break;
					case 26:
						if ((0x20000000200L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 25;
						break;
					case 27:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(21, 23);
						break;
					case 33:
						if ((0x8000000080000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 27;
						break;
					case 34:
						if ((0x200000002L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 33;
						break;
					case 35:
						if ((0x400000004L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 34;
						break;
					case 36:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(24, 26);
						break;
					case 42:
						if ((0x200000002000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 36;
						break;
					case 43:
						if ((0x200000002L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 42;
						break;
					case 44:
						if ((0x400000004000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 43;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 45 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_3(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_3(int pos, long active0) {
		return jjMoveNfa_3(jjStopStringLiteralDfa_3(pos, active0), pos + 1);
	}

	private final int jjStopAtPos(int pos, int kind) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		return pos + 1;
	}

	private final int jjStartNfaWithStates_3(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_3(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_3() {
		switch (curChar) {
		case 44:
			return jjStopAtPos(0, 23);
		default:
			return jjMoveNfa_3(0, 0);
		}
	}

	private final int jjMoveNfa_3(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 6;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L) {
							if (kind > 46)
								kind = 46;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 4;
						else if ((0x2400L & l) != 0L) {
							if (kind > 24)
								kind = 24;
						}
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					case 1:
						if (curChar == 10 && kind > 24)
							kind = 24;
						break;
					case 2:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					case 3:
						if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 4:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 5:
						if ((0x100001200L & l) != 0L && kind > 46)
							kind = 46;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 4:
						kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 4:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_17(int pos, long active0,
			long active1) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_17(int pos, long active0, long active1) {
		return jjMoveNfa_17(jjStopStringLiteralDfa_17(pos, active0, active1),
				pos + 1);
	}

	private final int jjStartNfaWithStates_17(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_17(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_17() {
		switch (curChar) {
		case 92:
			return jjStartNfaWithStates_17(0, 71, 2);
		default:
			return jjMoveNfa_17(0, 0);
		}
	}

	private final int jjMoveNfa_17(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 10;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffefe5ffffdbffL & l) != 0L) {
							if (kind > 68)
								kind = 68;
						} else if ((0x100a00002400L & l) != 0L) {
							if (kind > 77)
								kind = 77;
						} else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 5;
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 8;
						else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 2:
						if ((0x1000109e00000000L & l) != 0L && kind > 69)
							kind = 69;
						break;
					case 3:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 4:
						if (kind > 70)
							kind = 70;
						break;
					case 6:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 5;
						break;
					case 7:
						if ((0x100a00002400L & l) != 0L && kind > 77)
							kind = 77;
						break;
					case 8:
						if (curChar == 10 && kind > 77)
							kind = 77;
						break;
					case 9:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 8;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffffefffffffL & l) != 0L) {
							if (kind > 68)
								kind = 68;
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 1:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 2:
						if ((0x2814404438000000L & l) != 0L && kind > 69)
							kind = 69;
						break;
					case 4:
						if ((0xf7ffffffffffffffL & l) != 0L && kind > 70)
							kind = 70;
						break;
					case 5:
						if (curChar == 123 && kind > 76)
							kind = 76;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 68)
							kind = 68;
						break;
					case 4:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 70)
							kind = 70;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 10 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_11() {
		return jjMoveNfa_11(0, 0);
	}

	private final int jjMoveNfa_11(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 8;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffebffffdbffL & l) != 0L) {
							if (kind > 48)
								kind = 48;
						} else if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 6;
						else if (curChar == 34) {
							if (kind > 39)
								kind = 39;
						}
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 5;
						break;
					case 1:
						if ((0xffffffebffffdbffL & l) != 0L && kind > 48)
							kind = 48;
						break;
					case 3:
						if ((0x1000109e00000000L & l) != 0L && kind > 49)
							kind = 49;
						break;
					case 4:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 5;
						break;
					case 5:
						if (kind > 50)
							kind = 50;
						break;
					case 7:
						if (curChar == 36)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffffefffffffL & l) != 0L) {
							if (kind > 48)
								kind = 48;
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 3;
						break;
					case 1:
						if ((0xffffffffefffffffL & l) != 0L && kind > 48)
							kind = 48;
						break;
					case 2:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 3;
						break;
					case 3:
						if ((0x2814404438000000L & l) != 0L && kind > 49)
							kind = 49;
						break;
					case 5:
						if ((0xf7ffffffffffffffL & l) != 0L && kind > 50)
							kind = 50;
						break;
					case 6:
						if (curChar == 123 && kind > 51)
							kind = 51;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 48)
							kind = 48;
						break;
					case 5:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 50)
							kind = 50;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 8 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_9(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_9(int pos, long active0) {
		return jjMoveNfa_9(jjStopStringLiteralDfa_9(pos, active0), pos + 1);
	}

	private final int jjStartNfaWithStates_9(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_9(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_9() {
		switch (curChar) {
		case 92:
			return jjStartNfaWithStates_9(0, 34, 2);
		default:
			return jjMoveNfa_9(0, 0);
		}
	}

	private final int jjMoveNfa_9(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 4;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffff7fffffdbffL & l) != 0L) {
							if (kind > 32)
								kind = 32;
						} else if (curChar == 39) {
							if (kind > 35)
								kind = 35;
						}
						break;
					case 2:
						if (curChar == 39 && kind > 33)
							kind = 33;
						break;
					case 3:
						if (curChar == 39 && kind > 35)
							kind = 35;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0xffffffffefffffffL & l) != 0L) {
							if (kind > 32)
								kind = 32;
						} else if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 1:
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 2:
						if (curChar == 92 && kind > 33)
							kind = 33;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 32)
							kind = 32;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_14() {
		return 1;
	}

	private final int jjMoveStringLiteralDfa0_16() {
		return 1;
	}

	private final int jjMoveStringLiteralDfa0_4() {
		return jjMoveNfa_4(0, 0);
	}

	private final int jjMoveNfa_4(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 6;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L) {
							if (kind > 72)
								kind = 72;
						} else if ((0x2400L & l) != 0L) {
							if (kind > 73)
								kind = 73;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 1;
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 1:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 2:
						if ((0x100001200L & l) != 0L && kind > 72)
							kind = 72;
						break;
					case 3:
						if ((0x2400L & l) != 0L && kind > 73)
							kind = 73;
						break;
					case 4:
						if (curChar == 10 && kind > 73)
							kind = 73;
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_6() {
		return jjMoveNfa_6(0, 0);
	}

	private final int jjMoveNfa_6(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 6;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L) {
							if (kind > 59)
								kind = 59;
						} else if ((0x2400L & l) != 0L) {
							if (kind > 60)
								kind = 60;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 1;
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 1:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 2:
						if ((0x100001200L & l) != 0L && kind > 59)
							kind = 59;
						break;
					case 3:
						if ((0x2400L & l) != 0L && kind > 60)
							kind = 60;
						break;
					case 4:
						if (curChar == 10 && kind > 60)
							kind = 60;
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_2(int pos, long active0,
			long active1) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_2(int pos, long active0, long active1) {
		return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0, active1),
				pos + 1);
	}

	private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_2(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_2() {
		switch (curChar) {
		case 46:
			return jjStopAtPos(0, 18);
		case 91:
			return jjStopAtPos(0, 19);
		case 93:
			return jjStopAtPos(0, 20);
		case 125:
			return jjStopAtPos(0, 80);
		default:
			return jjMoveNfa_2(0, 0);
		}
	}

	private final int jjMoveNfa_2(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 17;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x3ff200000000000L & l) != 0L) {
							if (kind > 17)
								kind = 17;
							jjCheckNAdd(2);
						} else if ((0x100001200L & l) != 0L) {
							if (kind > 78)
								kind = 78;
						} else if ((0x2400L & l) != 0L) {
							if (kind > 79)
								kind = 79;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 6;
						else if (curChar == 34) {
							if (kind > 36)
								kind = 36;
						} else if (curChar == 39) {
							if (kind > 30)
								kind = 30;
						} else if (curChar == 36)
							jjCheckNAdd(4);
						if ((0x3fe000000000000L & l) != 0L) {
							if (kind > 16)
								kind = 16;
							jjCheckNAdd(1);
						} else if (curChar == 48) {
							if (kind > 16)
								kind = 16;
							jjCheckNAddTwoStates(14, 16);
						} else if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 11;
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(1);
						break;
					case 2:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 3:
						if (curChar == 36)
							jjCheckNAdd(4);
						break;
					case 4:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjCheckNAdd(4);
						break;
					case 5:
						if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 6:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 7:
						if (curChar == 39 && kind > 30)
							kind = 30;
						break;
					case 8:
						if (curChar == 34 && kind > 36)
							kind = 36;
						break;
					case 9:
						if ((0x100001200L & l) != 0L && kind > 78)
							kind = 78;
						break;
					case 10:
						if ((0x2400L & l) != 0L && kind > 79)
							kind = 79;
						break;
					case 11:
						if (curChar == 10 && kind > 79)
							kind = 79;
						break;
					case 12:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 11;
						break;
					case 13:
						if (curChar != 48)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAddTwoStates(14, 16);
						break;
					case 15:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjstateSet[jjnewStateCnt++] = 15;
						break;
					case 16:
						if ((0xff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(16);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 6:
						if (kind > 27)
							kind = 27;
						break;
					case 14:
						if ((0x100000001000000L & l) != 0L)
							jjCheckNAdd(15);
						break;
					case 15:
						if ((0x7e0000007eL & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(15);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 6:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 17 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_5() {
		return jjMoveNfa_5(0, 0);
	}

	private final int jjMoveNfa_5(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 6;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x100001200L & l) != 0L) {
							if (kind > 53)
								kind = 53;
						} else if ((0x2400L & l) != 0L) {
							if (kind > 54)
								kind = 54;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 1;
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 1:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 2:
						if ((0x100001200L & l) != 0L && kind > 53)
							kind = 53;
						break;
					case 3:
						if ((0x2400L & l) != 0L && kind > 54)
							kind = 54;
						break;
					case 4:
						if (curChar == 10 && kind > 54)
							kind = 54;
						break;
					case 5:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 6 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjStopStringLiteralDfa_0(int pos, long active0) {
		switch (pos) {
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0) {
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}

	private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_0() {
		switch (curChar) {
		case 46:
			return jjStopAtPos(0, 18);
		case 61:
			return jjStopAtPos(0, 22);
		case 91:
			return jjStopAtPos(0, 19);
		case 93:
			return jjStopAtPos(0, 20);
		default:
			return jjMoveNfa_0(0, 0);
		}
	}

	private final int jjMoveNfa_0(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 20;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x3ff200000000000L & l) != 0L) {
							if (kind > 17)
								kind = 17;
							jjCheckNAdd(2);
						} else if ((0x100001200L & l) != 0L) {
							if (kind > 25)
								kind = 25;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 10;
						else if ((0x2400L & l) != 0L) {
							if (kind > 26)
								kind = 26;
						} else if (curChar == 34) {
							if (kind > 36)
								kind = 36;
						} else if (curChar == 39) {
							if (kind > 30)
								kind = 30;
						} else if (curChar == 36)
							jjCheckNAdd(4);
						if ((0x3fe000000000000L & l) != 0L) {
							if (kind > 16)
								kind = 16;
							jjCheckNAdd(1);
						} else if (curChar == 48) {
							if (kind > 16)
								kind = 16;
							jjCheckNAddTwoStates(17, 19);
						} else if (curChar == 33)
							jjCheckNAdd(13);
						else if (curChar == 35)
							jjCheckNAdd(13);
						else if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(1);
						break;
					case 2:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 3:
						if (curChar == 36)
							jjCheckNAdd(4);
						break;
					case 4:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjCheckNAdd(4);
						break;
					case 5:
						if ((0x100001200L & l) != 0L && kind > 25)
							kind = 25;
						break;
					case 6:
						if ((0x2400L & l) != 0L && kind > 26)
							kind = 26;
						break;
					case 7:
						if (curChar == 10 && kind > 26)
							kind = 26;
						break;
					case 8:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 9:
						if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 10:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 11:
						if (curChar == 39 && kind > 30)
							kind = 30;
						break;
					case 12:
						if (curChar == 34 && kind > 36)
							kind = 36;
						break;
					case 13:
						if (curChar == 33 && kind > 40)
							kind = 40;
						break;
					case 14:
						if (curChar == 35)
							jjCheckNAdd(13);
						break;
					case 15:
						if (curChar == 33)
							jjCheckNAdd(13);
						break;
					case 16:
						if (curChar != 48)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAddTwoStates(17, 19);
						break;
					case 18:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjstateSet[jjnewStateCnt++] = 18;
						break;
					case 19:
						if ((0xff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(19);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 10:
						if (kind > 27)
							kind = 27;
						break;
					case 17:
						if ((0x100000001000000L & l) != 0L)
							jjCheckNAdd(18);
						break;
					case 18:
						if ((0x7e0000007eL & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(18);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 10:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 20 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_7() {
		return jjMoveNfa_7(0, 0);
	}

	private final int jjMoveNfa_7(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 3;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x2400L & l) != 0L) {
							if (kind > 28)
								kind = 28;
						}
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					case 1:
						if (curChar == 10 && kind > 28)
							kind = 28;
						break;
					case 2:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	private final int jjMoveStringLiteralDfa0_1() {
		switch (curChar) {
		case 46:
			jjmatchedKind = 18;
			return jjMoveNfa_1(0, 0);
		case 61:
			jjmatchedKind = 22;
			return jjMoveNfa_1(0, 0);
		case 69:
			return jjMoveStringLiteralDfa1_1(0x4000L);
		case 85:
			return jjMoveStringLiteralDfa1_1(0x8000L);
		case 91:
			jjmatchedKind = 19;
			return jjMoveNfa_1(0, 0);
		case 93:
			jjmatchedKind = 20;
			return jjMoveNfa_1(0, 0);
		case 101:
			return jjMoveStringLiteralDfa1_1(0x4000L);
		case 117:
			return jjMoveStringLiteralDfa1_1(0x8000L);
		default:
			return jjMoveNfa_1(0, 0);
		}
	}

	private final int jjMoveStringLiteralDfa1_1(long active0) {
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 0);
		}
		switch (curChar) {
		case 69:
			return jjMoveStringLiteralDfa2_1(active0, 0x8000L);
		case 78:
			return jjMoveStringLiteralDfa2_1(active0, 0x4000L);
		case 101:
			return jjMoveStringLiteralDfa2_1(active0, 0x8000L);
		case 110:
			return jjMoveStringLiteralDfa2_1(active0, 0x4000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 1);
	}

	private final int jjMoveStringLiteralDfa2_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 1);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 1);
		}
		switch (curChar) {
		case 67:
			return jjMoveStringLiteralDfa3_1(active0, 0x4000L);
		case 83:
			return jjMoveStringLiteralDfa3_1(active0, 0x8000L);
		case 99:
			return jjMoveStringLiteralDfa3_1(active0, 0x4000L);
		case 115:
			return jjMoveStringLiteralDfa3_1(active0, 0x8000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 2);
	}

	private final int jjMoveStringLiteralDfa3_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 2);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 2);
		}
		switch (curChar) {
		case 67:
			if ((active0 & 0x8000L) != 0L) {
				jjmatchedKind = 15;
				jjmatchedPos = 3;
			}
			break;
		case 79:
			return jjMoveStringLiteralDfa4_1(active0, 0x4000L);
		case 99:
			if ((active0 & 0x8000L) != 0L) {
				jjmatchedKind = 15;
				jjmatchedPos = 3;
			}
			break;
		case 111:
			return jjMoveStringLiteralDfa4_1(active0, 0x4000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 3);
	}

	private final int jjMoveStringLiteralDfa4_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 3);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 3);
		}
		switch (curChar) {
		case 68:
			return jjMoveStringLiteralDfa5_1(active0, 0x4000L);
		case 100:
			return jjMoveStringLiteralDfa5_1(active0, 0x4000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 4);
	}

	private final int jjMoveStringLiteralDfa5_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 4);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 4);
		}
		switch (curChar) {
		case 73:
			return jjMoveStringLiteralDfa6_1(active0, 0x4000L);
		case 105:
			return jjMoveStringLiteralDfa6_1(active0, 0x4000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 5);
	}

	private final int jjMoveStringLiteralDfa6_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 5);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 5);
		}
		switch (curChar) {
		case 78:
			return jjMoveStringLiteralDfa7_1(active0, 0x4000L);
		case 110:
			return jjMoveStringLiteralDfa7_1(active0, 0x4000L);
		default:
			break;
		}
		return jjMoveNfa_1(0, 6);
	}

	private final int jjMoveStringLiteralDfa7_1(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjMoveNfa_1(0, 6);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return jjMoveNfa_1(0, 6);
		}
		switch (curChar) {
		case 71:
			if ((active0 & 0x4000L) != 0L) {
				jjmatchedKind = 14;
				jjmatchedPos = 7;
			}
			break;
		case 103:
			if ((active0 & 0x4000L) != 0L) {
				jjmatchedKind = 14;
				jjmatchedPos = 7;
			}
			break;
		default:
			break;
		}
		return jjMoveNfa_1(0, 7);
	}

	private final int jjMoveNfa_1(int startState, int curPos) {
		int strKind = jjmatchedKind;
		int strPos = jjmatchedPos;
		int seenUpto;
		input_stream.backup(seenUpto = curPos + 1);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			throw new Error("Internal Error");
		}
		curPos = 0;
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 17;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
						if ((0x3ff200000000000L & l) != 0L) {
							if (kind > 17)
								kind = 17;
							jjCheckNAdd(2);
						} else if ((0x100001200L & l) != 0L) {
							if (kind > 45)
								kind = 45;
						} else if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 9;
						else if ((0x2400L & l) != 0L) {
							if (kind > 24)
								kind = 24;
						} else if (curChar == 34) {
							if (kind > 36)
								kind = 36;
						} else if (curChar == 39) {
							if (kind > 30)
								kind = 30;
						} else if (curChar == 36)
							jjCheckNAdd(4);
						if ((0x3fe000000000000L & l) != 0L) {
							if (kind > 16)
								kind = 16;
							jjCheckNAdd(1);
						} else if (curChar == 48) {
							if (kind > 16)
								kind = 16;
							jjCheckNAddTwoStates(14, 16);
						} else if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 1:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(1);
						break;
					case 2:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 3:
						if (curChar == 36)
							jjCheckNAdd(4);
						break;
					case 4:
						if ((0x3ff200000000000L & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjCheckNAdd(4);
						break;
					case 5:
						if ((0x2400L & l) != 0L && kind > 24)
							kind = 24;
						break;
					case 6:
						if (curChar == 10 && kind > 24)
							kind = 24;
						break;
					case 7:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 8:
						if ((0xa00000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 9;
						break;
					case 9:
						if ((0xfffffffdffffffffL & l) != 0L && kind > 27)
							kind = 27;
						break;
					case 10:
						if (curChar == 39 && kind > 30)
							kind = 30;
						break;
					case 11:
						if (curChar == 34 && kind > 36)
							kind = 36;
						break;
					case 12:
						if ((0x100001200L & l) != 0L && kind > 45)
							kind = 45;
						break;
					case 13:
						if (curChar != 48)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAddTwoStates(14, 16);
						break;
					case 15:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjstateSet[jjnewStateCnt++] = 15;
						break;
					case 16:
						if ((0xff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(16);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 9:
						if (kind > 27)
							kind = 27;
						break;
					case 14:
						if ((0x100000001000000L & l) != 0L)
							jjCheckNAdd(15);
						break;
					case 15:
						if ((0x7e0000007eL & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(15);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int hiByte = (int) (curChar >> 8);
				int i1 = hiByte >> 6;
				long l1 = 1L << (hiByte & 077);
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 0:
					case 2:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 17)
							kind = 17;
						jjCheckNAdd(2);
						break;
					case 4:
						if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
							break;
						if (kind > 21)
							kind = 21;
						jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 9:
						if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 27)
							kind = 27;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 17 - (jjnewStateCnt = startsAt)))
				break;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				break;
			}
		}
		if (jjmatchedPos > strPos)
			return curPos;

		int toRet = Math.max(curPos, seenUpto);

		if (curPos < toRet)
			for (i = toRet - Math.min(curPos, seenUpto); i-- > 0;)
				try {
					curChar = input_stream.readChar();
				} catch (java.io.IOException e) {
					throw new Error(
							"Internal Error : Please send a bug report.");
				}

		if (jjmatchedPos < strPos) {
			jjmatchedKind = strKind;
			jjmatchedPos = strPos;
		} else if (jjmatchedPos == strPos && jjmatchedKind > strKind)
			jjmatchedKind = strKind;

		return toRet;
	}

	static final int[] jjnextStates = { 1, 2, 3, 1, 2, 4, 16, 17, 19, 28, 29,
			31, 37, 38, 40, 1, 5, 2, 16, 20, 17, 28, 32, 29, 37, 41, 38, };

	private static final boolean jjCanMove_0(int hiByte, int i1, int i2,
			long l1, long l2) {
		switch (hiByte) {
		case 0:
			return ((jjbitVec2[i2] & l2) != 0L);
		case 48:
			return ((jjbitVec3[i2] & l2) != 0L);
		case 49:
			return ((jjbitVec4[i2] & l2) != 0L);
		case 51:
			return ((jjbitVec5[i2] & l2) != 0L);
		case 61:
			return ((jjbitVec6[i2] & l2) != 0L);
		default:
			if ((jjbitVec0[i1] & l1) != 0L)
				return true;
			return false;
		}
	}

	private static final boolean jjCanMove_1(int hiByte, int i1, int i2,
			long l1, long l2) {
		switch (hiByte) {
		case 0:
			return ((jjbitVec8[i2] & l2) != 0L);
		default:
			if ((jjbitVec7[i1] & l1) != 0L)
				return true;
			return false;
		}
	}

	public static final String[] jjstrLiteralImages = { null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, "\56", "\133", "\135", null, "\75", "\54", null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null,
			null, };
	public static final String[] lexStateNames = { "DEFAULT", "IN_DIRECTIVE",
			"IN_REFERENCE", "IN_VALUES", "IN_SIMPLE_STRING_PRE_NEW_LINE",
			"IN_PRE_SQ_BLOCK", "IN_PRE_DQ_BLOCK", "IN_COMMENTS",
			"IN_PRE_VALUES", "IN_SQ_STRING", "IN_DQ_STRING", "IN_DQ_STRING_EX",
			"IN_PRE_DIRECTIVE", "IN_SQ_BLOCK", "IN_SQ_BLOCK_END",
			"IN_DQ_BLOCK", "IN_DQ_BLOCK_END", "IN_SIMPLE_STRING",
			"IN_SIMPLE_STRING_NEW_LINE", };
	public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 8, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, 18, -1, 17,
			-1, -1, -1, -1, -1, };
	static final long[] jjtoToken = { 0x2081e8801ffc001L, 0x3006L, };
	static final long[] jjtoSkip = { 0xfdf0e010fe000000L, 0x1cff9L, };
	static final long[] jjtoMore = { 0x7016700000000L, 0x0L, };
	private CharStream input_stream;
	private final int[] jjrounds = new int[45];
	private final int[] jjstateSet = new int[90];
	StringBuffer image;
	int jjimageLen;
	int lengthOfMatch;
	protected char curChar;

	public StandardParserTokenManager(CharStream stream) {
		input_stream = stream;
	}

	public StandardParserTokenManager(CharStream stream, int lexState) {
		this(stream);
		SwitchTo(lexState);
	}

	public void ReInit(CharStream stream) {
		jjmatchedPos = jjnewStateCnt = 0;
		curLexState = defaultLexState;
		input_stream = stream;
		ReInitRounds();
	}

	private final void ReInitRounds() {
		int i;
		jjround = 0x80000001;
		for (i = 45; i-- > 0;)
			jjrounds[i] = 0x80000000;
	}

	public void ReInit(CharStream stream, int lexState) {
		ReInit(stream);
		SwitchTo(lexState);
	}

	public void SwitchTo(int lexState) {
		if (lexState >= 19 || lexState < 0)
			throw new TokenMgrError("Error: Ignoring invalid lexical state : "
					+ lexState + ". State unchanged.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		else
			curLexState = lexState;
	}

	private final Token jjFillToken() {
		Token t = Token.newToken(jjmatchedKind);
		t.kind = jjmatchedKind;
		String im = jjstrLiteralImages[jjmatchedKind];
		t.image = (im == null) ? input_stream.GetImage() : im;
		t.beginLine = input_stream.getBeginLine();
		t.beginColumn = input_stream.getBeginColumn();
		t.endLine = input_stream.getEndLine();
		t.endColumn = input_stream.getEndColumn();
		return t;
	}

	int curLexState = 0;
	int defaultLexState = 0;
	int jjnewStateCnt;
	int jjround;
	int jjmatchedPos;
	int jjmatchedKind;

	public final Token getNextToken() {
		int kind;
		Token specialToken = null;
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				curChar = input_stream.BeginToken();
			} catch (java.io.IOException e) {
				jjmatchedKind = 0;
				matchedToken = jjFillToken();
				CommonTokenAction(matchedToken);
				return matchedToken;
			}
			image = null;
			jjimageLen = 0;

			for (;;) {
				switch (curLexState) {
				case 0:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_0();
					break;
				case 1:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_1();
					break;
				case 2:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_2();
					break;
				case 3:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_3();
					break;
				case 4:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_4();
					break;
				case 5:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_5();
					break;
				case 6:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_6();
					break;
				case 7:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_7();
					if (jjmatchedPos == 0 && jjmatchedKind > 29) {
						jjmatchedKind = 29;
					}
					break;
				case 8:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_8();
					if (jjmatchedPos == 0 && jjmatchedKind > 67) {
						jjmatchedKind = 67;
					}
					break;
				case 9:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_9();
					break;
				case 10:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_10();
					break;
				case 11:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_11();
					break;
				case 12:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_12();
					break;
				case 13:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_13();
					break;
				case 14:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_14();
					if (jjmatchedPos == 0 && jjmatchedKind > 57) {
						jjmatchedKind = 57;
					}
					break;
				case 15:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_15();
					break;
				case 16:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_16();
					if (jjmatchedPos == 0 && jjmatchedKind > 66) {
						jjmatchedKind = 66;
					}
					break;
				case 17:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_17();
					break;
				case 18:
					jjmatchedKind = 0x7fffffff;
					jjmatchedPos = 0;
					curPos = jjMoveStringLiteralDfa0_18();
					if (jjmatchedPos == 0 && jjmatchedKind > 75) {
						jjmatchedKind = 75;
					}
					break;
				}
				if (jjmatchedKind != 0x7fffffff) {
					if (jjmatchedPos + 1 < curPos)
						input_stream.backup(curPos - jjmatchedPos - 1);
					if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
						matchedToken = jjFillToken();
						TokenLexicalActions(matchedToken);
						if (jjnewLexState[jjmatchedKind] != -1)
							curLexState = jjnewLexState[jjmatchedKind];
						CommonTokenAction(matchedToken);
						return matchedToken;
					} else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
						SkipLexicalActions(null);
						if (jjnewLexState[jjmatchedKind] != -1)
							curLexState = jjnewLexState[jjmatchedKind];
						continue EOFLoop;
					}
					MoreLexicalActions();
					if (jjnewLexState[jjmatchedKind] != -1)
						curLexState = jjnewLexState[jjmatchedKind];
					curPos = 0;
					jjmatchedKind = 0x7fffffff;
					try {
						curChar = input_stream.readChar();
						continue;
					} catch (java.io.IOException e1) {
					}
				}
				int error_line = input_stream.getEndLine();
				int error_column = input_stream.getEndColumn();
				String error_after = null;
				boolean EOFSeen = false;
				try {
					input_stream.readChar();
					input_stream.backup(1);
				} catch (java.io.IOException e1) {
					EOFSeen = true;
					error_after = curPos <= 1 ? "" : input_stream.GetImage();
					if (curChar == '\n' || curChar == '\r') {
						error_line++;
						error_column = 0;
					} else
						error_column++;
				}
				if (!EOFSeen) {
					input_stream.backup(1);
					error_after = curPos <= 1 ? "" : input_stream.GetImage();
				}
				throw new TokenMgrError(EOFSeen, curLexState, error_line,
						error_column, error_after, curChar,
						TokenMgrError.LEXICAL_ERROR);
			}
		}
	}

	final void SkipLexicalActions(Token matchedToken) {
		switch (jjmatchedKind) {
		case 27:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			pushLexState(IN_COMMENTS);
			break;
		case 28:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			putbackNewLine();
			popLexState();
			break;
		case 30:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			pushLexState(IN_SQ_STRING);
			break;
		case 31:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			SwitchTo(IN_VALUES);
			pushLexState(IN_SQ_STRING);
			break;
		case 36:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			pushLexState(IN_DQ_STRING);
			break;
		case 47:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			SwitchTo(IN_VALUES);
			pushLexState(IN_DQ_STRING_EX);
			break;
		case 52:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			blockEndTag = image.substring(1);
			textBlock.setLength(0);
			SwitchTo(IN_VALUES);
			pushLexState(IN_PRE_SQ_BLOCK);
			break;
		case 54:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			startIndex = 1;
			SwitchTo(IN_SQ_BLOCK);
			break;
		case 55:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			if (image.substring(image.charAt(1) > ' ' ? 1 : 2,
					image.length() - 1).trim().equals(blockEndTag)) {
				if (textBlock.length() <= 0) {
					startIndex = 0;
				}
				SwitchTo(IN_SQ_BLOCK_END);
			} else {
				textBlock.append(image.substring(0, image.length() - 1));
			}
			break;
		case 56:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			textBlock.append(image.charAt(0));
			break;
		case 58:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			blockEndTag = image.substring(2);
			textBlock.setLength(0);
			SwitchTo(IN_VALUES);
			pushLexState(IN_PRE_DQ_BLOCK);
			break;
		case 60:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			startIndex = 1;
			SwitchTo(IN_DQ_BLOCK);
			break;
		case 61:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			if (image.substring(image.charAt(1) > ' ' ? 1 : 2,
					image.length() - 1).trim().equals(blockEndTag)) {
				if (textBlock.length() <= 0) {
					startIndex = 0;
				}
				SwitchTo(IN_DQ_BLOCK_END);
			} else {
				textBlock.append(image.substring(0, image.length() - 1));
			}
			break;
		case 62:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			textBlock.append(image.charAt(0));
			break;
		case 63:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			unescapeChar();
			textBlock.append(image.charAt(0));
			break;
		case 64:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			textBlock.append(image.charAt(0));
			break;
		case 67:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			textBlock.setLength(0);
			lastNonspaceIndex = 0;
			multipartString = false;
			SwitchTo(IN_VALUES);
			pushLexState(IN_SIMPLE_STRING);
			break;
		case 68:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			appendTextBlock(false);
			break;
		case 69:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			unescapeChar();
			appendTextBlock(true);
			break;
		case 70:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			image.setLength(image.length() - 1);
			appendTextBlock(false);
			break;
		case 75:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			textBlock.setLength(lastNonspaceIndex);
			if (lastNonspaceIndex == 0) {
				if (multipartString) {
					textBlock.append(' ');
				}
			} else {
				if (textBlock.charAt(lastNonspaceIndex - 1) > ' ') {
					textBlock.append(' ');
				}
			}
			break;
		case 80:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			popLexState();
			break;
		default:
			break;
		}
	}

	final void MoreLexicalActions() {
		jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
		switch (jjmatchedKind) {
		case 33:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen)));
			else
				image.append(input_stream.GetSuffix(jjimageLen));
			jjimageLen = 0;
			unescapeCharSimple();
			break;
		case 38:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen)));
			else
				image.append(input_stream.GetSuffix(jjimageLen));
			jjimageLen = 0;
			unescapeChar();
			break;
		case 40:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen)));
			else
				image.append(input_stream.GetSuffix(jjimageLen));
			jjimageLen = 0;
			pushLexState(IN_PRE_DIRECTIVE);
			break;
		case 49:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen)));
			else
				image.append(input_stream.GetSuffix(jjimageLen));
			jjimageLen = 0;
			unescapeChar();
			break;
		case 50:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen)));
			else
				image.append(input_stream.GetSuffix(jjimageLen));
			jjimageLen = 0;
			input_stream.backup(1);
			image.setLength(image.length() - 1);
			break;
		default:
			break;
		}
	}

	final void TokenLexicalActions(Token matchedToken) {
		switch (jjmatchedKind) {
		case 21:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = image.substring(1);
			break;
		case 22:
			if (image == null)
				image = new StringBuffer(jjstrLiteralImages[22]);
			else
				image.append(jjstrLiteralImages[22]);
			// DEFAULT状态碰到"=", 则调用求值过程.
			if (curLexState == DEFAULT) {
				pushLexState(IN_PRE_VALUES);
			}
			break;
		case 24:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			popLexState();
			break;
		case 35:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = image.substring(0, image.length() - 1);
			popLexState();
			break;
		case 39:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = image.substring(0, image.length() - 1);
			popLexState();
			break;
		case 41:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			putbackNewLine();
			SwitchTo(IN_DIRECTIVE);
			break;
		case 42:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			putbackNewLine();
			SwitchTo(IN_DIRECTIVE);
			break;
		case 43:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			putbackNewLine();
			SwitchTo(IN_DIRECTIVE);
			break;
		case 44:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			putbackNewLine();
			SwitchTo(IN_DIRECTIVE);
			break;
		case 51:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = image.substring(0, image.length()
					- lengthOfMatch);
			pushLexState(IN_REFERENCE);
			break;
		case 57:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			matchedToken.image = textBlock.substring(startIndex);
			popLexState();
			break;
		case 65:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = textBlock.substring(startIndex);
			startIndex = 0;
			textBlock.setLength(0);
			pushLexState(IN_REFERENCE);
			break;
		case 66:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			matchedToken.image = textBlock.substring(startIndex);
			startIndex = 0;
			popLexState();
			break;
		case 76:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			matchedToken.image = textBlock.toString();
			textBlock.setLength(0);
			lastNonspaceIndex = 0;
			multipartString = true;
			pushLexState(IN_REFERENCE);
			break;
		case 77:
			if (image == null)
				image = new StringBuffer(new String(input_stream
						.GetSuffix(jjimageLen
								+ (lengthOfMatch = jjmatchedPos + 1))));
			else
				image.append(input_stream.GetSuffix(jjimageLen
						+ (lengthOfMatch = jjmatchedPos + 1)));
			input_stream.backup(1);
			matchedToken.image = textBlock.substring(0, lastNonspaceIndex);
			popLexState();
			break;
		default:
			break;
		}
	}
}
