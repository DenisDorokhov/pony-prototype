package net.dorokhov.pony.web.client.common;

public class TimeUtils {

	public static String secondsToMinutes(int aSeconds) {

		int minutes = aSeconds / 60;
		int seconds = aSeconds - minutes * 60;

		StringBuilder buf = new StringBuilder();

		buf.append(minutes).append(":");

		if (seconds <= 9) {
			buf.append("0");
		}
		buf.append(seconds);

		return buf.toString();
	}

	public static String secondsToHours(int aSeconds) {

		int hours = aSeconds / (60 * 60);
		int minutes = (aSeconds - hours * 60) / 60;
		int seconds = aSeconds - hours * 60 * 60 - minutes * 60;

		StringBuilder buf = new StringBuilder();

		buf.append(hours).append(":");

		if (minutes <= 9) {
			buf.append("0");
		}
		buf.append(minutes).append(":");

		if (seconds <= 9) {
			buf.append("0");
		}
		buf.append(seconds);

		return buf.toString();
	}
}
