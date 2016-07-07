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
	private ArrayList<String> subDataList;//������ʾ��Edittext��search����ƥ�������
	private Handler handler;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_area);
		dbHelper=new DBHelper(this);//ʵ����dbHelper
		handler=new Handler();
		subDataList=new ArrayList<String>();
		mListView=(ListView)findViewById(R.id.listView);
		deleteImageView=(ImageView)findViewById(R.id.delete_imageView);
		searchEditText=(EditText)findViewById(R.id.search_editText);
		
		searchEditText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO �Զ����ɵķ������
				
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

		//mListView�����¼�
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast��ʾѡ��ĵ���
				Toast.makeText(SearchAreaActivity.this, subDataList.get(position), Toast.LENGTH_SHORT).show();
				String[] districtProvince=subDataList.get(position).split(",");
				String districtName=districtProvince[0];
				//��districtName���浽prefs,��MainActivity���ж�districtName�Ƿ���ڣ����ھ͸������ݣ�����ÿ�δ�����������
				SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(SearchAreaActivity.this).edit();
				editor.putString("cityName", districtName);
				
				editor.commit();
				Intent intent=new Intent(SearchAreaActivity.this,MainActivity.class);				
				startActivity(intent);
				finish();//���������activity�󣬽�����activity
			}
		});
		//deleteImageView�����¼�
		deleteImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				
			}
		});
		
		//���pinyinList,areaList,���ں�searchEditTextƥ��
		pinyinList=dbHelper.getPinYin();
		areaList=dbHelper.getArea();
		//���subDataList ������������ʡ��������ʾ�������listview��
		subDataList=dbHelper.getArea();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subDataList);
		mListView.setAdapter(adapter);
	}

	
	//��searchEditText������ʱ�ͽ����������
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
