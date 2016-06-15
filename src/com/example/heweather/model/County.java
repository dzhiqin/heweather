package com.example.heweather.model;

public class County {

	private int id;
	private String countyName;
	private String countyId;
	private String countyPY;
	private String countyCity;
	private String countyProvince;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getCountyId(){
		return countyId;
	}
	public void setCountyId(String countyId){
		this.countyId=countyId;
	}
	public String getCountyPY(){
		return countyPY;
	}
	public void setCountyPY(String countyPY){
		this.countyPY=countyPY;
	}
	public String getCountyName(){
		return countyName;
	}
	public void setCountyName(String countyName){
		this.countyName=countyName;
	}
	public String getCountyCity(){
		return countyCity;
	}
	public void setCountyCity(String countyCity){
		this.countyCity=countyCity;
	}
	public String getCountyProvince(){
		return countyProvince;
	}
	public void setCountyProvince(String countyProvince){
		this.countyProvince=countyProvince;
	}
}
