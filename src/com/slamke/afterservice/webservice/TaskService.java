package com.slamke.afterservice.webservice;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.slamke.afterservice.util.Message;

public class TaskService {
	private HttpClient httpclient;
	private final static String TEL = "tel";
	private final static String NUM = "num";
	
	private String url;
	private final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时5秒钟
	private final int SO_TIMEOUT = 30 * 1000; // 设置等待数据超时时间5秒钟
	public TaskService(String url) {
		BasicHttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("charset", HTTP.UTF_8); 
		HttpConnectionParams.setConnectionTimeout(httpParams,
				REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		httpclient = new DefaultHttpClient(httpParams);
		this.url = url;
	}

	public String getTaskList(String tel) {
		try {
			Log.i("url", url);
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader(TEL,tel);		
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity1 = response.getEntity();
			InputStream instream = entity1.getContent();
			String jaxrsmessage = "";
			jaxrsmessage = Reader.read(instream);
			Log.i("jaxrsmessage", jaxrsmessage);
			httpGet.abort();
			return jaxrsmessage;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return Message.NETWORK_FAIL;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Message.NETWORK_FAIL;
	}
	
	public String getTaskStatus(String tel,String num) {
		try {
			Log.i("url", url);
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "application/json");
			httpGet.addHeader(TEL,tel);	
			httpGet.addHeader(NUM,num);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity1 = response.getEntity();
			InputStream instream = entity1.getContent();
			String jaxrsmessage = "";
			jaxrsmessage = Reader.read(instream);
			Log.i("jaxrsmessage", jaxrsmessage);
			httpGet.abort();
			return jaxrsmessage;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return Message.NETWORK_FAIL;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Message.NETWORK_FAIL;
	}
}
