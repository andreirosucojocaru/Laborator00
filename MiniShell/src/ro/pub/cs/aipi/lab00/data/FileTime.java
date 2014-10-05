package ro.pub.cs.aipi.lab00.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class FileTime {

	final private int year;
	final private int month;
	final private int dayOfMonth;
	final private int hour;
	final private int minute;
	
	private void check(int year,
			int month,
			int dayOfMonth,
			int hour,
			int minute) {
		SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy hh:mm");
		try {
			format.parse(dayOfMonth+"/"+month+"/"+year+" "+hour+":"+minute);
		} catch(ParseException exception) {
			throw new IllegalArgumentException();
		}
	}
	
	public FileTime(int year,
			int month,
			int dayOfMonth,
			int hour,
			int minute) {
		check(year, month, dayOfMonth, hour, minute);
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.hour = hour;
		this.minute = minute;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public String toString() {
		return ((dayOfMonth<10)?"0":"")+dayOfMonth+"/"+((month<10)?"0":"")+month+"/"+((year<10)?"0":"")+year+" "+((hour<10)?"0":"")+hour+":"+((minute<10)?"0":"")+minute;
	}

}
