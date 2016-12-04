package com.slamke.afterservice;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.domain.Task;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.ConnectionDetector;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.webservice.TaskService;
/**
 * 进度查询用---提交前，进行网络状态检测;网络差时，不可用短信发送
 * @author sunke
 *
 */
public class ProgressActivity extends Activity implements OnClickListener {
	private Button btnBack;
	private Button btnSubmit;
	private Spinner spTaskNo;
	private String taskNo = "";
	private String[] taskNum;
	private TextView progressContent;
	
	private ViewStub vsStub;
	private ScrollView body;

	private TextView txtTitle;
	private PopupWindow popup;
	private WindowManager.LayoutParams lp;
	private Window mWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWindow = getWindow();
		lp = mWindow.getAttributes();

		setContentView(R.layout.activity_progress);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		btnSubmit.setVisibility(View.GONE);
		
		body = (ScrollView) findViewById(R.id.body);
		progressContent = (TextView) findViewById(R.id.progress_content);
		//progressContent.setVisibility(View.GONE);
		
		spTaskNo = (Spinner) findViewById(R.id.sp_no);
		spTaskNo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2>0) {
					taskNo = taskNum[arg2];
					initTask();
					submitProgressSearch();
				}else {
					progressContent.setVisibility(View.GONE);
				}
				//btnSubmit.setVisibility(View.VISIBLE);
				//progressContent.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				//taskNo = taskNum[0];
				taskNo = null;
			}
		});

		initPopupMenu();	
		vsStub = (ViewStub) findViewById(R.id.vs);
		View parent = vsStub.inflate();
		TextView titleView = (TextView) parent.findViewById(R.id.txt_title);
		titleView.setText(Message.TIP_LOADING_DATA);
		initData();
		progressContent.setVisibility(View.GONE);
	}

	private void initData() {
			vsStub.setVisibility(View.VISIBLE);
			body.setVisibility(View.GONE);
			AsyncTask<String, Void, String> simpleGetTask = new AsyncTask<String, Void, String>() {
				@Override
				protected String doInBackground(String... params) {
					try {
						SharedPreferences sp = getSharedPreferences(
								Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
						String server = sp.getString(Message.PREFERENCE_SERVER,
								"");
						String numberCustomer = sp.getString(Message.PREFERENCE_PHONE_NUMBER, "");
						if (server.equals("")) {
							server = getResources().getString(R.string.default_IPAddress)+getResources().getString(R.string.default_service);
						}else {
		    				server = server + getResources().getString(R.string.default_service);
						}
						server += "/task/progress_list";
						Log.i("server", server);						
						String result = Message.ERROR;
		    			TaskService service = new TaskService(server);
		                for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
							result = service.getTaskList(numberCustomer);
							if (result.equals(Message.NETWORK_FAIL)) {
								continue;
							}else {
								break;
							}
						}
		                return result;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return Message.NETWORK_FAIL;
					}

				}

				@Override
				protected void onPostExecute(String result) {
					try {
						result = URLDecoder.decode(result, "UTF-8");
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					Log.i("result", result);
					if (result.equals(Message.ERROR)
							|| result.equals(Message.NETWORK_FAIL)) {
						Toast.makeText(ProgressActivity.this, Message.NETWORK_ERROR_TRY_AGAIN,
								Toast.LENGTH_SHORT).show();
						spTaskNo.setEnabled(false);
						btnSubmit.setClickable(false);
					}else if (result.equals(Message.SUCCESS)) {
						Toast.makeText(ProgressActivity.this, Message.NO_UNCLOSED_TASK,
								Toast.LENGTH_SHORT).show();
						spTaskNo.setEnabled(false);
						btnSubmit.setClickable(false);
					}else{
						ClassParse parse = new ClassParse();
						List<Task> tasks = parse.string2TaskList(result);
						if (tasks != null && tasks.size() > 0){
							List<String> tempList = new ArrayList<String>();
							tempList.add(getResources().getString(R.string.please_select));
							for (int i = 0; i < tasks.size(); i++) {
								tempList.add(tasks.get(i).getNum());
							}
							taskNum = new String[]{""};
							taskNum = tempList.toArray(taskNum);
							//taskNo = tasks.get(0).getNum();
							taskNo = null;
							SpinnerAdapter adapterCategory = new SpinnerAdapter(
									ProgressActivity.this,
									android.R.layout.simple_spinner_item, taskNum);
							adapterCategory
									.setDropDownViewResource(android.R.layout.browser_link_context_header);
							spTaskNo.setAdapter(adapterCategory);
						}else {
							Toast.makeText(ProgressActivity.this, Message.NO_UNCLOSED_TASK,
									Toast.LENGTH_SHORT).show();
							spTaskNo.setEnabled(false);
							btnSubmit.setClickable(false);
						}
					}
					vsStub.setVisibility(View.GONE);
					body.setVisibility(View.VISIBLE);
				}

			};
			simpleGetTask.execute();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goHome();
			return false;
		}
		return false;
	}

	private void goHome() {
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			goHome();
			break;
		case R.id.btn_submit:
			//******检测网络*****//
			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
			Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
			if (!isInternetPresent) {
				Toast.makeText(ProgressActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
				return;
			}
			//******检测网络*****//
			btnSubmit.setClickable(false);
			initTask();
			submitProgressSearch();
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (popup != null && popup.isShowing()) {
			popup.dismiss();
		}
	}

	/**
	 * 复写onActivityResult，这个方法 是要等到SimpleTaskActivity点了提交过后才会执行的
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (44 == resultCode) {
			setResult(44);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 创建Popup Menu菜单
	 */
	private void initPopupMenu() {
		Context mContext = ProgressActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View mPopupWindow = mLayoutInflater.inflate(R.layout.progress, null);
		txtTitle = (TextView) mPopupWindow.findViewById(R.id.txt_title);
		popup = new PopupWindow(mPopupWindow, 300, 300);
		popup.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		popup.update();
	}

	private void windowFadeIn(Window mWindow, WindowManager.LayoutParams lp) {
		lp.dimAmount = 0.5f;
		lp.alpha = 0.5f;
		mWindow.setAttributes(lp);
	}

	private void windowFadeOut(Window mWindow, WindowManager.LayoutParams lp) {
		lp.dimAmount = 0.5f;
		lp.alpha = 1f;
		mWindow.setAttributes(lp);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void submitProgressSearch() {
		String content = taskNo;
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (popup != null) {
			popup.showAtLocation(findViewById(R.id.progress_page),
					Gravity.CENTER, 0, 0);
			windowFadeIn(mWindow, lp);
			txtTitle.setText(Message.TIP_SEARCHING_DATA);
		}
		simpleUploadTask.execute(content);
	}

	public AsyncTask<String, Void, String> simpleUploadTask;

	public void initTask() {
		simpleUploadTask = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				try {
					SharedPreferences sp = getSharedPreferences(
							Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
					String server = sp.getString(Message.PREFERENCE_SERVER, "");
					String numberCustomer = sp.getString(Message.PREFERENCE_PHONE_NUMBER, "");
					if (server.equals("")) {
						server = getResources().getString(
								R.string.default_IPAddress)
								+ getResources().getString(
										R.string.default_service);
					} else {
						server = server
								+ getResources().getString(
										R.string.default_service);
					}
					server += "/task/checkstatus";
					String result = Message.ERROR;
					TaskService service = new TaskService(server);
					for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
						result = service.getTaskStatus(
								numberCustomer, params[0]);
						if (result.equals(Message.NETWORK_FAIL)) {
							continue;
						} else {
							break;
						}
					}
					return result;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					if (e.getCause() instanceof ConnectTimeoutException) {
						System.out.println("ConnectionTimeoutException");
					}
					return Message.NETWORK_FAIL;
				}
			}

			@Override
			protected void onPostExecute(String result) {
				btnSubmit.setClickable(true);
				try {
					result = URLDecoder.decode(result, "UTF-8");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Log.i("result", result);
				if (popup != null && popup.isShowing()) {
					popup.dismiss();
					windowFadeOut(mWindow, lp);
				}
				if (result.equals(Message.ERROR)
						|| result.equals(Message.NETWORK_FAIL)) {
//					//网络连接错误，按照短信发送
//					SharedPreferences sp = getSharedPreferences(
//							Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences					
//					String number = sp.getString(
//							Message.PREFERENCE_PHONE_SERVER, "");
//					if (number.equals("")) {
////						number = getResources().getString(
////								R.string.default_phone);
//						//发给号码为移动平台，短信内容是content
//						SmsManager.getDefault().sendTextMessage(getResources().getString(
//								R.string.default_phone_mobile),
//								null, "JDCX"+taskNo, null, null);
//						//发给号码为联通平台，短信内容是content
//						SmsManager.getDefault().sendTextMessage(getResources().getString(
//								R.string.default_phone_comu),
//								null, "JDCX"+taskNo, null, null);
//						//发给号码为电信平台，短信内容是content
//						SmsManager.getDefault().sendTextMessage(getResources().getString(
//								R.string.default_phone_net),
//								null, "JDCX"+taskNo, null, null);
//					}else {
//						// 发给号码为5556的模拟器，短信内容是content
//						SmsManager.getDefault().sendTextMessage(number,
//								null, "JDCX"+taskNo, null, null);
//					}
//					Toast.makeText(
//							ProgressActivity.this,
//							getResources().getString(
//									R.string.message_progress_tip),
//							Toast.LENGTH_LONG).show();
					Toast.makeText(ProgressActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
					return;
				} else{
					progressContent.setVisibility(View.VISIBLE);
					progressContent.setText(getResources().getString(R.string.progress_content)+"\n"+result);
					btnSubmit.setVisibility(View.GONE);
				}
			}
		};
	}

}
