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
	//public static final String DB_NAME="city.s3db";
	public static final String DB_NAME="CityId";
	public static final String PACKAGE_NAME="com.example.heweather";
	public static final String DB_PATH="/data"+Environment.getDataDirectory().getAbsolutePath()
			+"/"+PACKAGE_NAME;   //�ֻ��������ݿ��λ��
	private SQLiteDatabase database;
	private Context context;
	
	DBManager(Context context){
		this.context=context;
	}
	public SQLiteDatabase getDatabase(){
		return this.database;
	}
	public  void openDatabase(){
		this.database=this.openDatabase(DB_PATH+"/"+DB_NAME);
	}
	private SQLiteDatabase openDatabase(String dbfile){
		try{ 	//�ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬������ֱ�Ӵ�
			if(!new File(dbfile).exists()){
				InputStream is=this.context.getResources().openRawResource(R.raw.city);//Ҫ��������ݿ�
				FileOutputStream fos=new FileOutputStream(dbfile);
				byte[] buffer=new byte[BUFFER_SIZE];
				int count=0;
				while((count=is.read(buffer))>0){
					fos.write(buffer,0,count);
				}
				//���ϣ���city.s3db�ж�ȡ����Ȼ��д��dbfile�У�dbfile�洢��com.example.heweather
				fos.close();
				is.close();
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
