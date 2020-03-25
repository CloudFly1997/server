package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pojo.Message;

/**
 * @author jack
 */
public class MessageHandle {
	private static final String SPLIT_CODE = "@AA@";

	public static String afterHandleMessage(String type, String from, String to, String date, String content) {
		String result = type + SPLIT_CODE + from + SPLIT_CODE + to + SPLIT_CODE + date + SPLIT_CODE + content;
		return result;
	}

	public static List<String> getNeedToPushUsers(String message) {
		List<String> users = new ArrayList<String>();
		users = Arrays.asList(message.split(SPLIT_CODE)[2].split(","));
		return users;
	}

	public static String getMessageType(String message) {
		return message.split(SPLIT_CODE)[0];
	}


	public static Message getMessageObject(String message) {
		String type = message.split(SPLIT_CODE)[0];
		String from = message.split(SPLIT_CODE)[1];
		String to = message.split(SPLIT_CODE)[2];
		String date = message.split(SPLIT_CODE)[3];
		String content = message.split(SPLIT_CODE)[4];
		Message messageObj = new Message(type, from, to, content, date);
		return messageObj;
	}
}
