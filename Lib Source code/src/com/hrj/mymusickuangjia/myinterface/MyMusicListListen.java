package com.hrj.mymusickuangjia.myinterface;

/**
 * 自定义列表操作的接口
 * 
 * @author 黄荣洁
 * 
 */
public interface MyMusicListListen {
	/**
	 * 正在播放的音乐在列表中的位置
	 * 
	 * @param position
	 *            位置
	 */
	public void onPosition(int position);

	/**
	 * 正在播放的音乐的进度
	 * 
	 * @param index
	 *            当前位置
	 * @param total
	 *            总的位置
	 */
	public void onProgress(long index, long total);
}
