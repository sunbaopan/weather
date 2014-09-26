package com.yitoa.weather.Listener;

import com.yitoa.weather.R;
import com.yitoa.weather.util.OperDataBase;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

/****
 * 九方格中的长点击事件
 * @author Administrator
 *
 */
public class NineLongOnClickListener implements OnItemLongClickListener {
	
	private Context context;
	
	public NineLongOnClickListener(){
		
	}
	
	public NineLongOnClickListener(Context context){
		this.context=context;
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Log.d("myDebug", "==longClick===");
		TextView textV = (TextView) arg1.findViewById(R.id.ItemText);
		String value = textV.getText().toString();
		if (!value.contains("NO")) {// 已经添加城市了
			OperDataBase odp = new OperDataBase(context);
			odp.del(arg2);
			textV.setText("NO." + arg2);
		}
		return false;
	}

}
