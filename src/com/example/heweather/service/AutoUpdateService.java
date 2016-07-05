package com.example.heweather.service;

import java.util.Calendar;

import com.example.heweather.receiver.AutoUpdateReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AutoUpdateService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	public int onStartCommand(Intent intent,int flags,int startId){
		//设置一个循环的闹钟
		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar calendar=Calendar.getInstance();
		//把calendar实例设置到当前的系统时间
		calendar.setTimeInMillis(System.currentTimeMillis());
		//预设定一个PendingIntent
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		//闹钟循环的时间设定为5小时
		alarm.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000*60*60*5, pi);
		return super.onStartCommand(intent,flags,startId);
	}
}
