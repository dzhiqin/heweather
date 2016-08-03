package com.example.heweather;


import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.heweather.util.LogUtil;
import com.example.heweather.util.Utility;
import com.example.heweather.activity.MainActivity;
import com.example.heweather.interfaces.*;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;

public class PullableViewLayout extends LinearLayout implements View.OnTouchListener{
	/**
	 * 下拉可以刷新
	 */
	public static final int STATUS_PULL_TO_REFRESH=0;
	/**
	 * 释放立即刷新
	 */
	public static final int STATUS_RELEASE_TO_REFRESH=1;
	/**
	 * 正在刷新
	 */
	public static final int STATUS_REFRESHING=2;
	/**
	 * 刷新完成或未刷新
	 */
	public static final int STATUS_REFRESH_FINISHED=3;
	/**
	 * 下拉头回滚的速度
	 */
	public static final int SCROLL_SPEED=-20;
	/**
	 * 为了防止不同界面的下拉刷新在更新时间上相互冲突，使用id来做区分
	 */
	private int mId=-1;
	/**
	 * 过滤多点触控
	 */
	private int mEvent=-1;
	/**
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;
	/**
	 * 当前处于什么状态，可选值有STATUS_PULL_TO_REFRESH,STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING,STATUS_REFRESH_FINISHED
	 */
	private int currentStatus=STATUS_REFRESH_FINISHED;
	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private int lastStatus=currentStatus;
	/**
	 * 手指按下时的屏幕纵坐标
	 */
	private float yDown;
	/**
	 * 被判定为滚动之前用户手指可以移动的最大值，消抖
	 */
	private int touchSlop;
	/**
	 * 是否已经加载过一次，用于在onLayout中进行初始化
	 */
	private boolean loadOnce;

	/**
	 * 下拉箭头转动180°动画
	 */
	//改为用java程序表示
	//private RotateAnimation rotateAnimation;
	/**
	 * 绕圈旋转动画
	 */
	//改为用progressBar，不需要用动画来显示
	//private RotateAnimation refreshingAnimation;
	/**
	 * 下拉头的view
	 */
	private View header;
	/**
	 * 实现了pullable接口的View
	 */
	private View pullableView;
	/**
	 * 刷新时显示的进度条
	 */
	private ProgressBar progressBar;
	/**
	 * 指示下拉和释放的箭头图标
	 */
	private ImageView arrow;
	/**
	 * 指示下拉和释放的文字描述
	 */
	private TextView description;
	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;
	/**
	 * 下拉刷新的回调接口
	 */
	private PullToRefreshListener mListener;
	/**
	 * 下拉刷新控件的构造函数
	 */

	public PullableViewLayout(Context context){
		super(context);
		touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
	}
	public PullableViewLayout(Context context,AttributeSet attrs){
		super(context,attrs);
		/**
		 * 箭头翻转和刷新圆圈的动画
		 */
		//rotateAnimation=(RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
		//refreshingAnimation=(RotateAnimation)AnimationUtils.loadAnimation(context, R.anim.refreshing_anim);
		/**
		 * 设置动画匀速运动
		 */
		//LinearInterpolator lir=new LinearInterpolator();
		//rotateAnimation.setInterpolator(lir);
		//refreshingAnimation.setInterpolator(lir);
		touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();		
	}
	public PullableViewLayout(Context context ,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();	
	}
	/**
	 * 进行一次初始化操作，将下拉头向上偏移，给pullableView注册touch事件
	 */
	@Override
	protected void onLayout(boolean changed,int l,int t,int r,int b){
		super.onLayout(changed, l, t, r, b);
		//第一次加载onLayout进入
		if(!loadOnce){
			header=getChildAt(0);
			LogUtil.v("test", "header="+header);
			pullableView=getChildAt(1);
			LogUtil.v("test", "pullableView="+pullableView);
			//设置为负数，是为了隐藏下拉头
			hideHeaderHeight=-header.getHeight();
			headerLayoutParams=(MarginLayoutParams)header.getLayoutParams();
			//可以看到topMargin也是负数，也就是说”控件到顶部的距离“这个值并不是取绝对值，这也可以理解，因为正负号可以代表方位
			headerLayoutParams.topMargin=hideHeaderHeight;
			progressBar=(ProgressBar)header.findViewById(R.id.progress_bar);
			arrow=(ImageView)header.findViewById(R.id.arrow_icon);
			description=(TextView)header.findViewById(R.id.description_tv);
			//为pullableView设置触摸监听事件
			pullableView.setOnTouchListener(this);
			loadOnce=true;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN://触摸检测
			LogUtil.v("test", "ACTION_DOWN");
			yDown=event.getRawY();
			mEvent=0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			//LogUtil.v("test", "ACTION_POINTER_DOWN OR UP");
			//过滤多点触碰
			mEvent=-1;
			break;
		case MotionEvent.ACTION_MOVE://移动检测
			LogUtil.v("test", "ACTION_MOVE");
			
			if(mEvent==0&&((Pullable)pullableView).canPullDown())
			{
				float yMove=event.getRawY();
				int distance=(int)(yMove-yDown);
				//去除微小的抖动误差
				if(distance<touchSlop){					
					return false;
				}
				if(currentStatus!=STATUS_REFRESHING){
					//已经在下拉状态
					if(headerLayoutParams.topMargin>0){
						//header在父布局的top大于0，说明已经完全拉下
						currentStatus=STATUS_RELEASE_TO_REFRESH;
					}else{
						currentStatus=STATUS_PULL_TO_REFRESH;
					}
					//实时移动header，移动距离是distance/2，实际移动的距离是手指下拉距离的一半，以实现用力下拉的效果
					headerLayoutParams.topMargin=distance/2+hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
			}
			
			break;
		case MotionEvent.ACTION_UP://松手检测
			default:
			if((currentStatus==STATUS_RELEASE_TO_REFRESH)){
				//松手时如果是释放立即刷新状态，就去调用正在刷新任务
				new RefreshingTask().execute();
			}else if(currentStatus==STATUS_PULL_TO_REFRESH){
				//松手时如果是下拉刷新状态，就调用隐藏下拉头的任务
				new HideHeaderTask().execute();
			}
			break;
		}
		//触摸时时刻记得更新下拉头中的信息
		
		if(currentStatus==STATUS_RELEASE_TO_REFRESH||currentStatus==STATUS_PULL_TO_REFRESH){
			updateHeaderView();
			//记录上次的状态
			lastStatus=currentStatus;
			// 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
		    //返回true，listView就不会网上滚动内容
		    return true;
		}
		return false;
	}
	/**
	 * 当所有的刷新逻辑完成后记得调用一下，否则会一直处于正在刷新状态
	 */
	public void refreshingFinish(){
		currentStatus=STATUS_REFRESH_FINISHED;
		new HideHeaderTask().execute();
	}
	/**
	 * 更新下拉头中的信息
	 */
	private void updateHeaderView(){
		//如果状态没有改变 lastStatus=currentStatus，就不更新view，避免箭头一直上下转
		if(lastStatus!=currentStatus){
			if(currentStatus==STATUS_PULL_TO_REFRESH){
				description.setText(getResources().getString(R.string.pull_to_refresh));//下拉可以刷新
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			}else if(currentStatus==STATUS_RELEASE_TO_REFRESH){
				description.setText(getResources().getString(R.string.release_to_refresh));//释放刷新
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			}else if(currentStatus==STATUS_REFRESHING){
				description.setText(getResources().getString(R.string.refreshing));//正在刷新
				arrow.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
			}
			//
		
		}
		
	}
	/**
	 * 根据当前状态旋转箭头
	 */
	private void rotateArrow(){
		LogUtil.v("test", "currentStatus="+currentStatus);
		float pivotX=arrow.getWidth()/2f;
		float pivotY=arrow.getHeight()/2;
		float fromDegrees=0f;
		float toDegrees=0f;
		if(currentStatus==STATUS_PULL_TO_REFRESH){
			fromDegrees=0f;
			toDegrees=180f;
		}else if(currentStatus==STATUS_RELEASE_TO_REFRESH){
			fromDegrees=180f;
			toDegrees=360f;
		}
		RotateAnimation animation=new RotateAnimation(fromDegrees,toDegrees,pivotX,pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		animation.setRepeatCount(0);
		arrow.startAnimation(animation);
	}
	/**
	 * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器
	 */
	class RefreshingTask extends AsyncTask<Void,Integer,String>{

		@Override
		protected String doInBackground(Void... params) {
			int topMargin=headerLayoutParams.topMargin;
			/**
			 * topMargin连续减SCROLL_SPEED,每次减去都延迟10ms，直到<=0,这中间所用掉的时间就是下拉头回滚的时间
			 * 所谓下拉头回滚就是，下拉头从被下拉的位置上滑到topMargin=0（下拉头正好显示在顶部）的位置这个动作
			 */
			while(true){
				topMargin=topMargin+SCROLL_SPEED;
				if(topMargin<=0){
					topMargin=0;
					break;
				}
			
				/**
			     * publishProgress如果删除了就不会有回滚的效果，不管下拉头下拉到什么位置，松手后直接出现在顶部topMargin=0 的位置
			     * 每次调用了publishProgress()之后，就会调用onProgressUpDate()函数，此函数在UI线程里运行，起到更新界面的效果
			     * @author Dawn 20160716
			     */
				publishProgress(topMargin);
				sleep(10);
			}
			currentStatus=STATUS_REFRESHING;
			publishProgress(0);
			/**
			 * 添加下拉刷新获取天气信息功能
			 */
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
			String districtName=prefs.getString("cityName","unknown");
			String address="https://api.heweather.com/x3/weather?city="+districtName+"&key=dc908906531e4c38886eb3245eab890d";
			/**
			 * 获取的天气信息结果
			 */
			String response=null;
			try{
				HttpClient hc=new DefaultHttpClient();
				HttpGet hg=new HttpGet(address);
				HttpResponse httpResponse=hc.execute(hg);
				if(httpResponse.getStatusLine().getStatusCode()==200){
					HttpEntity entity=httpResponse.getEntity();
					response=EntityUtils.toString(entity,"utf-8");
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			/**
			 * 执行完header回滚置顶之后再执行，
			 * 外部为PullableListViewActivity里注册了setOnRefreshListener监听器，mListtener=listener
			 * 设置mListener就是用来回调onRefresh的
			 */
			if(mListener!=null){
				mListener.onRefresh();
			}
			return response;
		}
		@Override
		protected void onProgressUpdate(Integer...topMargin){
			/**
			    * onProgressUpdate的参数是Integer...topMargin，
			    * 个人理解：这是一个不确定个数的Integer[]，每次进来一个数据占用的是topMargin[0]这个位置
			    * @author-Dawn 20160716
			    */
			updateHeaderView();
			headerLayoutParams.topMargin=topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}
		/**
		 * doInBackground的返回值进入这里
		 * 在ui线程中运行
		 */
		@Override
		protected void onPostExecute(String response){
			/**
			 * 尝试使用弱引用
			 */
			WeakReference<MainActivity> thisLayout=new WeakReference<MainActivity>(null);
			final MainActivity theLayout=thisLayout.get();
		
			Utility.handleWeatherResponse(getContext(),response);
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
			theLayout.localText.setText(prefs.getString("cityName", "未知"));
			theLayout.publishTimeText.setText(prefs.getString("publishTime", "未知"	));
			theLayout.dateText.setText(prefs.getString("todayDate", "未知"));
			theLayout.tempText.setText(prefs.getString("todayMin","min")+"~"+prefs.getString("todayMax", "max"));
			theLayout.despText.setText(prefs.getString("todayDesp", ""));
		}
	}
	/**
	  * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
	  *相比Refreshing Task多了onPostExecute，此函数从doInBackground的return获取参数
	  *onPostExecute主要用于改变当前的状态currentStatus=STATUS_REFRESH_FINISHED
	  * @author Dawn
	  */
	class HideHeaderTask extends AsyncTask<Void,Integer,Integer>{
	
		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin=headerLayoutParams.topMargin;
			while(true){
				topMargin=topMargin+SCROLL_SPEED;//在这过程中topMargin的值越来越小，header头的位置往上移动
				if(topMargin<=hideHeaderHeight){
					topMargin=hideHeaderHeight;//当topMargin达到hideHeaderHeight的高度时就停止移动
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			return topMargin;
		}
		@Override
		//此函数在调用publishProgress函数后执行
		protected void onProgressUpdate(Integer...topMargin){
			updateHeaderView();
			headerLayoutParams.topMargin=topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}
		@Override
		//此函数在执行完doInBackground函数后执行，topMargin是从doInBackground来的
		protected void onPostExecute(Integer topMargin){
			headerLayoutParams.topMargin=topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus=STATUS_REFRESH_FINISHED;
		}
		
	}
	/**
	  * 使当前线程睡眠指定的毫秒数。
	  *
	  * @param time
	  *            指定当前线程睡眠多久，以毫秒为单位
	  */
	 private void sleep(int time) {
	  try {
	   Thread.sleep(time);
	  } catch (InterruptedException e) {
	   e.printStackTrace();
	  }
	 }
	/**
	  * 给下拉刷新控件注册一个监听器。
	  *
	  * @param listener
	  *            监听器的实现。
	  * @param id
	  *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
	  */
	 public void setOnRefreshListener(PullToRefreshListener listener, int id) {
	  mListener = listener;
	  mId = id;
	 }
	 public interface PullToRefreshListener {
		  /**
		   * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
		   */
		  void onRefresh();
		 }
}
