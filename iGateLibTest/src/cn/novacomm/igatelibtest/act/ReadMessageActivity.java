package cn.novacomm.igatelibtest.act;

import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.R.id;
import cn.novacomm.igatelibtest.R.layout;
import cn.novacomm.igatelibtest.util.BackDatakUtil;
import cn.novacomm.igatelibtest.util.Encoding;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ReadMessageActivity extends Activity {

	MyReceivier receivier;
	TextView textView;
	String date, sender, content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rec_message);
		textView = (TextView) findViewById(R.id.textview);
		receivier = new MyReceivier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BackDatakUtil.action);
		registerReceiver(receivier, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receivier);
		super.onDestroy();
	}

	class MyReceivier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (BackDatakUtil.action.equals(intent.getAction())) {
				Log.i("info", "获取短信数据-> ");
				byte[] data = intent.getByteArrayExtra("data");
				int totalBao = BackDatakUtil.getTotalBao(data);
				int currentBao = BackDatakUtil.getCurrentBao(data);
				if (currentBao == 0) {// 第一包
					date = BackDatakUtil.getSendDate(data);
					sender = BackDatakUtil.getSender(data);
					content = BackDatakUtil.getBackContent(data, currentBao);
				} else {
					content += BackDatakUtil.getBackContent(data, currentBao);
				}// content 是 16进制字符串
				if (totalBao == currentBao + 1) {// 接收完毕 显示
					String ddd = Encoding.gbk2Unicode(content);
					textView.setText("时间：" + date + "\n" + "发件人：" + sender
							+ "\n" + "内容：" + ddd);
				}
			}
		}

	}
}
