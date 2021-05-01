import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import whu.sres.model.Room;

public class TestRoom {
	Calendar c = Calendar.getInstance();
	private Room room;
	
	Date date;
	String roomNUm = "504";
	String time_use = "晚上";
	
	@Before
	public void initPara(){
		c.set(2012, 0, 3); //2002- 1-13
		date = c.getTime();			
		room = new Room();
	}

	
	@Test
	public void testTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println(sdf.format(date));
	}
	
	@Test
	public void testIsUseful(){
		/*
		Room r = new Room("2013-1-3", "晚上", "504");		
		Assert.assertFalse(r.isUseful());
		r.setRoom("304");
		Assert.assertTrue(r.isUseful());
		*/
	}
	
	@Test
	public void testInsertRoom(){
		//2014-8-20, 220,7:00,9:00,田沁,123423
		String bookingDate = "2014-8-20";
		String bookingRoom = "220";
		String startTime = "7:00";
		String endTime = "9:00";
		String user = "王希望";
		String userID = "8917";
		String phoneNum = "123643243";
		String actualUser = "田沁";
		Room r = new Room(bookingDate,bookingRoom,startTime,endTime,user,"1",phoneNum,userID,actualUser);
		boolean res = r.insertRoom();
		if(res) {
			System.out.println("成功预定。");
		}else {
			System.out.println("预定失败");
		}
	}
	
	@Test
	public void testGetRoomByDate(){
	
		ArrayList<Room> list = room.getRoomByDate("2017-08-28");
		for(Room r: list){
			System.out.println("room user: " + r.getUser());
		}
		
	}
	
	@Test
	public void testGetUnavailableRoomsByDateRoom() {
		String date = "2017-08-28";
		String room = "320";
		Room r = new Room();
		TreeSet<Integer> set = r.getUnavailableRoomsByDateRoom(date, room);
		for(int i:set) {
			System.out.println(i);
		}
		r.close();
		
	}

}
