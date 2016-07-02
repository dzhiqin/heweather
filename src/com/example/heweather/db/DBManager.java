package com.example.heweather.db;

import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.heweather.R;
import com.example.heweather.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManager {
	private final int BUFFER_SIZE=1024;
	public final static int CHOOSE_DB=2;
	public static final String DB_NAME1="city.s3db";//省，市，区 三张表
	public static final String DB_NAME2="cityid";//和风天气数据库 主要是一张city_id表，包含全国各城市地区
	public static final String PACKAGE_NAME="com.example.heweather";
	public static final String DB_PATH="/data"+Environment.getDataDirectory().getAbsolutePath()
			+"/"+PACKAGE_NAME;   //手机里存放数据库的位置
	private SQLiteDatabase database;
	private Context context;
	
	DBManager(Context context){
		this.context=context;
	}
	public SQLiteDatabase getDatabase(){
		return this.database;
	}
	@SuppressWarnings("unused")
	public  void openDatabase(){
		if(CHOOSE_DB==1){
			this.database=this.openDatabase(DB_PATH+"/"+DB_NAME1);
		}else {
			this.database=this.openDatabase(DB_PATH+"/"+DB_NAME2);
		}
	}
	@SuppressWarnings("unused")
	private SQLiteDatabase openDatabase(String dbfile){
		try{ 	//判断数据库文件是否存在，若不存在则执行导入，存在则直接打开
			if(!new File(dbfile).exists()){
				if(CHOOSE_DB==1){
					InputStream is=this.context.getResources().openRawResource(R.raw.city);//要导入的数据库city.s3db
					FileOutputStream fos=new FileOutputStream(dbfile);
					byte[] buffer=new byte[BUFFER_SIZE];
					int count=0;
					while((count=is.read(buffer))>0){
						fos.write(buffer,0,count);
					}
					//以上，从city.s3db中读取数据然后写入dbfile中，dbfile存储在com.example.heweather
					fos.close();
					is.close();
				}else{
					InputStream is=this.context.getResources().openRawResource(R.raw.cityid);//要导入的数据库cityid
					FileOutputStream fos=new FileOutputStream(dbfile);
					byte[] buffer=new byte[BUFFER_SIZE];
					int count=0;
					while((count=is.read(buffer))>0){
						fos.write(buffer,0,count);
					}
					//以上，从cityid中读取数据然后写入dbfile中，dbfile存储在com.example.heweather
					fos.close();
					is.close();
				}
				
				
			}
				SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbfile, null);
				return db;
			}catch(FileNotFoundException e){
				LogUtil.e("TAG","FILE NOT FOUND");
				e.printStackTrace();
			}catch(IOException e){
				LogUtil.e("TAG","IOException");
				e.printStackTrace();
			}
			return null;		
	}
	public void closeDatabase(){
		this.database.close();
	}
}
