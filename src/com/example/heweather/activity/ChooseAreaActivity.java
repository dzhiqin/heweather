package com.example.heweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.heweather.R;
import com.example.heweather.db.DBHelper;
import com.example.heweather.model.Area;
import com.example.heweather.util.LogUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_DISTRICT=2;
	private int currentLevel;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private DBHelper dbHelper;
	private List<String>dataList=new ArrayList<String>();
	//ʡ�б�
	private List<Area> provinceList;
	//���б�
	private List<Area>cityList;
	//�����б�
	private List<Area>districtList;
	//֮ǰѡ�е�ʡ�ͳ���
	private Area selectedProvince;
	private Area selectedCity;

	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.local_text);
		//�趨һ��ListView��Ȼ��ͨ���ı�ListView����ʾ�������л�ʡ���С�������ʾ��
		//����adapter.notifyDataSetChanged();֪ͨˢ�����еĸĶ�
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		dbHelper=new DBHelper(this);
		listView.setAdapter(adapter);
		
		queryProvinces();//��ѯȫ��ʡ��
		//listview�����¼�
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(position);
					LogUtil.v("TAG", "the selected province is "+selectedProvince.getName());
					LogUtil.v("TAG", "code="+selectedProvince.getCode());
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(position);
					LogUtil.v("TAG", "the selected CITY is  "+selectedCity.getName());
					queryDistrict();
				}else if(currentLevel==LEVEL_DISTRICT){
					String districtName=districtList.get(position).getName();
					LogUtil.v("TAG", "the selected district is  "+districtName);
					Intent intent=new Intent(ChooseAreaActivity.this,MainActivity.class);
					intent.putExtra("districtName",districtName);
					startActivity(intent);
					
					finish();
				}
				
			}
		});
	}
	//��ѯȫ��ʡ
	private void queryProvinces(){
		LogUtil.v("TAG","queryProvinces from database");
		provinceList=dbHelper.getProvince();
		dataList.clear();
		for(Area province:provinceList){
			dataList.add(province.getName());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText("�й�");
		currentLevel=LEVEL_PROVINCE;
	}
	private void queryCities(){
		
		cityList=dbHelper.getCity(selectedProvince.getCode());
	
		dataList.clear();
		for(Area city:cityList){
			dataList.add(city.getName());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selectedProvince.getName());
		currentLevel=LEVEL_CITY;
	}
	private void queryDistrict(){
		
		districtList=dbHelper.getDistrict(selectedCity.getCode());
		
		dataList.clear();
		for(Area district:districtList){
			dataList.add(district.getName());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selectedCity.getName());
		currentLevel=LEVEL_DISTRICT;
	}
}






