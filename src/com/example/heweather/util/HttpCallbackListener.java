package com.example.heweather.util;

public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);

}
