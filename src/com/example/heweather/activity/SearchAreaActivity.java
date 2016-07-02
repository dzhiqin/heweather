package com.example.heweather.activity;

import java.util.ArrayList;

import com.example.heweather.R;
import com.example.heweather.db.DBHelper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchAreaActivity extends Activity {

	private EditText searchEditText;
	private ImageView deleteImageView;
	private ListView mListView;
	private DBHelper dbHelper;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> pinyinList;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_area);
		dbHelper=new DBHelper(this);//实例化dbHelper
		mListView=(ListView)findViewById(R.id.listView);
		deleteImageView=(ImageView)findViewById(R.id.delete_imageView);
		searchEditText=(EditText)findViewById(R.id.search_editText);
		
		searchEditText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()==0){
					deleteImageView.setVisibility(View.GONE);
					
				}else{
					deleteImageView.setVisibility(View.VISIBLE);
				}
				
			}
			
		});
		
		//
		deleteImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				
			}
		});
		
		//获得pinyinList
		pinyinList=dbHelper.getPinYin();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pinyinList);
		mListView.setAdapter(adapter);
	}

}
