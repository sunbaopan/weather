package com.yitoa.weather.autoPosCity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class AccessCity {

	/****
	 * 自动获取当前城市
	 * 
	 * @param context
	 * @return
	 */
	public String getCity(Context context) {
		String city = null;
		TelephonyManager tm;

		WifiInfoManager wifiInfoManager = new WifiInfoManager();
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {// 如果有网络
			// 获取网络类型
			int type = tm.getNetworkType();
			Log.d("myDebug", "==type==" + type);
			// 如果使用下面网络
			if (type == TelephonyManager.NETWORK_TYPE_GPRS
					|| type == TelephonyManager.NETWORK_TYPE_EDGE
					|| type == TelephonyManager.NETWORK_TYPE_HSDPA) {

				GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
				int cid = gcl.getCid();
				int lac = gcl.getLac();
				int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,
						3));
				int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,
						5));

				try {
					// 组装JSON查询字符串
					JSONObject holder = new JSONObject();
					holder.put("version", "1.1.0");
					holder.put("host", "maps.google.com");
					// holder.put("address_language", "zh_CN");
					holder.put("request_address", true);

					JSONArray array = new JSONArray();
					JSONObject data = new JSONObject();
					data.put("cell_id", cid); // 25070
					data.put("location_area_code", lac);// 4474
					data.put("mobile_country_code", mcc);// 460
					data.put("mobile_network_code", mnc);// 0
					array.put(data);
					holder.put("cell_towers", array);

					// 创建连接，发送请求并接受回应
					DefaultHttpClient client = new DefaultHttpClient();

					HttpPost post = new HttpPost(
							"http://www.google.com/loc/json");

					StringEntity se = new StringEntity(holder.toString());

					post.setEntity(se);
					HttpResponse resp = client.execute(post);

					HttpEntity entity = resp.getEntity();

					BufferedReader br = new BufferedReader(
							new InputStreamReader(entity.getContent()));

					StringBuffer sb = new StringBuffer();

					String result = br.readLine();

					while (result != null) {
						sb.append(result);
						result = br.readLine();
					}
					Log.d("myDebug", "sb" + sb);
					Location loc = this.parseJson(sb.toString());
					if (loc != null) {
						Bundle bundle = loc.getExtras();
						city = bundle.getString("city");
						if (city != null) {
							city = city.trim();
						}
					}
				} catch (Exception e) {
				}
				// 使用cdma网络
			} else if (type == TelephonyManager.NETWORK_TYPE_CDMA
					|| type == TelephonyManager.NETWORK_TYPE_1xRTT
					|| type == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
				CdmaCellLocation location = (CdmaCellLocation) tm
						.getCellLocation();
				Log.d("myDebug", "==cdma==");
				if (location == null)
					return null;
				int sid = location.getSystemId();// 系统标识 mobileNetworkCode
				int bid = location.getBaseStationId();// 基站小区号 cellId
				int nid = location.getNetworkId();// 网络标识 locationAreaCode

				ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
				CellIDInfo info = new CellIDInfo();
				info.cellId = bid;
				info.locationAreaCode = nid;
				info.mobileNetworkCode = String.valueOf(sid);
				info.mobileCountryCode = tm.getNetworkOperator()
						.substring(0, 3);
				info.mobileCountryCode = tm.getNetworkOperator()
						.substring(3, 5);
				info.radioType = "cdma";
				CellID.add(info);
				Location loc = callGear(CellID);
				if (loc != null) {
					Bundle bundle = loc.getExtras();
					city = bundle.getString("city");
					if (city != null) {
						city = city.trim();
					}
					Log.d("myDebug", "city" + city);

				}
			}
		} else if (wifiInfoManager.isWifiActive(context)) {// 判读是否在使用wifi如果使用
			JSONObject data;

			JSONArray array;

			JSONObject holder = new JSONObject();
			HttpPost post = new HttpPost("http://www.google.com/loc/json");
			DefaultHttpClient client = new DefaultHttpClient();
			try {

				// 根据wifi获取所在的城市
				ArrayList<WifiInfo> wifi = wifiInfoManager.getWifiInfo(context);
				if (wifi.get(0).mac != null) {
					data = new JSONObject();
					data.put("mac_address", wifi.get(0).mac);
					data.put("signal_strength", 8);
					data.put("age", 0);
					array = new JSONArray();
					array.put(data);
					holder.put("wifi_towers", array);
				}

				StringEntity se = new StringEntity(holder.toString());

				post.setEntity(se);
				HttpResponse resp = client.execute(post);

				HttpEntity entity = resp.getEntity();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				StringBuffer sb = new StringBuffer();
				String result = br.readLine();
				while (result != null) {
					Log.e("Locaiton reseive", result);
					sb.append(result);
					result = br.readLine();
				}
				// 获取城市
				Location loc = this.parseJson(sb.toString());
				if (loc != null) {
					Bundle bundle = loc.getExtras();
					city = bundle.getString("city");
					if (city != null) {
						city = city.trim();
					}
				}

			} catch (Exception e) {
				Log.e("-", e.toString());
			}
		}

		return city;
	}

	/***
	 * cdma卡基站调用
	 * 
	 * @param cellID
	 * @return
	 */
	private Location callGear(ArrayList<CellIDInfo> cellID) {
		if (cellID == null)
			return null;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();

		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code",
					cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode))
				holder.put("address_language", "zh_CN");
			else
				holder.put("address_language", "en_US");

			JSONObject current_data;

			JSONArray array = new JSONArray();

			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("location_area_code",
					cellID.get(0).locationAreaCode);
			current_data.put("mobile_country_code",
					cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			current_data.put("signal_strength", -60);
			current_data.put("timing_advance", 5555);
			array.put(current_data);
			holder.put("cell_towers", array);
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);

			HttpEntity entity = resp.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				Log.e("Locaiton reseive", result);
				sb.append(result);
				result = br.readLine();
			}

			Location loc = this.parseJson(sb.toString());
			return loc;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 解析响应回来的json字符串
	 * 
	 * @param sb
	 * @return
	 */
	public Location parseJson(String sb) {
		JSONObject data, address;
		try {
			data = new JSONObject(sb.toString());
			Log.d("myDebug", sb.toString());
			data = (JSONObject) data.get("location");
			address = (JSONObject) data.get("address");

			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			loc.setTime(System.currentTimeMillis());// AppUtil.getUTCTime());
			Bundle bundle = new Bundle();
			// 放置所在的城市
			bundle.putString("city", address.getString("city"));
			loc.setExtras(bundle);
			return loc;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
