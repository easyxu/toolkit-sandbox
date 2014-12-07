package com.phoenix.test.dal.hibernate4.objects;

import java.math.BigDecimal;

public class Vo {

	private String name;
	private String cardName;
	private BigDecimal amount;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Vo [name=" + name + ", cardName=" + cardName + ", amount="
				+ amount + "]";
	}
}
