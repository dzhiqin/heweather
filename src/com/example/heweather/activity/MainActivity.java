package com.example.heweather.activity;

import com.example.heweather.R;
import com.example.heweather.R.id;
import com.example.heweather.R.layout;
import com.example.heweather.R.menu;
import com.example.heweather.util.HttpCallbackListener;
import com.example.heweather.util.HttpUtil;
import com.example.heweather.util.LogUtil;
import com.example.heweather.util.Utility;

import android.app.Activity;
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
		String address="https://api.heweather.com/x3/weather?city="+localText.getText().toString()+"&key=dc908906531e4c38886eb3245eab890d";
		LogUtil.v("TAG", "address="+address);
		queryFromServer(address,"weather");
		
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.homeBtn:
			
			break;
		case R.id.refreshBtn:
			break;
		default:break;
		}
		
	}
}
