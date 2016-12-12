package com.slamke.afterservice;

import java.net.URLDecoder;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.dao.CustomerService;
import com.slamke.afterservice.dao.DeviceService;
import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.webservice.LoginService;
import com.slamke.wheel.widget.CountDownButton;

/**
 * 登录页面--登录服务器或者进行本地身份验证
 * 
 * @author sunke
 *
 */
public class MainActivity extends Activity implements OnClickListener{

	private TextView txtTitle;	
	private PopupWindow popup; 
	private ImageView imgSetting;
	private WindowManager.LayoutParams lp;
	private Window mWindow;
	
	private TextView loginTxt;
	private EditText etxtPhoneNum;
	private EditText etextCodeNumber;
	
	private String tel;
	private String code;
	private CountDownButton btnCodeButton;
	
	private final static String CUSTOMER = "customer";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWindow = getWindow();  
        lp = mWindow.getAttributes();
		setContentView(R.layout.activity_login);
		loginTxt = (TextView) findViewById(R.id.login_login_btn_tv);
		etxtPhoneNum = (EditText) findViewById(R.id.etxt_phone_number);
		etextCodeNumber =  (EditText) findViewById(R.id.etxt_code_number);
		
		loginTxt.setOnClickListener(this);
		imgSetting = (ImageView) findViewById(R.id.img_setting);
		imgSetting.setOnClickListener(this);
		
		btnCodeButton = (CountDownButton)findViewById(R.id.btn_get_code);
		btnCodeButton.setOnClickListener(this);
		initPopupMenu();
		
				
		String savedTelNumber = getSavedPhoneNumber();
        Log.i("savedTelNumber", "savedTelNumber:"+savedTelNumber);
        
        if (savedTelNumber != null) {
			// 填写保存的手机号码
        	etxtPhoneNum.setText(savedTelNumber);
		}
	}
	
	/**
	 * 创建Popup Menu菜单
	 */
	private void initPopupMenu() {
		Context mContext = MainActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View mPopupWindow = mLayoutInflater.inflate(R.layout.progress, null);
		txtTitle = (TextView)mPopupWindow.findViewById(R.id.txt_title);
		popup = new PopupWindow(mPopupWindow, 300, 300);
		popup.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		popup.update();
	}
	
	private void windowFadeIn(Window mWindow,WindowManager.LayoutParams lp){
		lp.dimAmount = 0.5f; 
        lp.alpha = 0.5f;
        mWindow.setAttributes(lp);
	}
	
	private void windowFadeOut(Window mWindow,WindowManager.LayoutParams lp){
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
	
	private void savePhoneNumber(){
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
		SharedPreferences.Editor editor = sp.edit(); // 获得Editor								
		editor.putString(Message.PREFERENCE_PHONE_NUMBER, tel);
		editor.commit();
	}
	
	private String getSavedPhoneNumber(){
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences		
		String savedTelNumber = sp.getString(Message.PREFERENCE_PHONE_NUMBER, null);
		return savedTelNumber;
	}
	
	private String getServerAddress(){
	    SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
        String server = sp.getString(Message.PREFERENCE_SERVER, "");
        
        if (server.equals("")) {
            server = getResources().getString(R.string.default_IPAddress)+getResources().getString(R.string.default_service);
        }else {
            server = server + getResources().getString(R.string.default_service);
        }
        return server;
	}
	
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_setting:
			// finish();
			Intent intent = new Intent(this,SettingActivity.class);
			startActivityForResult(intent, 200);
			break;
		case R.id.btn_get_code:
			if(etxtPhoneNum.getText() !=null  &&  etxtPhoneNum.getText().toString() !=null
				&&  !etxtPhoneNum.getText().toString().trim().equals("") ){
				tel = etxtPhoneNum.getText().toString().trim();
			} else {
				Toast.makeText(MainActivity.this, Message.TEL_EMPTY, Toast.LENGTH_SHORT).show();
				return;
			}
			
			AsyncTask<String, Void, String> getAuthCodeTask =  new AsyncTask<String, Void, String>() {
	            @Override
	            protected String doInBackground(String... params) {	               
	            	try {
		                String server = getServerAddress() + "/customer/authcode";
		    			String result = Message.ERROR;
		    			LoginService service = new LoginService(server);
						result = service.getAuthCode(tel);
		                return result;
					} catch (Exception e) {
						e.printStackTrace();
						if( e.getCause() instanceof ConnectTimeoutException ) {  
		                    System.out.println("ConnectionTimeoutException");  
		                }
						return Message.NETWORK_FAIL;
					}
	            }
	 
	            @Override
	            protected void onPostExecute(String result) {
	            	try {
	            		result = URLDecoder.decode(result, "UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
	                Log.i("getAuthCodeTask", result);	              
	                if (result.equals(Message.SUCCESS)) {
	                	Toast.makeText(MainActivity.this, Message.TEL_CODE_SUCCESS, Toast.LENGTH_SHORT).show();
	                } else if (result.equals(Message.ERROR)) {
	                	Toast.makeText(MainActivity.this, Message.TEL_NONEXISTS, Toast.LENGTH_SHORT).show();
	                } else if (result.equals(Message.NETWORK_FAIL)) {
	                	Toast.makeText(MainActivity.this, Message.NETWORK_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
	                }
	            }	 
	        };
	        
	        getAuthCodeTask.execute();
			break;
		case R.id.login_login_btn_tv:
			loginTxt.setClickable(false);
			
			if(etxtPhoneNum.getText() !=null  &&  etxtPhoneNum.getText().toString() !=null
					&&  !etxtPhoneNum.getText().toString().trim().equals("") ){
				tel = etxtPhoneNum.getText().toString().trim();
			} else {
				Toast.makeText(MainActivity.this, Message.TEL_EMPTY, Toast.LENGTH_SHORT).show();
				loginTxt.setClickable(true);
				return;
			}
			
			if (etextCodeNumber.getText() !=null  &&  etextCodeNumber.getText().toString() !=null
					&&  !etextCodeNumber.getText().toString().trim().equals("") ) {
				code = etextCodeNumber.getText().toString().trim();
			} else {
				Toast.makeText(MainActivity.this, Message.CODE_EMPTY, Toast.LENGTH_SHORT).show();
				loginTxt.setClickable(true);
				return;
			}
			
			if (popup != null) {
				popup.showAtLocation(findViewById(R.id.login_page),
							Gravity.CENTER, 0, 0);
				windowFadeIn(mWindow, lp);
				txtTitle.setText(Message.TIP_LOGIN);
			}
			
			
			AsyncTask<String, Void, String> loginTask =  new AsyncTask<String, Void, String>() {
	            @Override
	            protected String doInBackground(String... params) {	               
	            	try {
		                
		    			String server = getServerAddress() + "/customer/login/code";
		    			String result = Message.ERROR;
		    			LoginService service = new LoginService(server);
		    			result = service.login(tel, code);
		                return result;
					} catch (Exception e) {
						e.printStackTrace();
						if( e.getCause() instanceof ConnectTimeoutException ) {  
		                    System.out.println("ConnectionTimeoutException");  
		                }
						return Message.NETWORK_FAIL;
					}
	            }
	 
	            @Override
	            protected void onPostExecute(String result) {
	            	loginTxt.setClickable(true);
	            	try {
	            		result = URLDecoder.decode(result, "UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
	                Log.i("result", result);
	                if (popup != null && popup.isShowing()) {
						popup.dismiss();
						windowFadeOut(mWindow, lp);
					}
	                
	                if (result.equals(Message.ERROR)) {
	                	Toast.makeText(MainActivity.this, Message.TEL_CODE_ERROR, Toast.LENGTH_SHORT).show();
	                } else if (result.equals(Message.NETWORK_FAIL)) {
	                	Toast.makeText(MainActivity.this, Message.NETWORK_ERROR_LOGIN, Toast.LENGTH_SHORT).show();						
	                } else {
						ClassParse parse = new ClassParse();
						Map<String, String> map = parse.string2Map(result);
						if (map == null) {
							Toast.makeText(MainActivity.this, Message.NETWORK_ERROR_LOGIN, Toast.LENGTH_SHORT).show();
							return;
						}
						Customer customer = parse.string2Customer(map.get(CUSTOMER));
						if (customer !=null) {
							GlobalObject.customer = customer;
							//成功登录后，将用户信息缓存至本地
							CustomerService service = new CustomerService(MainActivity.this);
							service.insertCustomer(customer);
							service.close();
							DeviceService deviceService = new DeviceService(MainActivity.this);
							deviceService.insertDevices(customer.getDevices(), customer);
							deviceService.close();
							savePhoneNumber();
							
							finish();
							Intent intent = new Intent(MainActivity.this,HomeActivity.class);
							startActivity(intent);
							return;
						} else {
							Toast.makeText(MainActivity.this, Message.TEL_CODE_ERROR, Toast.LENGTH_SHORT).show();
						}
					}
	            }
	 
	        };
	        
	        loginTask.execute();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (popup != null && popup.isShowing()) {
			popup.dismiss();
		}
	}
	
	 /** 
     * 复写onActivityResult，这个方法 
     * 是要等到SimpleTaskActivity点了提交过后才会执行的 
     */  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //可以根据多个请求代码来作相应的操作  
        if(44==resultCode) {  
        	finish();
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    } 
}