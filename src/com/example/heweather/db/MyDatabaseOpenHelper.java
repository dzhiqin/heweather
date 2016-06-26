package com.example.heweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper{


	public static final String CREATE_COUNTY="create table County("
			+ "id integer primary key autoincrement, "
			+ "county_id text, "
			+ "county_py text, "
			+ "county_name text, "
			+ "county_city text, "
			+ "county_province text, )";
	public static final String CERTIFICATION="dc908906531e4c38886eb3245eab890d";
	public MyDatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_COUNTY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������
		
	}

}
