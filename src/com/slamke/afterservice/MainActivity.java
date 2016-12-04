package com.slamke.afterservice;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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
import com.slamke.afterservice.domain.CTelStart;
import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.util.CardNumGetter;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.util.SIMCardInfo;
import com.slamke.afterservice.webservice.LoginService;

/**
 * 登录页面--登录服务器或者进行本地身份验证
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
	private String tel;
	
	private final static String LOGIN_CHECK = "SIMUIM";
	private final static String CUSTOMER = "customer";
	private final static String TEL_START = "tel_start";
	
	private final static String SUPPLIER_MOBILE = "移动";
	private final static String SUPPLIER_COMU = "电信";
	private final static String SUPPLIER_NET = "联通";
	
	 private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWindow = getWindow();  
        lp = mWindow.getAttributes();
		setContentView(R.layout.activity_login);
		loginTxt = (TextView) findViewById(R.id.login_login_btn_tv);
		etxtPhoneNum = (EditText) findViewById(R.id.etxt_phone_number);
		etxtPhoneNum.setEnabled(false);
		loginTxt.setOnClickListener(this);
		imgSetting = (ImageView) findViewById(R.id.img_setting);
		imgSetting.setOnClickListener(this);
		initPopupMenu();
		
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
		SharedPreferences.Editor editor = sp.edit(); // 获得Editor
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String currentSim = tm.getSimSerialNumber();
        String savedSim= sp.getString(Message.PREFERENCE_SIM_NUMBER, "");
        Log.i("savedSim", "savedSim:"+savedSim);
        Log.i("currentSim", "currentSim:"+currentSim);
        if (currentSim == null) {
			Toast.makeText(this, Message.NO_SIM_CARD, Toast.LENGTH_SHORT).show();
			return;
		}else {
			if(!savedSim.equals(currentSim)){
				//sim卡发生了变化
				editor.putString(Message.PREFERENCE_SIM_NUMBER, currentSim);
				editor.putString(Message.PREFERENCE_PHONE_NUMBER, null);
				editor.putString(Message.PREFERENCE_PHONE_SERVER, null);
				//设置sim卡更换的时间
				editor.putLong(Message.PREFERENCE_SIM_DATE, new Date().getTime());
				editor.commit();
	        }
		}
        
		
		String num = sp.getString(Message.PREFERENCE_PHONE_NUMBER, null);
		//Log.i("num", "num:"+num);
		if (num == null||num.equals("")) {
			SIMCardInfo siminfo = new SIMCardInfo(MainActivity.this);
			num = siminfo.getNativePhoneNumber();
			if (num == null||num.equals("")) {
				String number = sp.getString(
						Message.PREFERENCE_PHONE_SERVER, "");
				//通过短信形式进行号码检索，因为是第一次登录所以server的手机号码不确定；（如果确定表示是已经登录过）
				if (number.equals("")) {
					CardNumGetter getter = new CardNumGetter(this);
					String numberServer = "";
					
					String numberMobile = getResources().getString(
							R.string.default_phone_mobile);
					Log.i("numberMobile", numberMobile);
					//首先获取得到短信中的号码
					String numMobile = getter.getSIMCardNum(numberMobile,sp.getLong(Message.PREFERENCE_SIM_DATE, 1));
					Log.e("numMobile", ""+numMobile);
					if (numMobile != null&& !numMobile.equals("")) {
						num = numMobile;
						numberServer = numberMobile;
					}
					
					String numberComu = getResources().getString(
							R.string.default_phone_comu);
					Log.i("numberComu", numberComu);
					//首先获取得到短信中的号码
					String numComu = getter.getSIMCardNum(numberComu,sp.getLong(Message.PREFERENCE_SIM_DATE, 1));
					Log.e("numComu", ""+numComu);
					if (numComu != null&& !numComu.equals("")) {
						num = numComu;
						numberServer = numberComu;
					}
					
					
					String numberNet = getResources().getString(
							R.string.default_phone_net);
					Log.i("numberNet", numberNet);
					//首先获取得到短信中的号码
					String numNet = getter.getSIMCardNum(numberNet,sp.getLong(Message.PREFERENCE_SIM_DATE, 1));
					Log.e("numNet", ""+numNet);
					if (numNet != null&& !numNet.equals("")) {
						num = numNet;
						numberServer = numberNet;
					}
					editor.putString(Message.PREFERENCE_PHONE_SERVER, numberServer);
					editor.putString(Message.PREFERENCE_PHONE_NUMBER, num);
					editor.commit();
				}
				if (num == null||num.equals("")){
					//发给号码为5556的模拟器，短信内容是content
					SmsManager.getDefault().sendTextMessage(getResources().getString(
							R.string.default_phone_mobile),
							null, LOGIN_CHECK, null, null);
					SmsManager.getDefault().sendTextMessage(getResources().getString(
							R.string.default_phone_comu),
							null, LOGIN_CHECK, null, null);
					SmsManager.getDefault().sendTextMessage(getResources().getString(
							R.string.default_phone_net),
							null, LOGIN_CHECK, null, null);
					Toast.makeText(this, Message.LOGIN_CHECK_TIP, Toast.LENGTH_LONG).show();
					return;
				}
			}
		}
		
		editor.putString(Message.PREFERENCE_PHONE_NUMBER, num);
		editor.commit();
		etxtPhoneNum.setText(num.replace(" ", ""));
		////////////////////////////////////////////////////////////////////////////////////////////////
		//((TextView)findViewById(R.id.txt_dial)).setMovementMethod(LinkMovementMethod.getInstance());
		//((TextView)findViewById(R.id.txt_dial)).setText(ActionUtils.getDialAction(this));
		//之后在控件上还要设置setMovementMethod(LinkMovementMethod.getInstance())
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.img_setting:
			//finish();
			Intent intent = new Intent(this,SettingActivity.class);
			startActivityForResult(intent, 200);
			break;
		case R.id.login_login_btn_tv:
			loginTxt.setClickable(false);
			if(etxtPhoneNum.getText() !=null  &&  etxtPhoneNum.getText().toString() !=null
					&&  !etxtPhoneNum.getText().toString().trim().equals("") ){
				tel = etxtPhoneNum.getText().toString().trim();
			}else {
				Toast.makeText(MainActivity.this, Message.TEL_EMPTY, Toast.LENGTH_SHORT).show();
				loginTxt.setClickable(true);
				return;
			}
			if (popup != null) {
				popup.showAtLocation(findViewById(R.id.login_page),
							Gravity.CENTER, 0, 0);
				windowFadeIn(mWindow, lp);
					txtTitle.setText(Message.TIP_LOGIN);
			}
			
			
			AsyncTask<String, Void, String> simpleGetTask =  new AsyncTask<String, Void, String>() {
	            @Override
	            protected String doInBackground(String... params) {
	               
	            	try {
		                SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
		        				MODE_PRIVATE); // 获得Preferences
		                String server = sp.getString(Message.PREFERENCE_SERVER, "");
		                
		    			if (server.equals("")) {
		    				server = getResources().getString(R.string.default_IPAddress)+getResources().getString(R.string.default_service);
		    			}else {
		    				server = server + getResources().getString(R.string.default_service);
						}
		    			server += "/customer/login";
		    			String result = Message.ERROR;
		    			LoginService service = new LoginService(server);
		                for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
							result = service.login(tel);
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
						if( e.getCause() instanceof ConnectTimeoutException )  
		                {  
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
						// TODO: handle exception
						e.printStackTrace();
					}
	                Log.i("result", result);
	                if (popup != null && popup.isShowing()) {
						popup.dismiss();
						windowFadeOut(mWindow, lp);
					}
	                
	                if (result.equals(Message.ERROR)) {
	                	Toast.makeText(MainActivity.this, Message.TEL_ERROR, Toast.LENGTH_SHORT).show();
	                }else if (result.equals(Message.NETWORK_FAIL)) {
	                	//已经成功登陆的用户，在网络条件差时，可以通过本地缓存的信息进行登录
						CustomerService service = new CustomerService(MainActivity.this);
						Customer customer = service.login(tel);
						service.close();
						if(customer != null){
							GlobalObject.customer = customer;
							finish();
							Intent intent = new Intent(MainActivity.this,HomeActivity.class);
							startActivity(intent);
							return;
						}else {
							Toast.makeText(MainActivity.this, Message.NETWORK_ERROR_LOGIN, Toast.LENGTH_SHORT).show();
						}
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
							
							CTelStart telStart = parse.string2CTelStart(map.get(TEL_START));
							if (telStart != null) {
								SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
										MODE_PRIVATE); // 获得Preferences
								SharedPreferences.Editor editor = sp.edit(); // 获得Editor
								if(telStart.getAcceptNum()!= null && telStart.getAcceptNum().length()>10){									
									editor.putString(Message.PREFERENCE_PHONE_SERVER, telStart.getAcceptNum());
									editor.commit();
								}else {
									if (telStart.getSupplier() != null) {
										if (telStart.getSupplier().equals(SUPPLIER_MOBILE)) {
											editor.putString(Message.PREFERENCE_PHONE_SERVER, getResources().getString(R.string.default_phone_mobile));
											editor.commit();
										}else if (telStart.getSupplier().equals(SUPPLIER_NET)) {
											editor.putString(Message.PREFERENCE_PHONE_SERVER, getResources().getString(R.string.default_phone_net));
											editor.commit();
										}else if (telStart.getSupplier().equals(SUPPLIER_COMU)) {
											editor.putString(Message.PREFERENCE_PHONE_SERVER, getResources().getString(R.string.default_phone_comu));
											editor.commit();
										}
									}
								}
								
							}
							finish();
							Intent intent = new Intent(MainActivity.this,HomeActivity.class);
							startActivity(intent);
							return;
						}else {
							Toast.makeText(MainActivity.this, Message.TEL_ERROR, Toast.LENGTH_SHORT).show();
						}
						
					}
	            }
	 
	        };
	        
	        simpleGetTask.execute();
			break;
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
     * 复写onActivityResult，这个方法 
     * 是要等到SimpleTaskActivity点了提交过后才会执行的 
     */  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        //可以根据多个请求代码来作相应的操作  
        if(44==resultCode)  
        {  
        	finish();
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    } 
}
