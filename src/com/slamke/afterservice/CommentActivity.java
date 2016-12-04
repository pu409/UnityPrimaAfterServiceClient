package com.slamke.afterservice;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.domain.CommentInfo;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.ConnectionDetector;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.webservice.TaskService;
import com.slamke.afterservice.webservice.UploadService;
/**
 * 评论用--提交前，进行网络状态检测;网络差时，不可用短信发送
 * @author sunke
 *
 */
public class CommentActivity extends Activity implements OnClickListener {

	private final static String COMMENT_CONTENT_SUPER_SATISFY = "很满意";
	private final static String COMMENT_CONTENT_SATISFY = "满意";
	private final static String COMMENT_CONTENT_UNSATISFY = "不满意";
	private String commentContent = COMMENT_CONTENT_SUPER_SATISFY;

	private Button btnBack;
	private Button btnSubmit;
	private EditText etxtReason;
	private Spinner spTask;
	private String taskNo = "";
	private String[] taskNum;
	
	private ViewStub vsStub;
	private ScrollView body;
	
	private RadioButton rdSuper;
	private RadioButton rdSatis;
	private RadioButton rdUnSatis;

	private TextView txtTitle;
	private PopupWindow popup;
	private WindowManager.LayoutParams lp;
	private Window mWindow;
	
	private Set<String> tasks;
	private Map<String, String> pairs;
	//private Set<String> persons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWindow = getWindow();
		lp = mWindow.getAttributes();

		setContentView(R.layout.activity_comment);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		etxtReason = (EditText) findViewById(R.id.etxt_reason);
		spTask = (Spinner) findViewById(R.id.sp_no);
		
		body = (ScrollView) findViewById(R.id.body);
		
		spTask.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (taskNum !=null && taskNum.length >1) {
					taskNo = taskNum[arg2].substring(0, taskNum[arg2].indexOf("("));
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if (taskNum !=null && taskNum.length >1) {
					taskNo = taskNum[0].substring(0, taskNum[0].indexOf("("));
				}
			}
		});
		rdSuper = (RadioButton) findViewById(R.id.rd_super);
		rdSuper.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					commentContent = COMMENT_CONTENT_SUPER_SATISFY;
				}
			}
		});
		rdSatis = (RadioButton) findViewById(R.id.rd_satis);
		rdSatis.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					commentContent = COMMENT_CONTENT_SATISFY;
				}
			}
		});
		rdUnSatis = (RadioButton) findViewById(R.id.rd_unsatis);
		rdUnSatis.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					commentContent = COMMENT_CONTENT_UNSATISFY;
					etxtReason.setVisibility(View.VISIBLE);
				} else {
					etxtReason.setVisibility(View.GONE);
				}
			}
		});
		
		initPopupMenu();
		vsStub = (ViewStub) findViewById(R.id.vs);
		View parent = vsStub.inflate();
		TextView titleView = (TextView) parent.findViewById(R.id.txt_title);
		titleView.setText(Message.TIP_LOADING_DATA);
		initData();
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
					server += "/task/unevaluated_list";
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
				//result = "{\"C20131025001\":\"司亚雷\",\"B20131230009\":\"司亚雷\",\"B20131230008\":\"\",\"B20131230010\":\"司亚雷\"}";
				Log.i("result", result);
				if (result.equals(Message.ERROR)
						|| result.equals(Message.NETWORK_FAIL)) {
					Toast.makeText(CommentActivity.this, Message.NETWORK_ERROR_EVALUATE_LAST_ONLY,
							Toast.LENGTH_SHORT).show();
					spTask.setEnabled(false);
					//btnSubmit.setClickable(false);
				}else if (result.equals(Message.SUCCESS)) {
					Toast.makeText(CommentActivity.this, Message.NO_UNEVALUATED_TASK,
							Toast.LENGTH_SHORT).show();
					spTask.setEnabled(false);
					btnSubmit.setClickable(false);
				}else{
					ClassParse parse = new ClassParse();
					pairs = parse.string2taskPersonPair(result);
					if(pairs != null) {
						Log.i("pairs", "size:"+pairs.size());
					}else {
						Toast.makeText(CommentActivity.this, Message.NETWORK_ERROR_EVALUATE_LAST_ONLY,
								Toast.LENGTH_SHORT).show();
						spTask.setEnabled(false);
						vsStub.setVisibility(View.GONE);
						body.setVisibility(View.VISIBLE);
						return;
					}
					tasks = null;
					if (pairs != null) {
						tasks = pairs.keySet();
					}
					if (tasks != null && tasks.size() > 0){
						List<String> tempList = new ArrayList<String>();
						for (String string : tasks) {
							tempList.add(string+"("+pairs.get(string)+")");
						}
						taskNum = new String[]{""};
						taskNum = tempList.toArray(taskNum);
						taskNo = tempList.get(0).substring(0, tempList.get(0).indexOf("("));
						SpinnerAdapter adapterCategory = new SpinnerAdapter(
								CommentActivity.this,
								android.R.layout.simple_spinner_item, taskNum);
						adapterCategory
								.setDropDownViewResource(android.R.layout.browser_link_context_header);
						spTask.setAdapter(adapterCategory);
					}else {
						Toast.makeText(CommentActivity.this, Message.NO_UNEVALUATED_TASK,
								Toast.LENGTH_SHORT).show();
						spTask.setEnabled(false);
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
			if (taskNum == null||taskNo == null||taskNo.equals("")) {
				evaluateLast();
				goHome();
				return;
			}
			if (commentContent.equals(COMMENT_CONTENT_UNSATISFY)
					&& (etxtReason.getText() == null
							|| etxtReason.getText().toString() == null || etxtReason
							.getText().toString().trim().equals(""))) {
				Toast.makeText(CommentActivity.this,
						Message.COMMENT_UNSTATISFY_REASON, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			//******检测网络*****//
			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
			Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
			if (!isInternetPresent) {
				Toast.makeText(CommentActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
				return;
			}
			//******检测网络*****//
			btnSubmit.setClickable(false);
			initTask();
			submitComment();
		default:
			break;
		}
	}
	private void evaluateLast(){
		String result = "FWPJHMY";
		if (rdSatis.isChecked()) {
			result = "FWPJMY";
		}else if (rdUnSatis.isChecked()) {
			result = "FWPJBMY";
		}else {
			result = "FWPJHMY";
		}
		Log.i("result:", result);
		SharedPreferences sp = getSharedPreferences(
				Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
		String number = sp.getString(Message.PREFERENCE_PHONE_SERVER, "");
		if (number.equals("")) {
			//发给号码为移动平台，短信内容是content
			SmsManager.getDefault().sendTextMessage(getResources().getString(
					R.string.default_phone_mobile),
					null, result, null, null);
			//发给号码为联通平台，短信内容是content
			SmsManager.getDefault().sendTextMessage(getResources().getString(
					R.string.default_phone_comu),
					null, result, null, null);
			//发给号码为电信平台，短信内容是content
			SmsManager.getDefault().sendTextMessage(getResources().getString(
					R.string.default_phone_net),
					null, result, null, null);
		}else {
			// 发给号码为5556的模拟器，短信内容是content
			SmsManager.getDefault().sendTextMessage(number, null,
					result, null, null);
		}
		Toast.makeText(
				CommentActivity.this,
				getResources()
						.getString(R.string.server_comment_tip),
				Toast.LENGTH_SHORT).show();
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
		Context mContext = CommentActivity.this;
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

	public void submitComment() {
		CommentInfo info = new CommentInfo(taskNo, commentContent, etxtReason
				.getText().toString());
		ClassParse parse = new ClassParse();
		String content = parse.commentInfo2String(info);
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (popup != null) {
			popup.showAtLocation(findViewById(R.id.comment_page),
					Gravity.CENTER, 0, 0);
			windowFadeIn(mWindow, lp);
			txtTitle.setText(Message.TIP_UPLOADING_DATA);
		}
		simpleUploadTask.execute(content);
		return;
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
					server += "/submit/comment";
					String result = Message.ERROR;
					UploadService service = new UploadService(server);
					for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
						result = service.upload(
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
//					Toast.makeText(CommentActivity.this, Message.NETWORK_ERROR,
//							Toast.LENGTH_SHORT).show();
					Toast.makeText(CommentActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
					return;
				} else if (result.equals(Message.SUCCESS)) {
					Toast.makeText(
							CommentActivity.this,
							getResources()
									.getString(R.string.server_comment_tip),
							Toast.LENGTH_SHORT).show();
						etxtReason.setText("");
						etxtReason.setVisibility(View.GONE);
						rdSatis.setChecked(false);
						rdUnSatis.setChecked(false);
						rdSuper.setChecked(true);
						List<String> tempList = new ArrayList<String>();
						for (String string : tasks) {
							if(taskNo.equals(string)){
								tasks.remove(string);
								break;
							}	
						}
						/*for (int i = 0; i < tasks.size(); i++) {
							if(taskNo.equals(tasks.get(i).getNum())){
								tasks.remove(i);
								break;
							}	
						}*/
						if (tasks != null && tasks.size() > 0){
							for (String string : tasks) {
								tempList.add(string+"("+pairs.get(string)+")");
							}
							taskNum = new String[]{""};
							taskNum = tempList.toArray(taskNum);
							taskNo = tempList.get(0).substring(0, tempList.get(0).indexOf("("));
							SpinnerAdapter adapterCategory = new SpinnerAdapter(
									CommentActivity.this,
									android.R.layout.simple_spinner_item, taskNum);
							adapterCategory
									.setDropDownViewResource(android.R.layout.browser_link_context_header);
							spTask.setAdapter(adapterCategory);
						}else {
							taskNum = new String[]{""};
							SpinnerAdapter adapterCategory = new SpinnerAdapter(
									CommentActivity.this,
									android.R.layout.simple_spinner_item, taskNum);
							adapterCategory
									.setDropDownViewResource(android.R.layout.browser_link_context_header);
							spTask.setAdapter(adapterCategory);
							btnSubmit.setClickable(false);
							spTask.setEnabled(false);
						}					
						return;
				}
			}
		};
	}

}
