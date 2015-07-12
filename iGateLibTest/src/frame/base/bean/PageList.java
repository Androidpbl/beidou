package frame.base.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class PageList<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5239968360684098813L;
	
	/**
	 * 分页标志
	 */
//	public  int pageFlag;
	public  String nextPageFlag; 
	private boolean hasNext;
	private Vector<T> list=new Vector<T>();
	
	private NextPageListener<T> nextPageListener;
	
	/**
	 * 缓存
	 */
	private String key;
	
	public PageList(){
		
	}
	
	public PageList(String key){
		
	}
	
	
	
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	public Vector<T> getList() {
		if(list==null)list=new Vector<T>();
		return list;
	}
	public void setList(Vector<T> list) {
		this.list = list;
	}
	
	public void addALL(List<T> list){
		getList().addAll(list);
	}
 
	public int size(){
		if(list==null)return 0;
		return list.size();
	}
	
	
	public T get(int position){
		if(list==null)return null;
		return list.get(position);
	}
	
	
	
	public String getNextPageFlag() {
		if(nextPageListener!=null){
			nextPageFlag=nextPageListener.getNextPageFlag(this);
			return nextPageFlag;
		}
		
		return nextPageFlag;
	}

	public void setNextPageFlag(int nextPageFlag) {
		this.nextPageFlag = nextPageFlag+"";
	}
	public void setNextPageFlag(String nextPageFlag) {
		this.nextPageFlag = nextPageFlag;
	}

	
	public NextPageListener<T> getNextPageListener() {
		return nextPageListener;
	}

	public void setNextPageListener(NextPageListener<T> nextPageListener) {
		this.nextPageListener = nextPageListener;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPageList(PageList<T> pageList){
		this.hasNext=pageList.hasNext;
		this.list.clear();
		this.list.addAll(pageList.getList());
		this.nextPageFlag=pageList.nextPageFlag;
	}
	
	public void addPageList(PageList<T> pageList){
		this.hasNext=pageList.hasNext;
		this.list.addAll(pageList.getList());
		this.nextPageFlag=pageList.nextPageFlag;
	}
	
	
	public interface NextPageListener<T> extends Serializable{
		public String getNextPageFlag(PageList<T> pagelist);
	}
	
}
