package frame.http.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpResultBean {

	private String result;
	
	public HttpResultBean(String result){
		this.result=result;
	}
	
	
	public String getString(){
		return result;
	}
	
	
	public JSONObject getJSONObject(){
		if(result==null)return null;
		try {
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray getJSONArray(){
		if(result==null)return null;
		try {
			return new JSONArray(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public JSONObject getJSONArrayToJSONObject(String key){
		if(result==null)return null;
		try {
			JSONObject jobj=new JSONObject();
			jobj.put(key, new JSONArray(result));
			
			return jobj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
