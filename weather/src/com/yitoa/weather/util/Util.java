package com.yitoa.weather.util;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.yitoa.weather.city.CityUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Administrator 项目工程的辅助类
 */
public class Util {
	
	private Context context;
	
	public Util(){}
	
	public Util(Context context){
		this.context=context;
	}

	/****
	 * 获取本地的ip地址
	 * 
	 * @return返回上网的ip地址
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}
	


	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		Log.v("Network", ((null != networkInfo) && networkInfo.isConnected())
				+ "");
		return (null != networkInfo) && networkInfo.isConnected();
	}
	
	
	// 读取assets文件夹里面的文件
		public Bitmap getImageFromAssetFile(String fileName) {
			Bitmap image = null;
			try {
				AssetManager am = context.getAssets();
				InputStream is = am.open(fileName);
				image = BitmapFactory.decodeStream(is);
				is.close();
			} catch (Exception e) {

			}
			return image;
		}
}
