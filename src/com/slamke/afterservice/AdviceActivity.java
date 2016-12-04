package com.slamke.afterservice;

import java.net.URLDecoder;
import java.net.URLEncoder;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.domain.AdviceInfo;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.ConnectionDetector;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.webservice.UploadService;
/**
 * 建议用page--提交前，进行网络状态检测;网络差时，不可用短信发送
 * @author sunke
 *
 */
public class AdviceActivity extends Activity implements OnClickListener {
	private Button btnBack;
	//private Dialog methodListDlg;
	private Button btnSubmit;
	private EditText etxtAdvice;
	private Spinner spAdviceType;
	private String adviceType = "";
	private SpinnerAdapter adapterType;
	//private CheckBox cbMessage;
	//private CheckBox cbEmail;
	//private CheckBox cbServer;

	private TextView txtTitle;
	private PopupWindow popup;
	private WindowManager.LayoutParams lp;
	private Window mWindow;

	//private boolean emailFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWindow = getWindow();
		lp = mWindow.getAttributes();
		setContentView(R.layout.activity_advice);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		etxtAdvice = (EditText) findViewById(R.id.etxt_advice);
		//methodListDlg = createMethodListDialog();
		spAdviceType = (Spinner) findViewById(R.id.sp_advice_type);
		adapterType = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.adviceTypes));
		adapterType
				.setDropDownViewResource(android.R.layout.browser_link_context_header);
		spAdviceType.setAdapter(adapterType);
		spAdviceType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				adviceType = getResources().getStringArray(R.array.adviceTypes)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				adviceType = getResources().getStringArray(R.array.adviceTypes)[0];
			}
		});
		initPopupMenu();
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
			if (etxtAdvice.getText() == null
					|| etxtAdvice.getText().toString() == null
					|| etxtAdvice.getText().toString().trim().equals("")) {
				Toast.makeText(AdviceActivity.this, Message.ADVICE_EMPTY,
						Toast.LENGTH_SHORT).show();
				return;
			}
			//******检测网络*****//
			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
			Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
			if (!isInternetPresent) {
				Toast.makeText(AdviceActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
				return;
			}
			//******检测网络*****//
			initTask();
			btnSubmit.setClickable(false);
			//methodListDlg.show();
			submitAdvice();
		default:
			break;
		}
	}
	private void submitAdvice(){
		if (popup != null) {
			popup.showAtLocation(
					findViewById(R.id.advice_page),
					Gravity.CENTER, 0, 0);
			windowFadeIn(mWindow, lp);
			txtTitle.setText(Message.TIP_UPLOADING_DATA);
		}
		AdviceInfo info = new AdviceInfo(adviceType, etxtAdvice
				.getText().toString());
		ClassParse parse = new ClassParse();
		String content = parse.adviceInfo2String(info);
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		simpleUploadTask.execute(content);
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
		Context mContext = AdviceActivity.this;
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

	public AsyncTask<String, Void, String> simpleUploadTask;
	public void initTask(){
		simpleUploadTask = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				try {					
					SharedPreferences sp = getSharedPreferences(
							Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
					String server = sp.getString(Message.PREFERENCE_SERVER, "");
					String numberCustomer = sp.getString(Message.PREFERENCE_PHONE_NUMBER, "");
					if (server.equals("")) {
						server = getResources().getString(R.string.default_IPAddress)+getResources().getString(R.string.default_service);
					}else {
	    				server = server + getResources().getString(R.string.default_service);
					}
					server += "/submit/advice";
					String result = Message.ERROR;
	    			UploadService service = new UploadService(server);
	                for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
						result = service.upload(numberCustomer, params[0]);
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
//						String number = sp.getString(
//								Message.PREFERENCE_PHONE_SERVER, "");
//						
//						AdviceInfo info = new AdviceInfo(adviceType, etxtAdvice
//								.getText().toString());
//						if (number.equals("")) {
////							number = getResources().getString(
////									R.string.default_phone_mobile);
//							//发给号码为移动平台，短信内容是content
//							SmsManager.getDefault().sendTextMessage(getResources().getString(
//									R.string.default_phone_mobile),
//									null, info.toMessage(), null, null);
//							//发给号码为联通平台，短信内容是content
//							SmsManager.getDefault().sendTextMessage(getResources().getString(
//									R.string.default_phone_comu),
//									null, info.toMessage(), null, null);
//							//发给号码为电信平台，短信内容是content
//							SmsManager.getDefault().sendTextMessage(getResources().getString(
//									R.string.default_phone_net),
//									null, info.toMessage(), null, null);
//						}else {
//							//发给号码为5556的模拟器，短信内容是content
//							SmsManager.getDefault().sendTextMessage(number,
//									null, info.toMessage(), null, null);
//						}
					Toast.makeText(AdviceActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
					return;
				}else if (result.equals(Message.SUCCESS)){
					Toast.makeText(AdviceActivity.this,getResources().getString(R.string.server_advice_tip) ,
							Toast.LENGTH_SHORT).show();					
					finish();
					Intent intent = new Intent(AdviceActivity.this,
								HomeActivity.class);
					startActivity(intent);
					return;
				}else {
					Toast.makeText(AdviceActivity.this, Message.SERVER_CONFIG_ERROR, Toast.LENGTH_SHORT).show();
					return;
				}
			}
		};
	}
	
}
