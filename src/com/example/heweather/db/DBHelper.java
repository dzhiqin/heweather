package com.example.heweather.db;

import java.util.ArrayList;

import com.example.heweather.model.Area;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public DBHelper(Context context){
		super();
		this.context=context;
		dbm=new DBManager(context);
	}
	public ArrayList<Area> getCity(String pcode){
		dbm.openDatabase();
		db=dbm.getDatabase();
		ArrayList<Area>list=new ArrayList<Area>();
		try{
			String sql="select * from city where pcode='"+pcode+"'";
			Cursor cursor=db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				do{
					String code=cursor.getString(cursor.getColumnIndex("code"));
					byte bytes[]=cursor.getBlob(2);
					String name=new String(bytes,"gbk");
					Area area =new Area();
					area.setName(name);
					area.setCode(code);
					area.setPcode(pcode);
					list.add(area);
				}while(cursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return list;
	}
	
	public ArrayList<Area> getProvince(){
		dbm.openDatabase();
		db=dbm.getDatabase();
		ArrayList<Area> list=new ArrayList<Area>();
		try{
			String sql="select * from province";
			Cursor cursor=db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				do{
					String code=cursor.getString(cursor.getColumnIndex("code"));
					byte bytes[]=cursor.getBlob(2);
					String name=new String(bytes,"gbk");
					Area area=new Area();
					area.setName(name);
					area.setCode(code);
					list.add(area);
					
				}while(cursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return list;
	}
	
	public ArrayList<Area>getDistrict(String pcode){
		dbm.openDatabase();
		db=dbm.getDatabase();
		ArrayList <Area> list=new ArrayList<Area>();
		try{
			String sql="select * from district where pcode='"+pcode+"'";
			Cursor cursor=db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				do{
					String code=cursor.getString(cursor.getColumnIndex("code"));
					byte bytes[]=cursor.getBlob(2);
					String name=new String(bytes,"gbk");
					Area area=new Area();
					area.setName(name);
					area.setPcode(code);
					list.add(area);
				}while(cursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		dbm.closeDatabase();
		db.close();
		return list;
	}
}
