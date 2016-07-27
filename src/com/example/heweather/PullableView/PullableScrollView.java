package com.example.heweather.PullableView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements com.example.heweather.interfaces.Pullable{

	



	public PullableScrollView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	public PullableScrollView(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	public PullableScrollView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
	}

	@Override
	public boolean canPullDown() {
		//如果scroll划到最顶部了，就允许下拉
		if(getScrollY()==0)
			return true;
		else 
			return false;
		
	}
/**
 * mScrollView.getChildAt(0).getMeasuredHeight().表示ScrollView的实际高度，与屏幕大小无关
 * view.getScrollY().表示ScrollView顶端已经划出去的高度/距离
 * view.getHeight().表示ScrollView的可见高度，与屏幕有关
 */
	@Override
	public boolean canPullUp() {
		
		if(getChildAt(0).getMeasuredHeight()<=getScrollY()+getHeight()){
			return true;
		}
		else
		return false;
	}

	
}
