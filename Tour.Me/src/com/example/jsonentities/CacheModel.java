package com.example.jsonentities;

public class CacheModel {
     private int id;
     private String name;
     private String data;
     
	
	public CacheModel(int id, String name, String data) {
		super();
		this.id = id;
		this.name = name;
		this.data = data;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
