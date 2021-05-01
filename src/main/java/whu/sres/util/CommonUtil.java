package whu.sres.util;

import java.util.TreeSet;

public class CommonUtil {
	
	/**
	 * 将一个时间段转换成下标索引。
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static TreeSet<Integer> period2seq(String startTime,String endTime) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		String[] starts=startTime.split(":");
		int start_hours = Integer.parseInt(starts[0]);
		int start_mins = Integer.parseInt(starts[1]);
		
		String[] ends=endTime.split(":");
		int end_hours = Integer.parseInt(ends[0]);
		int end_mins = Integer.parseInt(ends[1]);
		
		for(int i = start_hours*60  + start_mins + 30;i<=end_hours*60+end_mins;) {
			int baseline = 7*60;
			int j = (i-baseline)/30;
			set.add(j);
			i = i+30;
		}
		
		return set;
		
	}
	
	/**
	 * Find the start time according to its index. 
	 * @param index
	 * @return
	 */
	public static String index2StartTime(String index) {
		int i = Integer.parseInt(index);
		int mins = (i-2)*30 + 7*60+30;
		int h;
		int m;
		String result ;
		if(mins % 60 == 0) {
			//mins = 0;
			result = mins/60 + ":00";
		}else {
			result = (mins - 30)/60 + ":30";
		}
		return result;
	}
	
	/**
	 * Find the start time according to its index. 
	 * @param index
	 * @return
	 */
	public static String index2StartTime(int index) {;
		int mins = (index-2)*30 + 7*60+30;
		int h;
		int m;
		String result ;
		if(mins % 60 == 0) {
			//mins = 0;
			result = mins/60 + ":00";
		}else {
			result = (mins - 30)/60 + ":30";
		}
		return result;
	}
	
	
	
	public static String int2String(int i) {
		if(i < 10) {
			return "0"+i;
		}else {
			return Integer.toString(i);
		}
	}
	
	
	public static  String getDay(int i){
		String str="星期";
		switch(i) {
		case 1:
			str = str+"天";
			break;
		case 2:
			str = str+"一";
			break;
		case 3:
			str = str+"二";
			break;
		case 4:
			str = str+"三";
			break;
		case 5:
			str = str+"四";
			break;
		case 6:
			str = str+"五";
			break;
		case 7:
			str = str+"六";
			break;
		}
		return str;	
	}
	
	/*public static JSONArray rooms2Json(ArrayList<Room> rooms) {
		JSONArray result = new JSONArray();
		for(Room r:rooms) {
			JSONObject o = new JSONObject();
			o.put("userName", r.getUser());
			o.put("recordId", r.getRowId());
			o.put("actualUser", r.getActualUser());
			o.put("phoneNum", r.getPhoneNum());
			
			JSONArray period = new JSONArray();
			for(Integer i:CommonUtil.period2seq(r.getStartTime(),r.getEndTime())) {
				period.put(r.getRoom()+CommonUtil.int2String(i));
			}
			
			o.put("record", period);
			result.put(o);
		}
		
		
		return result;
	}
	*/
	
	/*public static JSONArray set2Json(TreeSet<Integer> set) {
		JSONArray result = new JSONArray();
		for(Integer i:set) {
			result.put(i.toString());
		}
		return result;
	}*/
	
}
