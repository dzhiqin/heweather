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
		// TODO �Զ����ɵķ������
		return null;
	}

	public int onStartCommand(Intent intent,int flags,int startId){
		//����һ��ѭ��������
		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
		Calendar calendar=Calendar.getInstance();
		//��calendarʵ�����õ���ǰ��ϵͳʱ��
		calendar.setTimeInMillis(System.currentTimeMillis());
		//Ԥ�趨һ��PendingIntent
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		//����ѭ����ʱ���趨Ϊ5Сʱ
		alarm.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), 1000*60*60*5, pi);
		return super.onStartCommand(intent,flags,startId);
	}
}
