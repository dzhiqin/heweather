package com.example.heweather.model;

public class Area {

	private String name;
	private String code;
	private String pcode;
	
	public void setName(String name){
		this.name=name;
	}
	public  String getName(){
		return name;
	}
	public void setCode(String code){
		this.code=code;
	}
	public String getCode(){
		return code;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	} 
	public String getPcode(){
		return pcode;
	}
	public Area(){
		super();
	}
	public String toString(){
		return "Area [code="+code+",name="+name+",pcode="+pcode+"]";
	}
}
