package com.example.heweather.activity;

import com.example.heweather.R;
import com.example.heweather.R.id;
import com.example.heweather.R.layout;
import com.example.heweather.R.menu;
import com.example.heweather.db.DBHelper;
import com.example.heweather.db.DBManager;
import com.example.heweather.service.AutoUpdateService;
import com.example.heweather.util.HttpCallbackListener;
import com.example.heweather.util.HttpUtil;
import com.example.heweather.util.LogUtil;
import com.example.heweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private Button homeBtn;
	private Button refreshBtn;
	private TextView localText;
	private TextView publishTimeText;
	private TextView dateText;
	private TextView despText;
	private TextView tempText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		homeBtn=(Button)findViewById(R.id.homeBtn);
		refreshBtn=(Button)findViewById(R.id.refreshBtn);
		localText=(TextView)findViewById(R.id.localText);
		publishTimeText=(TextView)findViewById(R.id.publishTimeText);
		dateText=(TextView)findViewById(R.id.dateText);
		despText=(TextView)findViewById(R.id.despText);
		tempText=(TextView)findViewById(R.id.tempText);
		homeBtn.setOnClickListener(this);
		refreshBtn.setOnClickListener(this);
		
		//和风天气获取：https://api.heweather.com/x3/weather?city=石狮&key=dc908906531e4c38886eb3245eab890d
		//city=石狮市获取不到天气
		//和风天气获取：https://api.heweather.com/x3/weather?cityid=CN101010100&key=dc908906531e4c38886eb3245eab890d
		//用city.s3db无法获得和风天气需要的cityid码，市区名称也无法对应
		/*String districtCode=getIntent().getStringExtra("districtCode");
		if(districtCode!=null){
			String address="https://api.heweather.com/x3/weather?cityid="+districtCode+"&key=dc908906531e4c38886eb3245eab890d";
			LogUtil.v("TAG", "address="+address);
			queryFromServer(address,"weather");
		}else{
			showWeather();
		}*/
		//查看districtName是否已经选择了，如果选择了就更新一次数据
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String districtName=prefs.getString("cityName", "unknown");
		LogUtil.v("TAG", "prefs get the selected district:"+districtName);
		//String districtName=getIntent().getStringExtra("districtName");
		//LogUtil.v("TAG", "MainActivity get distrctName="+districtName);
		if(districtName!=null){
			String address="https://api.heweather.com/x3/weather?city="+districtName+"&key=dc908906531e4c38886eb3245eab890d";
			LogUtil.v("TAG", "address="+address);
			queryFromServer(address,"weather");
		}else{
			showWeather();
		}
		
		
		
	}

	protected void onResume(){
		super.onResume();
		
	}
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				if("weather".equals(type)){
					if(!TextUtils.isEmpty(response)){
						Utility.handleWeatherResponse(MainActivity.this,response);
						MainActivity.this.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								showWeather();
								
							}
							
						});
					}
				}
				
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						publishTimeText.setText("同步失败");
					}
					
				});
			}
			
		});
		
	}

	private void showWeather() {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		localText.setText(prefs.getString("cityName", "未知"));
		publishTimeText.setText(prefs.getString("publishTime", "未知"	));
		dateText.setText(prefs.getString("todayDate", "未知"));
		tempText.setText(prefs.getString("todayMin","min")+"~"+prefs.getString("todayMax", "max"));
		despText.setText(prefs.getString("todayDesp", ""));
		
		//启动一个服务，用于定时更新天气
		Intent i=new Intent(this,AutoUpdateService.class);
		startService(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unused")
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.homeBtn:
			if(DBManager.CHOOSE_DB==1){//选择ChoseAreaActivity使用数据库city.s3db
				Intent intent=new Intent(MainActivity.this,ChooseAreaActivity.class);
				startActivity(intent);
				finish();
			}else{//选择SearchAreaActivity使用数据库city.db
				Intent intent=new Intent(MainActivity.this,SearchAreaActivity.class);
				startActivity(intent);
				finish();
			}			
			break;
		case R.id.refreshBtn:
			publishTimeText.setText("同步失败");
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			String districtName=prefs.getString("cityName", "unknown");
			String address="https://api.heweather.com/x3/weather?city="+districtName+"&key=dc908906531e4c38886eb3245eab890d";
			queryFromServer(address,"weather");
			break;
		default:break;
		}
		
	}
}
