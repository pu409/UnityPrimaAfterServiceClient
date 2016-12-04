package com.slamke.afterservice;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.dao.ProductTypeClassService;
import com.slamke.afterservice.domain.ConsultInfo;
import com.slamke.afterservice.domain.ProductType;
import com.slamke.afterservice.domain.ProductTypeClass;
import com.slamke.afterservice.util.ClassParse;
import com.slamke.afterservice.util.Const;
import com.slamke.afterservice.util.DateParse;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.afterservice.webservice.ConsultService;
/**
 * 咨询用--不进行网络状态检测
 * @author sunke
 *
 */
@SuppressLint("SimpleDateFormat")
public class ConsultActivity extends Activity implements OnClickListener {
//	private CheckBox cbMessage;
//	private CheckBox cbEmail;
//	private CheckBox cbServer;
	private Button btnBack;
//	private Dialog methodListDlg;
	private Button btnSubmit;
	private Spinner spCategory;
	private Spinner spType;
	private ScrollView body;
	
	//private String category;
	//private String type;
	private EditText etxtConsult;
	private ViewStub vsStub;

	private List<ProductTypeClass> typeClasses;
	private List<ProductType> types;

	private ProductTypeClass theClass;
	private ProductType theType;
	
	private String[] classStrs = { "" };
	private String[] typeStrs = { "" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consult);
		body = (ScrollView) findViewById(R.id.body);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		spCategory = (Spinner) findViewById(R.id.sp_category);
		spType = (Spinner) findViewById(R.id.sp_type);
		etxtConsult = (EditText) findViewById(R.id.etxt_consult);
		spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				theClass = typeClasses.get(arg2);
				List<String> temp = new ArrayList<String>();
				if (theClass.getTypes() != null) {
					types = theClass.getTypes();
					for (int i = 0; i < types.size(); i++) {
						temp.add(types.get(i).getModel());
					}
					if (types.size()>0) {
						theType = types.get(0);
					}
				}
				typeStrs = new String[]{""};
				typeStrs = temp.toArray(typeStrs);
				if(typeStrs == null){
					spType.setAdapter(null);
					spType.setEnabled(false);
				}else {
					SpinnerAdapter adapterType = new SpinnerAdapter(
							ConsultActivity.this,
							android.R.layout.simple_spinner_item, typeStrs);
					adapterType
							.setDropDownViewResource(android.R.layout.browser_link_context_header);
					spType.setAdapter(adapterType);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if (typeClasses.size()>0) {
					theClass = typeClasses.get(0);
				}else {
					theClass = null;
				}
			}
		});

		spType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (types.size()>0) {
					theType = types.get(arg2);
				}else {
					theType = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if (types.size()>0) {
					theType = types.get(0);
				}else {
					theType = null;
				}
			}
		});

		vsStub = (ViewStub) findViewById(R.id.vs);
		View parent = vsStub.inflate();
		TextView titleView = (TextView) parent.findViewById(R.id.txt_title);
		titleView.setText(Message.TIP_LOADING_DATA);
		initData();
	}

	private void initData() {
		ProductTypeClassService tempService = new ProductTypeClassService(
				ConsultActivity.this);
		List<ProductTypeClass> tempList = tempService.getAllClasses();
		tempService.close();
		SharedPreferences sp = getSharedPreferences(Const.PREFERENCE_NAME,
				MODE_PRIVATE); 
		SharedPreferences.Editor editor = sp.edit(); 
		String lastTimeStr = "";
		lastTimeStr = sp.getString(Const.PREFERENCE_UPDATE_CONSULT, "20121212");		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Calendar lastTime  =Calendar.getInstance();
		try {
			lastTime.setTime(sdf.parse(lastTimeStr));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Calendar now =Calendar.getInstance();
		long l=now.getTimeInMillis()-lastTime.getTimeInMillis();
		int days= Long.valueOf(l/(1000*60*60*24)).intValue();
		Log.e("day inter:", ""+days);
		
		//每周更新数据
		if (days > 7 || tempList.size() < 1) {
		//if(true){
			editor.putString(Const.PREFERENCE_UPDATE_CONSULT, new DateParse().date2String(new Date())); 
	    	editor.commit();
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
						server += "/product/list";
						
						String result = Message.ERROR;
		    			ConsultService service = new ConsultService(server);
		                for (int i = 0; i < GlobalObject.TRY_TIMES; i++) {
							result = service.downloadData(numberCustomer);
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
					ProductTypeClassService service = new ProductTypeClassService(
							ConsultActivity.this);
					if (result.equals(Message.ERROR)
							|| result.equals(Message.NETWORK_FAIL)) {
						typeClasses = service.getAllClasses();
					} else {
						ClassParse parse = new ClassParse();
						typeClasses = parse.string2ProductTypeClassList(result);
						if (typeClasses != null) {
							service.insertClasses(typeClasses);
						} else {
							typeClasses = service.getAllClasses();
						}
					}
					if (typeClasses != null && typeClasses.size() > 0) {
						List<String> temp = new ArrayList<String>();
						for (int i = 0; i < typeClasses.size(); i++) {
							temp.add(typeClasses.get(i).getName());
						}
						classStrs = new String[]{""};
						classStrs = temp.toArray(classStrs);
						temp.clear();
						if (typeClasses.get(0).getTypes() != null) {
							types = typeClasses.get(0).getTypes();
							for (int i = 0; i < typeClasses.get(0).getTypes()
									.size(); i++) {
								temp.add(typeClasses.get(0).getTypes().get(i)
										.getModel());
							}
						}
						typeStrs= new String[]{""};
						typeStrs = temp.toArray(typeStrs);
						
						if(typeClasses.size()>0){
							theClass = typeClasses.get(0);
						}else {
							theClass = null;
						}
						
						if (types.size()>0) {
							theType = types.get(0);
						}else {
							theType = null;
						}
						
						if (classStrs !=null && classStrs.length >0) {
							SpinnerAdapter adapterCategory = new SpinnerAdapter(
									ConsultActivity.this,
									android.R.layout.simple_spinner_item, classStrs);
							adapterCategory
									.setDropDownViewResource(android.R.layout.browser_link_context_header);
							spCategory.setAdapter(adapterCategory);
						}else {
							spCategory.setAdapter(null);
							spCategory.setEnabled(false);
						}
						if (typeStrs !=null && typeStrs.length >0) {
							SpinnerAdapter adapterType = new SpinnerAdapter(
									ConsultActivity.this,
									android.R.layout.simple_spinner_item, typeStrs);
							adapterType
									.setDropDownViewResource(android.R.layout.browser_link_context_header);
							spType.setAdapter(adapterType);
						}else {
							spType.setAdapter(null);
							spType.setEnabled(false);
						}
					}else {
						Toast.makeText(ConsultActivity.this, Message.NETWORK_ERROR_TRY_AGAIN,
								Toast.LENGTH_SHORT).show();
					}

					service.close();
					vsStub.setVisibility(View.GONE);
					body.setVisibility(View.VISIBLE);
				}

			};
			simpleGetTask.execute();
		}else {
			vsStub.setVisibility(View.GONE);
			body.setVisibility(View.VISIBLE);
			ProductTypeClassService service = new ProductTypeClassService(
			ConsultActivity.this);
			typeClasses = service.getAllClasses();
			if (typeClasses != null && typeClasses.size() > 0) {
				List<String> temp = new ArrayList<String>();
				for (int i = 0; i < typeClasses.size(); i++) {
					temp.add(typeClasses.get(i).getName());
				}
				classStrs = new String[]{""};
				classStrs = temp.toArray(classStrs);
				temp.clear();
				if (typeClasses.get(0).getTypes() != null) {
					types = typeClasses.get(0).getTypes();
					for (int i = 0; i < typeClasses.get(0).getTypes()
							.size(); i++) {
						temp.add(typeClasses.get(0).getTypes().get(i)
								.getModel());
					}
				}
				typeStrs = new String[]{""};
				typeStrs = temp.toArray(typeStrs);
				if (typeClasses.size()>0) {
					theClass = typeClasses.get(0);
				}else {
					theClass = null;
				}
				if (types.size()>0) {
					theType = types.get(0);
				}else {
					theType = null;
				}
				if (classStrs !=null && classStrs.length >0) {
					SpinnerAdapter adapterCategory = new SpinnerAdapter(
							ConsultActivity.this,
							android.R.layout.simple_spinner_item, classStrs);
					adapterCategory
							.setDropDownViewResource(android.R.layout.browser_link_context_header);
					spCategory.setAdapter(adapterCategory);
				}else {
					spCategory.setAdapter(null);
					spCategory.setEnabled(false);
				}
				if (typeStrs !=null && typeStrs.length >0) {
					SpinnerAdapter adapterType = new SpinnerAdapter(
							ConsultActivity.this,
							android.R.layout.simple_spinner_item, typeStrs);
					adapterType
							.setDropDownViewResource(android.R.layout.browser_link_context_header);
					spType.setAdapter(adapterType);
				}else {
					spType.setAdapter(null);
					spType.setEnabled(false);
				}
			}else {
				Toast.makeText(ConsultActivity.this, Message.NETWORK_ERROR_TRY_AGAIN,
						Toast.LENGTH_SHORT).show();
			}
			service.close();
		}
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
//			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
//			Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
//			if (!isInternetPresent) {
//				Toast.makeText(ConsultActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
//				return;
//			}
			//******检测网络*****//
			btnSubmit.setClickable(false);
			if(theClass != null && theType != null){
				if (etxtConsult.getText().toString() != null && !etxtConsult.getText().toString().equals("")) {
					GlobalObject.type = getResources().getString(
							R.string.item_consult);
					ConsultInfo info = new ConsultInfo(theClass,theType,etxtConsult.getText().toString());
					GlobalObject.message = info.toString();
					SharedPreferences sp = getSharedPreferences(
							Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences					
					String number = sp.getString(
							Message.PREFERENCE_PHONE_SERVER, "");
					if (number.equals("")) {
//						number = getResources().getString(
//								R.string.default_phone);
						//发给号码为移动平台，短信内容是content
						SmsManager.getDefault().sendTextMessage(getResources().getString(
								R.string.default_phone_mobile),
								null, info.toMessage(), null, null);
						//发给号码为联通平台，短信内容是content
						SmsManager.getDefault().sendTextMessage(getResources().getString(
								R.string.default_phone_comu),
								null, info.toMessage(), null, null);
						//发给号码为电信平台，短信内容是content
						SmsManager.getDefault().sendTextMessage(getResources().getString(
								R.string.default_phone_net),
								null, info.toMessage(), null, null);
					}else {
						// 发给号码为5556的模拟器，短信内容是content
						SmsManager.getDefault().sendTextMessage(number,
								null, info.toMessage(), null, null);
						Log.i("number", "number:"+number);
						Log.i("Message", "Message:"+info.toMessage());
					}
					Toast.makeText(
							ConsultActivity.this,
							getResources().getString(
									R.string.message_consult_tip),
							Toast.LENGTH_LONG).show();
				}
				GlobalObject.theClass = theClass;
				GlobalObject.theType = theType;
				//finish();
				Intent intent = new Intent(ConsultActivity.this,DeviceSpecActivity.class);
				startActivity(intent);
			}
			btnSubmit.setClickable(true);
			break;
		default:
			break;
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
}
