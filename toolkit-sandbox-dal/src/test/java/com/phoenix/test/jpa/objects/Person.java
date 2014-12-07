package com.phoenix.test.jpa.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
