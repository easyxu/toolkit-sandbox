package com.phoenix.test.dal.hibernate4.objects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class Person implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8301907701605862244L;

	private Integer id;
	private String name;
	private Set<CreditCard> card = new HashSet<>(0);
	
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
	
	@OneToMany(mappedBy="person",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Set<CreditCard> getCard() {
		return card;
	}
	public void setCard(Set<CreditCard> card) {
		this.card = card;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + "]";
	}

}
