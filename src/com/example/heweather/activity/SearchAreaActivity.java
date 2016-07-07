package com.example.heweather.activity;

import java.util.ArrayList;

import com.example.heweather.R;
import com.example.heweather.db.DBHelper;
import com.example.heweather.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class SearchAreaActivity extends Activity {

	private EditText searchEditText;
	private ImageView deleteImageView;
	private ListView mListView;
	private DBHelper dbHelper;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> pinyinList;
	private ArrayList<String> areaList;
	private ArrayList<String> subDataList;//用于显示从Edittext上search到的匹配的内容
	private Handler handler;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_area);
		dbHelper=new DBHelper(this);//实例化dbHelper
		handler=new Handler();
		subDataList=new ArrayList<String>();
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
				handler.post(eChanged);	
			}
			
		});

		//mListView监听事件
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast显示选择的地区
				Toast.makeText(SearchAreaActivity.this, subDataList.get(position), Toast.LENGTH_SHORT).show();
				String[] districtProvince=subDataList.get(position).split(",");
				String districtName=districtProvince[0];
				//把districtName保存到prefs,在MainActivity里判断districtName是否存在，存在就更新数据，这样每次打开软件都会更新
				SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(SearchAreaActivity.this).edit();
				editor.putString("cityName", districtName);
				
				editor.commit();
				Intent intent=new Intent(SearchAreaActivity.this,MainActivity.class);				
				startActivity(intent);
				finish();//当跳到别的activity后，结束本activity
			}
		});
		//deleteImageView监听事件
		deleteImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				
			}
		});
		
		//获得pinyinList,areaList,用于和searchEditText匹配
		pinyinList=dbHelper.getPinYin();
		areaList=dbHelper.getArea();
		//获得subDataList 包含地区名和省名，并显示在最初的listview里
		subDataList=dbHelper.getArea();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subDataList);
		mListView.setAdapter(adapter);
	}

	
	//当searchEditText有输入时就进入这个程序
	Runnable eChanged=new Runnable(){

		@Override
		public void run() {
			String area=searchEditText.getText().toString();
			subDataList.clear();
			int length=pinyinList.size();
			LogUtil.v("TAG", "Length="+length);
			for(int i=0;i<length;i++){
				if(pinyinList.get(i).contains(area)||areaList.get(i).contains(area)){
					subDataList.add(areaList.get(i));
				}
			}
			adapter.notifyDataSetChanged();
			
		}
		
	};
}
