package com.avanse.model;

import java.util.List;

public class Collection {
	
	private String entity;
	private String count;
	private List<Order> items;
	
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<Order> getItems() {
		return items;
	}
	public void setItems(List<Order> items) {
		this.items = items;
	}

}
