package frame.base;

import android.content.Context;
import android.widget.BaseAdapter;
import frame.base.bean.PageList;
import frame.util.Cache;

public abstract class PageListAdapter<T> extends BaseAdapter {

	private PageList<T> pageList;
	protected Context context;
	
	public PageListAdapter(Context context,PageList<T> pageList){
		this.pageList=pageList;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return pageList.size();
	}
	
	public  T get(int position) {
		return pageList.get(position);
	}

	@Override
	public Object getItem(int position) {
		return pageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
 
	public void refreshPageList(PageList<T> list){
		if(pageList!=null){
			pageList.setPageList(list);
			Cache.put("pageList",pageList);
			notifyDataSetChanged();
		}
	}

	public void addPageList(PageList<T> list){
		if(pageList!=null){
			pageList.addPageList(list);
			Cache.put("pageList", pageList);
			notifyDataSetChanged();
		}
	}
	

	public PageList<T> getPageList() {
		return pageList;
	}

	public void setPageList(PageList<T> pageList) {
		this.pageList = pageList;
	}

	public abstract String getPageFlag();
	
	public abstract String getDefaulePageFlag();
}
