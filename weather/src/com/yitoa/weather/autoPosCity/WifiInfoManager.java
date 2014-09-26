package com.yitoa.weather.autoPosCity;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiInfoManager {

	WifiManager wm;

	public WifiInfoManager() {
	}

	public ArrayList getWifiInfo(Context context) {
		ArrayList<WifiInfo> wifi = new ArrayList();
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = new WifiInfo();
		info.mac = wm.getConnectionInfo().getBSSID();
		wifi.add(info);
		return wifi;
	}

	// 判断当前是否使用的是 WIFI网络
	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
