package com.example.app4;

public class Time {
	private int hours;
	private int minutes;
	private int seconds;
	private int milliseconds;

	public Time(int hours, int minutes, int seconds, int milliseconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliseconds = milliseconds;
	}

	public int getHours() {
		return hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public int getMilliseconds() { return milliseconds; }

	public void addSecond() {
		seconds++ ;
		minutes += seconds/60;
		seconds %= 60 ;
		hours += minutes/60;
		minutes %= 60 ;
	}
}
