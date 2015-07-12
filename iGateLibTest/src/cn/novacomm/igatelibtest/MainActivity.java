package cn.novacomm.igatelibtest;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.widget.TextView;
import cn.novacomm.ble.iGate;
import cn.novacomm.ble.iGateCallBacks;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.R.id;
import cn.novacomm.igatelibtest.R.layout;
import cn.novacomm.igatelibtest.R.menu;
import cn.novacomm.igatelibtest.R.string;

public class MainActivity extends Activity implements iGateCallBacks {
	private static final int ENABLE_BT_REQUEST_ID = 1;

	private TextView mIGateState;
	private TextView mReceivedData;
	private Button mButtonSend;
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


	private byte[] testBytes = new byte[] { 0x24, 0x53, 0x4d, 0x0e, 0x01, 0x00,//6
			0x01, 0x0f, 0x06, 0x11, 0x0b, 0x25, 0x00, 0x03, 0x7e, (byte) 0x21,//10
			(byte) 0xef, (byte) 0x79, (byte) 0x41, 0x0d };//4

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mIGateState = (TextView) findViewById(R.id.textViewState);
		mReceivedData = (TextView) findViewById(R.id.textViewRxData);

		mButtonSend = (Button) findViewById(R.id.buttonSend);
		mButtonSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.editTextSendStr);
				String message = view.getText().toString();
				if (sendIndex < mConnectedBluetoothDevicesAddress.size()) {
					// mIgate.iGateDeviceSendData(mConnectedBluetoothDevicesAddress.get(sendIndex),
					// message.getBytes());
					mIgate.iGateDeviceSendData(
							mConnectedBluetoothDevicesAddress.get(sendIndex),
							testBytes);
					// mIgate.iGateDeviceGetState(mConnectedBluetoothDevicesAddress.get(sendIndex));
					// mIgate.iGateDeviceGetRemoteRssi(mConnectedBluetoothDevicesAddress.get(sendIndex));
					// mIgate.iGateDeviceSetImmediateAlertLevel(mConnectedBluetoothDevicesAddress.get(sendIndex),
					// (byte)2);
					// mIgate.iGateDeviceGetLinklossAlertLevel(mConnectedBluetoothDevicesAddress.get(sendIndex));
					// mIgate.iGateDeviceSetLinklossAlertLevel(mConnectedBluetoothDevicesAddress.get(sendIndex),
					// (byte)2);
					// mIgate.iGateDeviceGetTxPower(mConnectedBluetoothDevicesAddress.get(sendIndex));
					// Log.i("------",String.format("iGate State %d",mIgate.iGateDeviceGetState(mConnectedBluetoothDevicesAddress.get(sendIndex)).ordinal()));
				} else {
					sendIndex = 0;
					if (mConnectedBluetoothDevicesAddress.size() > 0) {
						mIgate.iGateDeviceSendData(
								mConnectedBluetoothDevicesAddress
										.get(sendIndex), message.getBytes());
						Log.i("------", String.format(
								"iGate State %d",
								mIgate.iGateDeviceGetState(
										mConnectedBluetoothDevicesAddress
												.get(sendIndex)).ordinal()));
					}
				}
				sendIndex++;
				if (sendIndex == mConnectedBluetoothDevicesAddress.size()) {
					sendIndex = 0;
				}
			}
		});

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (mIgate.getIGateState() == iGateHostState.iGateHostStateSearching) {
			menu.findItem(R.id.scanning_start).setVisible(false);
			menu.findItem(R.id.scanning_stop).setVisible(true);
			menu.findItem(R.id.scanning_indicator).setActionView(
					R.layout.progress_indicator);

		} else {
			menu.findItem(R.id.scanning_start).setVisible(true);
			menu.findItem(R.id.scanning_stop).setVisible(false);
			menu.findItem(R.id.scanning_indicator).setActionView(null);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scanning_start:
			mIgate.startScanning(false);
			break;
		case R.id.scanning_stop:
			mIgate.stopScanning();
			break;
		}

		invalidateOptionsMenu();
		return true;
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
					 mReceivedData.append(tmpValue);
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

}
