package com.slamke.afterservice.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class CardNumGetter {
	/**
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";

	private Context context;// 这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。

	public CardNumGetter(Context _context) {
		this.context = _context;
	}

	/**
	 * Role:获取短信的各种信息 <BR>
	 */
	public List<SmsInfo> getSmsInfo() {
		List<SmsInfo> infos = new ArrayList<SmsInfo>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = resolver.query(uri, new String[] { "_id", "address",
				"date", "type", "body" }, null, null, " date desc ");
		SmsInfo info;
		while (cursor.moveToNext()) {
			info = new SmsInfo();
			String id = cursor.getString(0);
			String address = cursor.getString(1);
			String date = cursor.getString(2);
			int type = cursor.getInt(3);
			String body = cursor.getString(4);
			info.setId(id);
			info.setAddress(address);
			info.setDate(date);
			info.setType(type);
			info.setBody(body);
			infos.add(info);
		}
		cursor.close();
		return infos;
	}

	/**
	 * Role:获取本机号码 <BR>
	 */
	public String getSIMCardNum(String address,long standardDate) {
		String result = "";
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "_id", "address",
				"person", "date", "type", "body" }, null, null, " date desc ");
		while (cursor.moveToNext()) {
			String num = cursor.getString(1).replace(" ", "");
			long longDate = cursor.getLong(3);
			Log.i("num", num);
			if (num.startsWith("+") && num.length() > 10) {
				num = num.substring(3);
			}
			Log.i("num", num);
			// ALL = 0; INBOX = 1; SENT = 2; DRAFT = 3; OUTBOX = 4; FAILED = 5;
			// QUEUED = 6;
			//条件：1、接收的短信；2、时间在后；3、地址
			if (address.equals(num) && cursor.getInt(4) == 1 && longDate > standardDate) {
				String temp = cursor.getString(5);
				Log.i("temp", temp);
				//if (temp.startsWith("SIMUIM")) {
				if (temp.contains("SIMUIM")) {
					int index = temp.indexOf("SIMUIM");
					result = temp.substring(index+6);
					result = result.substring(0, firstNotNum(result));
					break;
				} else {
					continue;
				}

			}
		}
		cursor.close();
		return result;
	}

	public int firstNotNum(String str) {
		if (str == null) {
			return -1;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return i;
			}
		}
		return str.length();
	}

	public class SmsInfo {
		private String id;
		private String address;
		private String date;
		private int type;
		private String body;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
}
