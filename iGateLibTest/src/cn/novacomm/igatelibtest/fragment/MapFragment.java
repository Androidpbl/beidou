package cn.novacomm.igatelibtest.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.novacomm.igatelibtest.R;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * @项目名:iGateLibTest
 * 
 * @类名:MapFragment.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述: 地图
 * 
 * @date:2015-7-13
 * 
 * @Version:1.0
 */
@SuppressLint({ "NewApi", "ValidFragment" })
public class MapFragment extends SupportMapFragment implements LocationSource,
		AMapLocationListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private UiSettings mUiSettings;
	private TextView tv_left, tv_ly, tv_more, tv_zj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle arg2) {
		// TODO Auto-generated method stub
		View settingLayout = inflater
				.inflate(R.layout.fg_map, container, false);
		mapView = (MapView) settingLayout.findViewById(R.id.map);
		mapView.onCreate(arg2);
		tv_left = (TextView) settingLayout.findViewById(R.id.tv_left);
		tv_ly = (TextView) settingLayout.findViewById(R.id.tv_ly);
		tv_more = (TextView) settingLayout.findViewById(R.id.tv_more);
		tv_zj = (TextView) settingLayout.findViewById(R.id.tv_zj);
		setUpMapIfNeeded();
		setListener();
		return settingLayout;
	}

	private void setListener() {
		tv_left.setOnClickListener(this);
		tv_ly.setOnClickListener(this);
		tv_more.setOnClickListener(this);
		tv_zj.setOnClickListener(this);
	}

	@SuppressLint("ValidFragment")
	public MapFragment() {

	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();

	}

	private void setUpMapIfNeeded() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(getResources().getColor(R.color.botton_color));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 224, 231, 237));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		// aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()
		mUiSettings = aMap.getUiSettings();
//		mUiSettings.setCompassEnabled(true);
		// mUiSettings.se
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_left:// 用户

			break;
		case R.id.tv_ly:// 蓝牙

			break;
		case R.id.tv_more:// 更多

			break;
		case R.id.tv_zj:// 足迹

			break;
		}
	}

}
