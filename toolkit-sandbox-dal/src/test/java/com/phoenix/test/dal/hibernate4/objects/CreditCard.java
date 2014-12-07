package com.phoenix.test.dal.hibernate4.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="creditCard")
public class CreditCard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2916075851172912468L;

	
	private Integer id;
	private String name;
	private BigDecimal amount;
	private Person person;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	@Override
	public String toString() {
		return "CreditCard [id=" + id + ", name=" + name + ", amount=" + amount
				+ "]";
	}
}
