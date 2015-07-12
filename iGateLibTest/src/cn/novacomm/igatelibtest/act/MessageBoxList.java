package cn.novacomm.igatelibtest.act;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.novacomm.igatelibtest.R;
import cn.novacomm.igatelibtest.adapter.MessageBoxListAdapter;
import cn.novacomm.igatelibtest.bean.MessageBean;

/**
 * @项目名:iGateLibTest
 * 
 * @类名:MessageBoxList.java
 * 
 * @创建人:Administrator
 * 
 * @类描述:短信收发列表
 * 
 * @date:2015年7月12日
 * 
 * @Version:1.0
 */
public class MessageBoxList extends Activity {

    private ListView talkView;
    private List<MessageBean> list = null;
    private Button fasong;
    private EditText neirong;
    private SimpleDateFormat sdf;
    private AsyncQueryHandler asyncQuery;
    private String address;

    private TextView tv_title;
    private TextView tv_back;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sms_messageboxlist);

        fasong = (Button) findViewById(R.id.fasong);
        neirong = (EditText) findViewById(R.id.neirong);

        String thread = getIntent().getStringExtra("threadId");
        address = getIntent().getStringExtra("phoneNumber");
        tv_title = (TextView) findViewById(R.id.title);
        tv_back = (TextView) findViewById(R.id.back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(getPersonName(address));

        sdf = new SimpleDateFormat("MM-dd HH:mm");

        init(thread);

        fasong.setOnClickListener(new OnClickListener() {//TODO 发送

            public void onClick(View v) {
                String nei = neirong.getText().toString();
                ContentValues values = new ContentValues();
                values.put("address", address);
                values.put("body", nei);
                getContentResolver().insert(Uri.parse("content://sms/sent"),
                        values);
                Toast.makeText(MessageBoxList.this, nei, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void init(String thread) {

        asyncQuery = new MyAsyncQueryHandler(getContentResolver());

        talkView = (ListView) findViewById(R.id.list);
        list = new ArrayList<MessageBean>();

        Uri uri = Uri.parse("content://sms");
        String[] projection = new String[] { "date", "address", "person",
                "body", "type" };// 查询的列
        asyncQuery.startQuery(0, null, uri, projection,
                "thread_id = " + thread, null, "date asc");
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

        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String date = sdf.format(new Date(cursor.getLong(cursor
                            .getColumnIndex("date"))));
                    if (cursor.getInt(cursor.getColumnIndex("type")) == 1) {
                        MessageBean d = new MessageBean(
                                cursor.getString(cursor
                                        .getColumnIndex("address")),
                                date,
                                cursor.getString(cursor.getColumnIndex("body")),
                                R.layout.list_say_he_item);
                        list.add(d);
                    } else {
                        MessageBean d = new MessageBean(
                                cursor.getString(cursor
                                        .getColumnIndex("address")),
                                date,
                                cursor.getString(cursor.getColumnIndex("body")),
                                R.layout.list_say_me_item);
                        list.add(d);
                    }
                }
                if (list.size() > 0) {
                    talkView.setAdapter(new MessageBoxListAdapter(
                            MessageBoxList.this, list));
                    talkView.setDivider(null);
                    talkView.setSelection(list.size());
                } else {
                    Toast.makeText(MessageBoxList.this, "没有短信进行操作",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getPersonName(String number) {
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, };
        Cursor cursor = this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number
                        + "'", null, null);
        if (cursor == null) {
            return number;
        }
        String name = number;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        cursor.close();
        return name;
    }

}
