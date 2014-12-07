package com.phoenix.common;

import java.math.BigDecimal;

public class CurrencyConvert {

	// 配置文件中中文单位的分隔符
	@SuppressWarnings("unused")
	private static final String SPLIT = ",";

	// 最大转换数值
	private static final int MAX_NUMBER = 10000000;
	/**
	 * 中文金额单位数组 String[]{"分", "角", "圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
	 * "拾", "佰", "仟"}
	 */
	private static final String[] chineseUnit = new String[] { "分", "角", "圆",
			"拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟" };

	/**
	 * 中文数字字符数组 String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"}
	 */
	private static final String[] chineseNumber = new String[] { "零", "壹", "贰",
			"叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	/**
	 * 中文描述 String[]{"整", "负"}
	 */
	private static final String[] chineseDesc = new String[] { "整", "负" };

	public CurrencyConvert() {

		// String unit =
		// TurbineResources.getString("order.currency.chinese.unit");
		// String number =
		// TurbineResources.getString("order.currency.chinese.number");
		// String desc =
		// TurbineResources.getString("order.currency.chinese.description");
		// chineseUnit = buildStringToArray(unit);
		// chineseNumber = buildStringToArray(number);
		// chineseDesc = buildStringToArray(desc);
	}

	/**
	 * 将数字金额转换为中文金额
	 * 
	 * @param bigdMoneyNumber
	 * @return
	 */
	public String numberToChineseCurrency(BigDecimal bigdMoneyNumber) {
		String strChineseCurrency = "";
		if (bigdMoneyNumber.intValue() < MAX_NUMBER) {
			// 零数位标记
			boolean bZero = true;
			// 中文金额单位下标
			int ChineseUnitIndex = 0;

			if (bigdMoneyNumber.intValue() == 0) {
				strChineseCurrency = chineseNumber[0] + chineseUnit[2]
						+ chineseDesc[0];
				// try {
				// strChineseCurrency = new
				// String(strChineseCurrency.getBytes(NirvanaCSConstant.CODEING_8859),
				// NirvanaCSConstant.CODEING_GBK);
				// } catch (UnsupportedEncodingException e) {
				// e.printStackTrace();
				// log.error("encoding error at
				// CurrencyConvert.numberToChineseCurrency ");
				// }
				return strChineseCurrency;
			}

			// 处理小数部分，四舍五入
			double doubMoneyNumber = Math
					.round(bigdMoneyNumber.doubleValue() * 100);

			// 是否负数
			boolean bNegative = doubMoneyNumber < 0;

			// 取绝对值
			doubMoneyNumber = Math.abs(doubMoneyNumber);

			// 循环处理转换操作
			while (doubMoneyNumber > 0) {
				// 整的处理(无小数位)
				if (ChineseUnitIndex == 2 && strChineseCurrency.length() == 0) {
					strChineseCurrency = strChineseCurrency + chineseDesc[0];
				}

				// 非零数位的处理
				if (doubMoneyNumber % 10 > 0) {
					strChineseCurrency = chineseNumber[(int) doubMoneyNumber % 10]
							+ chineseUnit[ChineseUnitIndex]
							+ strChineseCurrency;
					bZero = false;
				}
				// 零数位的处理
				else {
					// 元的处理(个位)
					if (ChineseUnitIndex == 2) {
						// 段中有数字
						if (doubMoneyNumber > 0) {
							strChineseCurrency = chineseUnit[ChineseUnitIndex]
									+ strChineseCurrency;
							bZero = true;
						}
					}
					// 万、亿数位的处理
					else if (ChineseUnitIndex == 6 || ChineseUnitIndex == 10) {
						// 段中有数字
						if (doubMoneyNumber % 1000 > 0) {
							strChineseCurrency = chineseUnit[ChineseUnitIndex]
									+ strChineseCurrency;
						}

					}
					// 前一数位非零的处理
					if (!bZero && ChineseUnitIndex != 6
							&& ChineseUnitIndex != 10) {
						strChineseCurrency = chineseNumber[0]
								+ strChineseCurrency;
					}
					bZero = true;
				}
				doubMoneyNumber = Math.floor(doubMoneyNumber / 10);
				ChineseUnitIndex++;
			}

			// 负数的处理
			if (bNegative) {
				strChineseCurrency = chineseDesc[1] + strChineseCurrency;
			}
			// try {
			// strChineseCurrency = new
			// String(strChineseCurrency.getBytes(NirvanaCSConstant.CODEING_8859),
			// NirvanaCSConstant.CODEING_GBK);
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// log.error("encoding error at
			// CurrencyConvert.numberToChineseCurrency ");
			// }
		}
		return strChineseCurrency;
	}

	/**
	 * 将字符串分隔成字符串数组返回
	 * 
	 * @param inStr
	 * @return
	 */
	// private String[] buildStringToArray(String inStr) {
	// Pattern pattern = Pattern.compile(SPLIT);
	// return pattern.split(inStr);
	// }
}