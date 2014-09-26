package com.yitoa.weather;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yitoa.weather.Listener.NineLongOnClickListener;
import com.yitoa.weather.autoPosCity.AccessCity;
import com.yitoa.weather.city.CityUtil;
import com.yitoa.weather.entity.NineGrid;
import com.yitoa.weather.search.MyArrayAdapter;
import com.yitoa.weather.util.OperDataBase;
import com.yitoa.weather.util.Util;

public class MainActivity extends Activity {
	private ViewPager myViewPager;

	private MyPagerAdapter myAdapter;

	private LayoutInflater mInflater;

	private List<View> mListViews;

	private View layout3 = null;

	private View main = null;

	private View hotcity = null;

	private String checkCity = null;

	// id
	private int id;

	private View view;

	// 获取天气信息的变量
	private static final String NAMESPACE = "http://WebXml.com.cn/";

	// WebService地址
	private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";

	private static final String METHOD_NAME = "getWeatherbyCityName";

	private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

	private SoapObject detail;

	// 声明一个圆形的进度条
	private ProgressDialog xh_pDialog;

	// 显示日期和温度
	private TextView showDate1;

	private TextView showDate2;

	private TextView showDate3;

	// 提示未来三天的信息
	private TextView tipsView;

	private ImageView nineView;
	private ImageView searchView;
	private ImageView cityView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_layout);
		// 底部的三个图片
		nineView = (ImageView) findViewById(R.id.ninePage);
		searchView = (ImageView) findViewById(R.id.searchPage);
		cityView = (ImageView) findViewById(R.id.cityPage);

		myAdapter = new MyPagerAdapter();

		myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);

		myViewPager.setAdapter(myAdapter);

		mListViews = new ArrayList<View>();

		mInflater = getLayoutInflater();

		layout3 = mInflater.inflate(R.layout.layout3, null);

		main = mInflater.inflate(R.layout.main, null);
		// 获取热门城市的布局
		hotcity = mInflater.inflate(R.layout.citysearch, null);
		mListViews.add(layout3);
		mListViews.add(main);
		mListViews.add(hotcity);

		// 初始化当前显示的view
		myViewPager.setCurrentItem(1);
		changeImage(1);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("val");
		// 没有自动定位到城市所发来的intent
		Bundle autoCity = intent.getBundleExtra("autoPosCity");
		
		Bundle noWeatherMess=intent.getBundleExtra("noWeatherMess");
		if (null != autoCity) {
			View v = mListViews.get(0);
			myViewPager.setCurrentItem(0);
			initNineMess(v);
		}else if(noWeatherMess!=null){
			//显示城市管理页面
			myViewPager.setCurrentItem(2);
			//初始化城市管理页面
			initHotCity();
		} else {
			String city;
			if (bundle != null) {
				city = bundle.getString("city");
			} else {
				city = defaultWeather();
			}
			search(city);
		}
		//左右滑屏的监听器
		myViewPager.setOnPageChangeListener(new PageGideListener());

	}
	
	
	//左右滑屏的监听器
	private class PageGideListener implements OnPageChangeListener{
		
		public void onPageSelected(int arg0) {

			Log.d("myDebug", "onPageSelected - " + arg0);

			// activity从1到2滑动，2被加载后掉用此方法

			View v = mListViews.get(arg0);
			if (arg0 == 0) {// 九方格
				initNineMess(v);
			}
			// 如果滑屏到热门城市啊
			else if (arg0 == 2) {
				//初始化城市管理页面
				initHotCity();

			} else if (arg0 == 1) {
				// 默认显示那个城市
				search(defaultWeather());
				changeImage(1);
			}

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

			Log.d("myDebug", "onPageScrolled - " + arg0);

			// 从1到2滑动，在1滑动前调用

		}

		public void onPageScrollStateChanged(int arg0) {

			Log.d("myDebug", "onPageScrollStateChanged - " + arg0);
			// 状态有三个0空闲，1是增在滑行中，2目标加载完毕

		}

	}
	
	
	/***
	 * 初始城市管理页面
	 */
	public void initHotCity(){
		// 获取热门城市的布局
		View hotCityV = mListViews.get(2);
		// 获取输入城市的
		final AutoCompleteTextView act = (AutoCompleteTextView) hotCityV
				.findViewById(R.id.city);
		MyArrayAdapter<String> adapter = new MyArrayAdapter<String>(
				MainActivity.this,
				android.R.layout.simple_spinner_item,
				CityUtil.CITIE_IN_HANZI);
		// 设置从第一字就开始匹配
		act.setThreshold(1);
		act.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				String s = parent.getItemAtPosition(position)
						.toString();
				if (-1 < s.indexOf("-")) {
					act.setText(s.substring(0, s.indexOf("-"))
							.trim());
				} else {
					act.setText(s.trim());
				}
			}

		});

		act.setAdapter(adapter);

		// 配置热门城市
		GridView gridview = (GridView) hotCityV
				.findViewById(R.id.hotcity);
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(
				MainActivity.this, CityUtil.getHotCity(),
				R.layout.hotcity, new String[] { "ItemHotCity" },
				new int[] { R.id.ItemHotCity });
		gridview.setAdapter(saImageItems);
		gridview.setOnItemClickListener(new HotCityListener());

		// 获取城市管理页面里面的搜索按钮
		Button search = (Button) hotCityV
				.findViewById(R.id.searchCity);

		search.setOnClickListener(new SearchOnClickListener());
		// 设置底部显示的图标
		changeImage(2);
	}
	

	/****
	 * 初始化应用是显示天气
	 */
	public String defaultWeather() {
		// 默认显示北京
		AccessCity autoPosCity = new AccessCity();
		String city = autoPosCity.getCity(MainActivity.this);
		if (city == null) {
			city = "北京";
		} else {
			city = new CityUtil().getCityPy(city);
		}

		// 设置底部显示的图标
		return city;
	}

	/***
	 * 初始化九宫格里面的信息
	 * 
	 * @param v
	 */
	public void initNineMess(View v) {
		GridView gridview = (GridView) v.findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		OperDataBase odp = new OperDataBase(MainActivity.this);
		List<NineGrid> list = odp.queryAll();
		Log.d("myDebug", "==list==" + list.size());
		for (int i = 0; i < 9; i++) {
			int flag = 0;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", R.drawable.ic_launcher);// 添加图像资源的ID
			for (int k = 0; k < list.size(); k++) {
				NineGrid ng = list.get(k);
				if (ng.getId() == i) {
					flag = 1;
					map.put("ItemText", ng.getCity());// 按序号做ItemText
				}
			}
			if (flag == 0) {
				map.put("ItemText", "NO." + String.valueOf(i));// 按序号做ItemText
			}
			lstImageItem.add(map);
		}
		// 显示几行
		gridview.setNumColumns(3);
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(MainActivity.this,
				lstImageItem, R.layout.night_item, new String[] { "ItemImage",
						"ItemText" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		// 添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
		// 长点击事件
		gridview.setOnItemLongClickListener(new NineLongOnClickListener(MainActivity.this));
		
		changeImage(0);
	}

	
	// 点击城市管理页面中的搜索按钮
	private class SearchOnClickListener implements OnClickListener {

		public void onClick(View v) {
			AutoCompleteTextView act = (AutoCompleteTextView) mListViews.get(2)
					.findViewById(R.id.city);
			String value = act.getText().toString();
			if (value == null || "".equals(value)) {
				Toast.makeText(MainActivity.this, "请输入所要查询的城市",
						Toast.LENGTH_LONG).show();
			} else {
				skipNine("1", value, "val");
			}

		}

	}

	// 点击热门城市要做的处理
	private class HotCityListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 != 0) {
				TextView tx = (TextView) arg1.findViewById(R.id.ItemHotCity);
				String value = tx.getText().toString();
				// 跳转到首页面去
				skipNine("1", value, "val");
			} else {// 自动定位
				AccessCity autoPosCity = new AccessCity();
				String city = autoPosCity.getCity(MainActivity.this);
				Log.d("myDebug", "==atuoPosCity==" + city);
				if (city == null) {
					Toast.makeText(MainActivity.this, R.string.nopostcity,
							Toast.LENGTH_LONG).show();
					skipNine("2", null, "autoPosCity");
				} else {// 添加到九宫格中
					CityUtil cu = new CityUtil();
					// 根据拼音找到汉子的城市
					String cityHz = cu.getCityPy(city);
					if (cityHz == null) {
						Toast.makeText(MainActivity.this, R.string.nopostcity,
								Toast.LENGTH_LONG).show();

					} else {
						Log.d("myDebug", "==cityHz==" + cityHz);
						// 判断该城市是否在九宫格里面
						OperDataBase odp = new OperDataBase(MainActivity.this);
						NineGrid ng = odp.query(cityHz);
						if (ng != null) {// 说明九宫格中已经存在该城市了
							Toast.makeText(MainActivity.this,
									R.string.existCity, Toast.LENGTH_LONG)
									.show();
							// 跳转到九宫去
							skipNine("2", null, "autoPosCity");
						} else {// 如果不存在需要添加到九宫格去
								// 九宫格中空缺的id
							int id = odp.nineNotAdd();
							Log.d("myDebug", "==id===" + id);
							NineGrid newNg = new NineGrid();
							newNg.setCity(cityHz);
							newNg.setId(id);
							odp.save(newNg);
							Toast.makeText(MainActivity.this, R.string.addCity,
									Toast.LENGTH_LONG).show();
							// 跳转到九宫去
							skipNine("2", null, "autoPosCity");
						}
					}

				}

			}

		}

	}

	/***
	 * 
	 * @param type
	 *            类型
	 * @param city
	 *            城市
	 * @param extraName
	 *            putExtra中的name值
	 */
	private void skipNine(String type, String city, String extraName) {
		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		Bundle bundle = new Bundle();
		if ("1".equals(type)) {// 点击城市名称到天气想起页面
			bundle.putString("type", type);
			bundle.putString("city", city);
			intent.putExtra(extraName, bundle);
		} else if ("2".equals(type)) {//自动定位调转到九宫格页面
			bundle.putString("type", type);
			intent.putExtra(extraName, bundle);
		}else if("3".equals(type)){//没有查到天气信息调转到城市管理页面
			bundle.putString("type", type);
			intent.putExtra("noWeatherMess", bundle);
		}

		MainActivity.this.startActivity(intent);
	}

	// 左右滑屏的适配器
	private class MyPagerAdapter extends PagerAdapter {

		public void destroyItem(View arg0, int arg1, Object arg2) {

			Log.d("myDebug", "destroyItem");

			((ViewPager) arg0).removeView(mListViews.get(arg1));

		}

		public void finishUpdate(View arg0) {

			Log.d("myDebug", "finishUpdate");

		}

		public int getCount() {

			return mListViews.size();

		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			Log.d("myDebug", "instantiateItem+arg1" + arg1);

			((ViewPager) arg0).addView(mListViews.get(arg1), 0);

			return mListViews.get(arg1);

		}

		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == (arg1);

		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {

			Log.d("myDebug", "restoreState");

		}

		public Parcelable saveState() {

			Log.d("myDebug", "saveState");

			return null;

		}

		public void startUpdate(View arg0) {

			Log.d("myDebug", "startUpdate");

		}

	}

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// 获取九方格下面的值
			TextView textV = (TextView) arg1.findViewById(R.id.ItemText);
			String value = textV.getText().toString();
			id = arg2;
			view = arg1;
			// 如果还没有赋值添加值
			if (value.contains("NO")) {
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("请选择")
						.setItems(CityUtil.CITIE_IN_HANZI,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										checkCity = CityUtil.CITIE_IN_HANZI[which];
										Log.d("myDebug",
												CityUtil.CITIE_IN_HANZI[which]);
										if (checkCity != null) {
											OperDataBase odp = new OperDataBase(
													MainActivity.this);
											NineGrid ng = odp.query(checkCity);
											Log.d("myDebug", "==checkCity=="
													+ checkCity);
											// 存在该城市
											if (ng != null) {
												Toast.makeText(
														MainActivity.this,
														"已经添加该城市了不能再次添加",
														Toast.LENGTH_LONG)
														.show();
											} else {// 不存在该城市
													// 判断九方格的该格是否被使用
												int qid = odp.queryId(id);
												NineGrid mng = new NineGrid();
												mng.setId(id);
												mng.setCity(checkCity);
												// 没有被使用
												if (qid == -1) {
													odp.save(mng);
													TextView tv = (TextView) view
															.findViewById(R.id.ItemText);
													tv.setText(checkCity);
												} else {
													odp.update(mng);
												}

											}
										}
									}
								}).show();

				// setTitle(checkCity);
				Log.d("myDebug", "==checkCity111==" + checkCity);
				// 判断数据库是否存在该城市

			} else {// 如果赋值
				skipNine("1", value, "val");
			}

		}

	}

	// 显示天气情况
	public void showWeather(SoapObject detail, View mian) {
		//初始化工具类
		Util util=new Util(MainActivity.this);
		showDate1 = (TextView) mian.findViewById(R.id.showDate1);
		showDate2 = (TextView) mian.findViewById(R.id.showDate2);
		showDate3 = (TextView) mian.findViewById(R.id.showDate3);
		tipsView = (TextView) mian.findViewById(R.id.tips);

		ImageView bt1 = (ImageView) mian.findViewById(R.id.showtb1);
		ImageView bt2 = (ImageView) mian.findViewById(R.id.showtb2);
		TextView bj1 = (TextView) mian.findViewById(R.id.bj1);

		ImageView bt3 = (ImageView) mian.findViewById(R.id.showtb3);
		ImageView bt4 = (ImageView) mian.findViewById(R.id.showtb4);
		TextView bj2 = (TextView) mian.findViewById(R.id.bj2);

		ImageView bt5 = (ImageView) mian.findViewById(R.id.showtb5);
		ImageView bt6 = (ImageView) mian.findViewById(R.id.showtb6);
		TextView bj3 = (TextView) mian.findViewById(R.id.bj3);

		TextView tips = (TextView) mian.findViewById(R.id.tips);
		tips.setVisibility(TextView.VISIBLE);

		TextView windSize1 = (TextView) mian.findViewById(R.id.windsize1);
		TextView windSize2 = (TextView) mian.findViewById(R.id.windsize2);
		TextView windSize3 = (TextView) mian.findViewById(R.id.windsize3);

		// 获取查询天气的城市存放地址
		TextView weatherCity = (TextView) mian.findViewById(R.id.weatherCity);
		TextView currtFh = (TextView) mian.findViewById(R.id.currtFh);

		// 获取显示天气图片的地址
		ImageView weatherImage = (ImageView) mian
				.findViewById(R.id.weatherImage);
		ImageView weatherImage1 = (ImageView) mian
				.findViewById(R.id.weatherImage1);

		// 显示查询出来的天气信息
		// 获取今天的实时天气
		String city = detail.getProperty(1).toString();
		weatherCity.setText(city.toString());
		weatherImage.setImageBitmap(util.getImageFromAssetFile("a_"
				+ detail.getProperty(8).toString()));
		currtFh.setText("~~");
		weatherImage1.setImageBitmap(util.getImageFromAssetFile("a_"
				+ detail.getProperty(9).toString()));

		// 添加当前天气情况
		currentWeather(detail.getProperty(10).toString(), mian);

		tipsView.setText(city + "未来三天的天气情况");
		// 获取今天的信息
		String[] str = detail.getProperty(6).toString().split(" ");
		String tdWeather = str[0] + "\n" + str[1] + "\n"
				+ detail.getProperty(5);
		showDate1.setText(tdWeather);
		// 获取明天的信息
		str = detail.getProperty(13).toString().split(" ");
		String tomWeather = str[0] + "\n" + str[1] + "\n"
				+ detail.getProperty(12);
		showDate2.setText(tomWeather);
		// 获取后天的天气
		str = detail.getProperty(18).toString().split(" ");
		String ttomWeather = str[0] + "\n" + str[1] + "\n"
				+ detail.getProperty(17);
		showDate3.setText(ttomWeather);

		// 开始获取下面的图片
		bt1.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(8)
				.toString()));
		bt2.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(9)
				.toString()));
		bj1.setText("~~");

		bt3.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(15)
				.toString()));
		bt4.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(16)
				.toString()));
		bj2.setText("~~");

		bt5.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(20)
				.toString()));
		bt6.setImageBitmap(util.getImageFromAssetFile(detail.getProperty(21)
				.toString()));
		bj3.setText("~~");

		// 添加风力大小
		windSize1.setText(detail.getProperty(7).toString());
		windSize2.setText(detail.getProperty(14).toString());
		windSize3.setText(detail.getProperty(19).toString());
		// showLifeIndex(detail, main);
	}

	/***
	 * 放置当前天气的情况
	 * 
	 * @param str
	 */
	public void currentWeather(String str, View main) {
		TextView currentWd = (TextView) main.findViewById(R.id.currentWd);
		TextView currentFd = (TextView) main.findViewById(R.id.currentFd);
		TextView currentSd = (TextView) main.findViewById(R.id.currentSd);
		TextView currentKQZL = (TextView) main.findViewById(R.id.currentKQZL);
		TextView currentZWX = (TextView) main.findViewById(R.id.currentZWX);

		str = str.replace("今日天气实况：", "");
		Log.d("myDebu", "==str==" + str);
		if (!str.contains("暂无实况")) {
			currentWd.setText(str.substring(str.indexOf("气温"),
					str.indexOf("风向/风力")).replace("；", ""));
			currentFd.setText(str.substring(str.indexOf("风向/风力"),
					str.indexOf("湿度")).replace("；", ""));
			currentSd.setText(str.substring(str.indexOf("湿度"),
					str.indexOf("空气质量")).replace("；", ""));
			currentKQZL.setText(str.substring(str.indexOf("空气质量"),
					str.indexOf("紫外线强度")).replace("；", ""));
			currentZWX.setText(str
					.substring(str.indexOf("紫外线强度"), str.length()).replace("；",
							""));
		}

	}

	/****
	 * 根据城市名称获取城市的天气情况
	 * 
	 * @param city
	 * @return
	 */
	private SoapObject getWeather(String city) {
		if (!Util.isNetworkConnected(this)) {
			Toast.makeText(MainActivity.this, "暂时没有可用的网络，请稍后再试！",
					Toast.LENGTH_LONG).show();
			
			return null;
		}
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("theCityName", city);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			detail = (SoapObject) envelope.getResponse();
			Log.d("weather", detail.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	

	// 创建进度条
	public ProgressDialog createPro() {
		xh_pDialog = new ProgressDialog(MainActivity.this);
		// 设置进度条风格，风格为圆形，旋转的
		xh_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 设置ProgressDialog提示信息
		xh_pDialog.setMessage("正在查询请稍后");

		// 设置ProgressDialog标题图标
		xh_pDialog.setIcon(android.R.drawable.ic_dialog_map);

		return xh_pDialog;
	}

	/***
	 * 切换底部图标
	 * 
	 * @param showpage
	 *            显示的页数
	 */
	public void changeImage(int showpage) {
		if (showpage == 0) {// 显示的九宫格
			this.setChangeImage(nineView, R.drawable.current);
			this.setChangeImage(searchView, R.drawable.noshow);
			this.setChangeImage(cityView, R.drawable.noshow);
		} else if (showpage == 1) {// 显示详细页面
			this.setChangeImage(nineView, R.drawable.noshow);
			this.setChangeImage(searchView, R.drawable.current);
			this.setChangeImage(cityView, R.drawable.noshow);
		} else if (showpage == 2) {
			this.setChangeImage(nineView, R.drawable.noshow);
			this.setChangeImage(searchView, R.drawable.noshow);
			this.setChangeImage(cityView, R.drawable.current);
		}
	}

	/***
	 * 设置图片
	 * 
	 * @param v
	 * @param resourceId
	 */
	public void setChangeImage(ImageView v, int resourceId) {
		v.setImageResource(resourceId);
	}

	public void search(String city) {

		detail = getWeather(city.trim());
		if (null == detail
				|| detail.getProperty(1).toString().contains("anyType")) {
			Toast.makeText(MainActivity.this, R.string.noweatherMess,
					Toast.LENGTH_LONG).show();
			skipNine("3", null, "noWeatherMess"); 
		}else{
			showWeather(detail, main);
		}
	}
}
