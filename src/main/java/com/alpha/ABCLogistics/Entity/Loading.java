package com.alpha.ABCLogistics.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
@Entity
public class Loading {
	@Id
	@SequenceGenerator(name = "loading_seq", sequenceName = "order_sequence_701", initialValue = 701, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loading_seq")
	private int id;
	private String date;
	private String time;
	@ManyToOne
	private Address address;
	public Loading() {
		super();
	}
	public Loading(int id, String date, String time, Address address) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
