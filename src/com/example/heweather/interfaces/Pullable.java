package com.example.heweather.interfaces;

public interface Pullable {

	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ����ڸ�д��ʱ��ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullDown();

	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ����ڸ�д��ʱ��ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullUp();
}
