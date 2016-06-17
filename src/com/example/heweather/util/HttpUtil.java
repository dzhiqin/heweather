package com.example.heweather.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static  void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					HttpClient client=new DefaultHttpClient();
					HttpGet httpGet=new HttpGet(address);
					HttpResponse httpResponse=client.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode()==200){
						//请求和响应都成功了 
						HttpEntity entity=httpResponse.getEntity();
						String response=EntityUtils.toString(entity,"utf-8");
						LogUtil.v("TAG", "response="+response);
						if(listener!=null){
							listener.onFinish(response);
						}
					}
				}catch(ClientProtocolException e){
					if(listener!=null){
						listener.onError(e);
					}
				}catch(IOException e){
					if(listener!=null){
						listener.onError(e);
					}
				}
				
			}
			
		}).start();
	}
}
