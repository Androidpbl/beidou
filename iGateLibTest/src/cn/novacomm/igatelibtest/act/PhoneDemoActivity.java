package cn.novacomm.igatelibtest.act;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.novacomm.ble.iGate;
import cn.novacomm.ble.iGateCallBacks;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.R.id;
import cn.novacomm.igatelibtest.R.layout;
import cn.novacomm.igatelibtest.R.string;
import cn.novacomm.igatelibtest.util.BackDatakUtil;
import cn.novacomm.igatelibtest.util.Encoding;
import cn.novacomm.igatelibtest.util.Util;

public class PhoneDemoActivity extends Activity implements iGateCallBacks {
	private static final int ENABLE_BT_REQUEST_ID = 1;

	private TextView mIGateState;
	// private TextView mReceivedData;
	// private Button mButtonSend;
	private ArrayList<String> mConnectedBluetoothDevicesAddress = new ArrayList<String>();
	private int sendIndex = 0;
	// private String mConnectedBluetoothAddress=null;
	private BroadcastReceiver mPairIntentReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent
					.getAction())) {
				// int prevBondState =
				// intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE,
				// -1);
				int bondState = intent.getIntExtra(
						BluetoothDevice.EXTRA_BOND_STATE, -1);
				BluetoothDevice aDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (bondState == BluetoothDevice.BOND_BONDED) {
					mIgate.iGateDeviceSetPairState(aDevice.getAddress(), true);
				}
				Log.i("---- pair changed",
						aDevice.getAddress() + String.format("-%d", bondState));
			}
		}
	};

	private String message = "";// 测试短信0x240x530x4d0x140x020x000x0e0x0f0x060x11
								// 0x0b0x250x000x030x3b0xd7 0xa0 0x380x41sum
	// private String test="0x240x4F0x430x050x04";
	private byte[] testBytes = new byte[] { 0x24, 0x53, 0x4d, 0x14, 0x02, 0x00,
			0x0e, (byte) 0x0f, 0x06, 0x11, 0x0b, 0x25, 0x00, 0x03, 0x7e,
			(byte) 0x21, (byte) 0xef, (byte) 0x79, 0x41, 0x15 };

	private byte[] testBytes1 = new byte[] { 0x24, 0x53, 0x4d, 0x14, 0x02,
			0x01, 0x42, (byte) 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a,
			(byte) 0x4b, (byte) 0x4c, 0x4d, 0x4e, 0x15 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_phone);

		mIGateState = (TextView) findViewById(R.id.textView1);
		// mReceivedData = (TextView) findViewById(R.id.textViewRxData);
		et_content = (EditText) findViewById(R.id.et_content);
		et_phone = (EditText) findViewById(R.id.et_phone);

		mIgate = new iGate(this,
				UUID.fromString("C14D2C0A-401F-B7A9-841F-E2E93B80F631"), this);
		// mIgate.initialize(true);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mIgate.initialize(false); // true
		IntentFilter bluetoothPairFilter = new IntentFilter();
		bluetoothPairFilter
				.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		registerReceiver(mPairIntentReceiver, bluetoothPairFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mPairIntentReceiver);
	}

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.button1:// 开始搜索
			mIgate.startScanning(false);
			break;
		case R.id.button2:// 发送短信
			if (getString(R.string.str_idle).equals(
					mIGateState.getText().toString().trim())) {
				Toast.makeText(this, "请先搜索设备!", 0).show();
			} else {
				sendMessage();
			}

			break;
		case R.id.button3:// 读取盒子短信
			startActivity(new Intent(this, ReadMessageActivity.class));
			break;
		}
	}

	private void sendME(byte[] b) {
		if (sendIndex < mConnectedBluetoothDevicesAddress.size()) {
			mIgate.iGateDeviceSendData(
					mConnectedBluetoothDevicesAddress.get(sendIndex), b);
			//
			Log.i("------", String.format(
					"iGate State %d",
					mIgate.iGateDeviceGetState(
							mConnectedBluetoothDevicesAddress.get(sendIndex))
							.ordinal()));
		} else {
			sendIndex = 0;
			if (mConnectedBluetoothDevicesAddress.size() > 0) {
				mIgate.iGateDeviceSendData(
						mConnectedBluetoothDevicesAddress.get(sendIndex), b);
			}
		}
		sendIndex++;
		if (sendIndex == mConnectedBluetoothDevicesAddress.size()) {
			sendIndex = 0;
		}
	}

	EditText et_phone, et_content;

	private void sendMessage() {
		// TODO Auto-generated method stub
		if ("".equals(et_content.getText().toString().trim())
				|| "".equals(et_phone.getText().toString().trim())) {
			Toast.makeText(this, "请完善信息！", 0).show();
		} else {
			String total = getTotalBao();
			if (null != total) {
				for (int i = 0; i < Integer.valueOf(total); i++) {
					String data = getAllString(Integer.valueOf(total), i);
					System.out.println(data);
					byte[] ddds = getBytes(data);
					if (sendIndex < mConnectedBluetoothDevicesAddress.size()) {
						mIgate.iGateDeviceSendData(
								mConnectedBluetoothDevicesAddress
										.get(sendIndex), ddds);
						//
						Log.i("------", String.format(
								"iGate State %d",
								mIgate.iGateDeviceGetState(
										mConnectedBluetoothDevicesAddress
												.get(sendIndex)).ordinal()));
					} else {
						sendIndex = 0;
						if (mConnectedBluetoothDevicesAddress.size() > 0) {
							mIgate.iGateDeviceSendData(
									mConnectedBluetoothDevicesAddress
											.get(sendIndex), ddds);
						}
					}
					sendIndex++;
					if (sendIndex == mConnectedBluetoothDevicesAddress.size()) {
						sendIndex = 0;
					}

					System.out.println(Arrays.toString(ddds));
				}
			}

		}
	}

	@Override
	public void onBackPressed() {
		for (int i = 0; i < mConnectedBluetoothDevicesAddress.size(); i++) {
			Log.i("------", String.format("iGate disconnect %s",
					mConnectedBluetoothDevicesAddress.get(i)));
			mIgate.iGateDeviceDisconnect(mConnectedBluetoothDevicesAddress
					.get(i));
		}
		if (mIgate.getIGateState() == iGateCallBacks.iGateHostState.iGateHostStateSearching) {
			mIgate.stopScanning();
		}
		super.onBackPressed();
	}

	public void iGateHostDidUpdateState(final iGateHostState state) {
		Log.i("------", String.format("iGate State Change %d", state.ordinal()));
		switch (state) {
		case iGateHostStatePoweredOff:
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
			// mIgate.startScanning(true);
		case iGateHostStateIdle:
		case iGateHostStateSearching:
			// start automatically search when get idle,
			// mIgate.startScanning(true);
			invalidateOptionsMenu();
			break;

		default:
			break;
		}

		// adding to the UI have to happen in UI thread
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (mIgate.getIGateState()) {
				case iGateHostStateIdle:
					if (mConnectedBluetoothDevicesAddress.size() == 0) {
						mIGateState.setText(R.string.str_idle);
					} else {
						listConnectedDevices();
					}
					break;
				case iGateHostStateSearching:
					mIGateState.setText(R.string.str_scanning);
					break;
				default:
					break;
				}
			}
		});
	}

	/*
	 * (non-Javadoc) 搜索到设备了
	 * 
	 * @see cn.novacomm.ble.iGateCallBacks#iGateDeviceFound(java.lang.String,
	 * int, byte[])
	 */
	public void iGateDeviceFound(final String bluetoothAddress, final int rssi,
			final byte[] record) {
		Log.i("------",
				String.format("iGate found device %s", bluetoothAddress));
		// If iGate is initialized to automatically connect to device found,
		// there's no need to stop scan and make connection manually
		// mIgate.stopScanning();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mIGateState.setText(getString(R.string.str_found)
						+ bluetoothAddress);
				mIgate.stopScanning();
				mIgate.iGateDeviceConnect(bluetoothAddress);
			}
		});
		// Log.i("------", String.format("start connect to %s",
		// bluetoothAddress));
		// mIgate.iGateDeviceConnect(bluetoothAddress);
	}

	public void iGateDeviceConnected(final String bluetoothAddress) {
		Log.i("------", String.format("iGate connected %s", bluetoothAddress));
		if (!mConnectedBluetoothDevicesAddress.contains(bluetoothAddress)) {
			mConnectedBluetoothDevicesAddress.add(bluetoothAddress);
		}
		// mIgate.iGateDeviceBondService(bluetoothAddress);
		// adding to the UI have to happen in UI thread
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// listConnectedDevices();
				Log.i("------", String.format("iGate bond service to %s",
						bluetoothAddress));
				mIgate.iGateDeviceBondService(bluetoothAddress);
				mIGateState.setText(getString(R.string.str_found)
						+ bluetoothAddress);
			}
		});
	}

	public void iGateDeviceServiceBonded(final String bluetoothAddress) {
		Log.i("------", String.format("iGate bonded %s", bluetoothAddress));
		if (!mConnectedBluetoothDevicesAddress.contains(bluetoothAddress)) {
			mConnectedBluetoothDevicesAddress.add(bluetoothAddress);
		}
		// adding to the UI have to happen in UI thread
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listConnectedDevices();
			}
		});
	}

	public void iGateDeviceDisConnected(final String bluetoothAddress) {
		Log.i("------", String.format("disconnected %s", bluetoothAddress));
		mConnectedBluetoothDevicesAddress.remove(bluetoothAddress);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mConnectedBluetoothDevicesAddress.size() > 0) {
					String allConnectedDevices = new String();
					for (int i = 0; i < mConnectedBluetoothDevicesAddress
							.size(); i++) {
						allConnectedDevices += " "
								+ mConnectedBluetoothDevicesAddress.get(i);
					}
					mIGateState.setText(getString(R.string.str_connected)
							+ allConnectedDevices);
				} else {
					mIGateState.setText(getString(R.string.str_idle));
				}

			}
		});
	}

	public void iGateDeviceReceivedData(final String bluetoothAddress,
			final byte[] data) {
		try {
			final String tmpValue = new String(data);
			// final String tmpValue = Util.decode(data.toString());
			Log.e("------ RX data: ", Arrays.toString(data));

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// mReceivedData.append(tmpValue); 返回数据。。。。
					receiveData(data);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mIgate.iGateDeviceSendData(bluetoothAddress, data);
	}

	public void iGateDeviceUpdateRssi(final String bluetoothAddress,
			final int rssi) {
		Log.i("------",
				String.format("Rssi report %s,%d",
						mIgate.iGateDeviceGetName(bluetoothAddress), rssi));
	}

	public void iGateDeviceLinkLossAlertLevelReport(
			final String bluetoothAddress, byte alertLevel) {
		Log.i("------", String.format("Link loss alert level %s,%d",
				bluetoothAddress, alertLevel));
	}

	public void iGateDeviceTxPowerReport(final String bluetoothAddress,
			byte txPower) {
		Log.i("------",
				String.format("Tx Power %s,%d", bluetoothAddress, txPower));
	}

	private void listConnectedDevices() {
		String allConnectedDevices = new String();
		for (int i = 0; i < mConnectedBluetoothDevicesAddress.size(); i++) {
			allConnectedDevices += " "
					+ mConnectedBluetoothDevicesAddress.get(i);
		}
		mIGateState.setText(getString(R.string.str_connected)
				+ allConnectedDevices);
	}

	private iGate mIgate = null;

	private static String homeMes = "0x24,0x53,0x4d,";// $SM--
	private static String mes_leng = "0x00,";// 帧长
	private static String bao_sum = "0x00";// 总包数
	private static String bao_curr = "";// 当前包数
	private static String msg_leng = "";// 短信长度
	private static String msg_year = "";// 年
	private static String msg_month = "";// 月
	private static String msg_dat = "";// 日
	private static String msg_time = "";// 时
	private static String msg_min = "";// 分
	private static String msg_sec = ",0x00";// 秒
	private static String msg_receriver = ",0x00";// 收件人
	private static String msg_content = ",0x00";// 内容
	private static String msg_jy = ",0x00";// 校验字

	/**
	 * 获取收件人手机号
	 * 
	 * @return
	 */
	private String getReveiver() {
		String phone = "0"
				+ Long.toHexString(Long.valueOf(et_phone.getText().toString()
						.trim()));
		String relString = "";
		for (int i = 0; i < 5; i++) {
			relString += ",0x" + phone.substring(i * 2, 2 * i + 2);
		}
		return relString;
	}

	/**
	 * 获取短信内容
	 * 
	 * @return
	 */
	private String getMessageContent() {
		String et_cString = et_content.getText().toString().trim();
		String end = "";
		for (int i = 0; i < et_cString.length(); i++) {

			char c = et_cString.charAt(i);
			if (c >= ' ' && c <= '~') {// 数字

				end += Util.encode(String.valueOf(c));
			}
			// else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {//
			// 英文字母
			// }
			else {// 中字符
				end += Encoding.unicode2Gbk(String.valueOf(c));
			}
		}

		// String hexS = Util.encode(et_cString);
		return end;
	}

	/**
	 * 获取总包数
	 * 
	 * @return
	 */
	private String getTotalBao() {
		String message = getMessageContent();
		if (null != message) {
			int length = message.split(",").length;
			if (length == 1) {
				return "1";
			} else {
				int f = length / 13;
				int c = length % 13;
				return c > 0 ? String.valueOf(f + 2) : String.valueOf(f + 1);
			}
		}
		return "";
	}

	/**
	 * 获取总的字符串
	 * 
	 * @param total
	 *            总包数
	 * @param current
	 *            当前第几包
	 * @return
	 */
	private String getAllString(int total, int current) {// 2 0 1
		String messageALL = getMessageContent();
		int le = messageALL.split(",").length;// message length
		String mess = "";
		String content = "";
		// if (total - 1 > current) {// 多于1包
		content = getIndexString(current, messageALL);// 末尾多一个 分号
		// } else {
		// content = messageALL + ",";// 都加个分号
		// }

		if (current == 0) {// 第一次拼接 需要时间 发件人等等相关数据
			mess = homeMes + mes_leng + "0x" + Integer.toHexString(total) + ","
					+ "0x" + Integer.toHexString(current) + "," + "0x"
					+ Integer.toHexString(le) + "," + getTime() + getReveiver()
					+ "," + content;// 最后加上 校验字
		} else {
			mess = homeMes + mes_leng + "0x" + Integer.toHexString(total) + ","
					+ "0x" + Integer.toHexString(current) + "," + content;// 最后加上
																			// 校验字
		}

		return mess;
	}

	private byte[] getBytes(String content) {
		String[] dsa = content.split(",");
		byte[] by = new byte[dsa.length + 1];
		for (int i = 0; i < by.length; i++) {
			if (i == 3) {
				by[i] = (byte) Integer.parseInt(String.valueOf(getTotalBao()),
						16);// 总包长
			} else if (i != by.length - 1) {
				by[i] = (byte) Integer.parseInt(dsa[i].substring(2), 16);
			} else {
				by[i] = (byte) Integer.parseInt(String.valueOf(by.length - 1),
						16);// 校验字
			}
		}
		return by;
	}

	private String getIndexString(int cur, String content) {
		String[] ddStrings = content.split(",");
		// 第一包 最多一个字节 以后每包最多 13个字节 1 13 13 13 ......
		String end = "";// 0 1-13 14-27
		if (cur == 0) {// 1 14 27
			end = ddStrings[0];
		} else {
			for (int i = cur == 1 ? 1 : cur * 7; i <= (cur) * 13; i++) {
				if (i < ddStrings.length) {
					end += ddStrings[i] + ",";
				} else {
					return end + ",";
				}
			}
		}
		return end;
	}

	/**
	 * 获取发送时间
	 * 
	 * @return
	 */
	private String getTime() {
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR) - 2000;
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		return "0x" + Integer.toHexString(year) + ",0x"
				+ Integer.toHexString(month) + ",0x"
				+ Integer.toHexString(date) + ",0x" + Integer.toHexString(hour)
				+ ",0x" + Integer.toHexString(minute) + ",0x"
				+ Integer.toHexString(second);
	}

	/**
	 * 根据返回数据 确定类型
	 * 
	 * @param by
	 */
	private void receiveData(byte[] by) {
		String type = BackDatakUtil.getBackType(by);
		switch (type) {
		case "CN":// 连接握手发起CN

			break;
		case "AC":// 连接握手确认AC

			break;
		case "OM":// 收短信(未读短信)OM
			getMessageBack(by);
			break;
		case "BC":// 位置返回BC

			break;
		case "OR":// 获取最近一次线路OR

			break;
		case "BR":// 路线返回BR

			break;
		case "BS":// 状态返回BS

			break;

		}

	}

	private void getMessageBack(byte[] b) {
		Intent intent = new Intent();
		intent.setAction(BackDatakUtil.action);
		intent.putExtra("data", b);
		sendBroadcast(intent);
	}
}
