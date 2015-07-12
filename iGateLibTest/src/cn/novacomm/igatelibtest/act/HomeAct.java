package cn.novacomm.igatelibtest.act;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.base.BaseActivity;
import cn.novacomm.igatelibtest.fragment.ContactFragment;
import cn.novacomm.igatelibtest.fragment.MapFragment;

/**
 * @项目名:iGateLibTest
 * 
 * @类名:HomeAct.java
 * 
 * @创建人:pangerwei
 * 
 * @类描述: 主界面
 * 
 * @date:2015-7-12
 * 
 * @Version:1.0
 */
public class HomeAct extends BaseActivity {

	private MapFragment mapFragment;
	private ContactFragment contactFragment;

	private ImageView iv_map, iv_message, iv_sos;

	private TextView tv_map, tv_message;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	private LinearLayout ll_left, ll_right;

	private int currentFragment = 0;

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		ll_left = getView(R.id.ll_left);
		ll_right = getView(R.id.ll_right);
		tv_map = getView(R.id.tv_map);
		tv_message = getView(R.id.tv_message);
		iv_map = getView(R.id.iv_map);
		iv_message = getView(R.id.iv_message);
		iv_sos = getView(R.id.iv_sos);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		ll_left.setOnClickListener(this);
		ll_right.setOnClickListener(this);
		iv_sos.setOnClickListener(this);
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		setTabSelection(currentFragment);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected int loadView() {
		// TODO Auto-generated method stub
		return R.layout.act_home;
	}

	@Override
	public void onKeyEv(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.ll_left:
			// if (currentFragment != 0) {
			setTabSelection(0);
			currentFragment = 0;
			// }
			break;
		case R.id.ll_right:
			if (currentFragment != 1) {
				setTabSelection(1);
				currentFragment = 1;
			}
			break;
		case R.id.iv_sos:// 一键求救

			break;

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示事件地点，1表示路径导航，2表示当前图片，3表示推送视频。
	 */
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:

			iv_map.setBackgroundResource(R.drawable.dt_xz);
			tv_map.setTextColor(getResources().getColor(R.color.botton_color));

			if (null == mapFragment) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mapFragment = new MapFragment();
				transaction.add(R.id.content, mapFragment, "messageFragment");
			} else {
				transaction.show(mapFragment);
			}
			break;
		case 1:
			// 当点击了tab时，改变控件的图片和文字颜色
			iv_message.setBackgroundResource(R.drawable.th_xz);
			tv_message.setTextColor(getResources().getColor(
					R.color.botton_color));
			if (null == contactFragment) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				contactFragment = new ContactFragment();
				transaction.add(R.id.content, contactFragment,
						"contactFragment");
			} else {
				transaction.show(contactFragment);
			}
			break;

		}
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (null != mapFragment) {
			transaction.hide(mapFragment);
		}
		if (null != contactFragment) {
			transaction.hide(contactFragment);
		}

	}

}
