package cn.novacomm.igatelibtest.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.act.MessageBoxList;
import cn.novacomm.igatelibtest.adapter.ContactHomeAdapter;
import cn.novacomm.igatelibtest.base.BaseActivity;
import cn.novacomm.igatelibtest.base.BaseFragment;
import cn.novacomm.igatelibtest.bean.ContactBean;
import cn.novacomm.igatelibtest.view.ui.QuickAlphabeticBar;

/**    
 * @项目名:iGateLibTest  
 * 
 * @类名:ContactFragment.java  
 * 
 * @创建人:Administrator 
 *
 * @类描述:联系人fragment
 * 
 * @date:2015年7月12日
 * 
 * @Version:1.0 
 */ 
public class ContactFragment extends BaseFragment {

    /**
     * 联系人列表
     */
    private ListView lv;
    
    /**
     * 索引条
     */
    private QuickAlphabeticBar quickAlphabeticBar;
    
    /**
     * 异步查询联系人数据
     */
    private AsyncQueryHandler asyncQuery;
    
    private Map<Integer, ContactBean> contactIdMap = null;
    private List<ContactBean> list;
    private ContactHomeAdapter adapter;
    private TextView tv_title;
    
    /**
     * 当前索引选中值
     */
    private TextView tv_fast_position;
    @Override
    protected void findViews(View view) {
        
        lv=(ListView) view.findViewById(R.id.fragment_contact_list);
        quickAlphabeticBar=(QuickAlphabeticBar) view.findViewById(R.id.fast_scroller);
        tv_fast_position=(TextView) view.findViewById(R.id.fast_position);
        asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
        tv_title=(TextView) view.findViewById(R.id.title);
        tv_title.setText("联系人");
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void getData() {
        init();
    }

    @Override
    protected int loadView() {
        return R.layout.fragment_home_contact;
    }

    @Override
    public void onKeyEv(View v) {
        // TODO Auto-generated method stub

    }
    
    
    /**
     * 初始化联系人数据
     */
    private void init(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
        String[] projection = { 
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        }; // 查询的列
        asyncQuery.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
    }
    
    /**
     * 数据库异步查询类AsyncQueryHandler
     * 
     * @author administrator
     * 
     */
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        /**
         * 查询结束的回调函数
         */
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                
                contactIdMap = new HashMap<Integer, ContactBean>();
                
                list = new ArrayList<ContactBean>();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (contactIdMap.containsKey(contactId)) {
                        
                    }else{
                        
                        ContactBean cb = new ContactBean();
                        cb.setDisplayName(name);
//                  if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
//                      cb.setPhoneNum(number.substring(3));
//                  } else {
                        cb.setPhoneNum(number);
//                  }
                        cb.setSortKey(sortKey);
                        cb.setContactId(contactId);
                        cb.setPhotoId(photoId);
                        cb.setLookUpKey(lookUpKey);
                        list.add(cb);
                        
                        contactIdMap.put(contactId, cb);
                        
                    }
                }
                if (list.size() > 0) {
                    setAdapter(list);
                }
            }
        }

    }
    
    private void setAdapter(List<ContactBean> list) {
        adapter = new ContactHomeAdapter(getActivity(), list, quickAlphabeticBar);
        lv.setAdapter(adapter);
        quickAlphabeticBar.init(getActivity());
        quickAlphabeticBar.setListView(lv);
        quickAlphabeticBar.setHight(quickAlphabeticBar.getHeight());
        quickAlphabeticBar.setVisibility(View.VISIBLE);
        
        //TODO 点击事件
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactBean cb = (ContactBean) adapter.getItem(position);
                String threadId = getSMSThreadId(cb.getPhoneNum());
                Intent intent =new Intent(getActivity(), MessageBoxList.class);
                intent.putExtra("phoneNumber", cb.getPhoneNum());
                intent.putExtra("threadId", threadId);
                startActivity(intent);
            }
        });
    }
    
    public static String[] SMS_COLUMNS = new String[]{  
        "thread_id"
    };
    private String getSMSThreadId(String adddress){
        Cursor cursor = null;  
        ContentResolver contentResolver = getActivity().getContentResolver();  
        cursor = contentResolver.query(Uri.parse("content://sms"), SMS_COLUMNS, " address like '%" + adddress + "%' ", null, null);  
        String threadId = "";
        if (cursor == null || cursor.getCount() > 0){
            cursor.moveToFirst();
            threadId = cursor.getString(0);
            cursor.close();
            return threadId;
        }else{
            cursor.close();
            return threadId;
        }
    }


}
