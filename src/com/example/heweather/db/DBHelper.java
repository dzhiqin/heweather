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
			String sql="select * from city_id where pcode='"+pcode+"'";
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
	
	//��cityid.db���ݿ��ȡ����ƴ���б�
	public ArrayList<String> getPinYin(){
		dbm.openDatabase();
		db=dbm.getDatabase();
		ArrayList<String> list=new ArrayList<String>();
		//��ѯ��city_id�µ���city_spell_zh��������
		Cursor cursor=db.query("city_id", new String[]{"city_spell_zh"}, null,null,null,null,null);
		if(cursor.moveToFirst()){
			do{
				String pinyin=cursor.getString(cursor.getColumnIndex("city_spell_zh"));
				list.add(pinyin);
			}while(cursor.moveToNext());
		}
		dbm.closeDatabase();
		db.close();
		return list;
	}
	
	//��cityid.db���ݿ��ȡ���У�������ʡ��
	public ArrayList<String> getArea(){
		dbm.openDatabase();
		db=dbm.getDatabase();
		ArrayList<String> list=new ArrayList<String>();
		//��ѯcity_id���µ��� city_area��city_province
		Cursor cursor=db.query("city_id", new String[]{"city_area","city_province" }, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				String area=cursor.getString(cursor.getColumnIndex("city_area"))+","+cursor.getString(cursor.getColumnIndex("city_province"));
				list.add(area);
			}while(cursor.moveToNext());
		}
		dbm.closeDatabase();
		db.close();
		return list;
	}
}
