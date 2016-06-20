package com.example.heweather.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utility {

	public static void handleWeatherResponse(Context context,String response){
		LogUtil.v("TAG", "Utility_handleweatherResponse");
		try{			
			JSONObject jsonObject=new JSONObject(response);
			
			JSONArray weatherArray=jsonObject.getJSONArray("HeWeather data service 3.0");
			JSONObject weatherInfo=weatherArray.getJSONObject(0);
			JSONArray dailyForecast=weatherInfo.getJSONArray("daily_forecast");
			
			JSONObject todayInfo=dailyForecast.getJSONObject(0);
			String todayDate=todayInfo.getString("date");//��õ�ǰ����			
			JSONObject temp=todayInfo.getJSONObject("tmp");
			String todayMax=temp.getString("max");//��õ�������º������
			String todayMin=temp.getString("min");
			JSONObject cond=todayInfo.getJSONObject("cond");
			String todayDesp=cond.getString("txt_d");//��ð�����������
			
			JSONObject basic=weatherInfo.getJSONObject("basic");
			String cityName=basic.getString("city");//��ó�����
			JSONObject update=basic.getJSONObject("update");
			String publishTime=update.getString("loc");//��ø���ʱ��
			LogUtil.v("TAG", "date="+todayDate+"weather="+todayDesp);
			SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		
			editor.putString("todayDate", todayDate);
			editor.putString("todayMax", todayMax);
			editor.putString("todayMin", todayMin);
			editor.putString("todayDesp", todayDesp);
			editor.putString("cityName", cityName);
			editor.putString("publishTime", publishTime);
			editor.commit();
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
}
