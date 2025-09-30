package com.alpha.ABCLogistics.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Carrier {
	@Id
	private int id;
	private String name;
	private String mail;
	private long contact;

	public Carrier() {
	}

	public Carrier(int id, String name, String mail, long contact) {
		this.id = id;
		this.name = name;
		this.mail = mail;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public long getContact() {
		return contact;
	}

	public void setContact(long contact) {
		this.contact = contact;
	}
}
