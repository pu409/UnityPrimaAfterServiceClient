package com.slamke.afterservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.dao.DeviceService;
import com.slamke.afterservice.domain.Device;
import com.slamke.afterservice.domain.RepairInfo;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
import com.slamke.wheel.widget.OnWheelChangedListener;
import com.slamke.wheel.widget.WheelView;
import com.slamke.wheel.widget.adapter.NumericWheelAdapter;
/**
 * 保修页面一--无相关网络交互
 * @author sunke
 *
 */
public class RepairFirstActivity extends Activity implements OnClickListener{
	private Button btnBack;
	private Button btnAdd;
	private Context mContext;
	private TextView txtFaultTime;
	private Spinner spNo;
	private String productNo;
	private Dialog dialog;
	private EditText etxtContent;
	WheelView year,month,day;   
    int minYear = 1970;  //最小年份  
    int fontSize = 20;   //字体大小  
    TextView txt_date;  
      
    int mYear=0,mMonth = 0,mDay = 0;
    private Button btnNext;
    String[] noArray ={""};
    private List<Device> devices;
    private Device errorDevice;
    private List<String> noList = new ArrayList<String>();
    private SpinnerAdapter adapterNO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair_first);
		initView();
		btnNext = (Button)findViewById(R.id.btn_next);
		btnNext.setOnClickListener(this);
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnAdd = (Button)findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(this);
		txtFaultTime = (TextView)findViewById(R.id.fault_time);
		spNo = (Spinner)findViewById(R.id.sp_no);
		dialog = initMethodListDlg();
		/////////////////////////////////
		DeviceService service = new DeviceService(this);
		devices = service.getDeviceByCustomer(GlobalObject.customer);
		service.close();
		if (devices != null && devices.size() > 0 ) {
			errorDevice = devices.get(0);
			productNo = errorDevice.getNum();
			List<String> nos = new ArrayList<String>(); 
			for (int i = 0; i < devices.size(); i++) {
				nos.add(devices.get(i).getNum());
				noArray = nos.toArray(noArray);
			}
		}
		if (noArray != null &&noArray.length > 0) {
			mContext = this;
			adapterNO = new SpinnerAdapter(this, android.R.layout.simple_spinner_item,noArray);
			adapterNO.setDropDownViewResource(android.R.layout.browser_link_context_header);	
			spNo.setAdapter(adapterNO);
			spNo.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(devices != null && devices.size()>0){
						errorDevice = devices.get(arg2);
						productNo = errorDevice.getNum();
					}else {
						errorDevice = null;
						productNo = null;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					errorDevice = null;
					productNo = null;
				}
			});
		}else{
			spNo.setAdapter(null);
			spNo.setEnabled(false);
		}
		
		Calendar rightNow = Calendar.getInstance();
		mDay = rightNow.get(Calendar.DAY_OF_MONTH);
		mMonth = rightNow.get(Calendar.MONTH)+1;
		mYear = rightNow.get(Calendar.YEAR);
		setShowDate();
	}
	private void initView(){       
        Calendar calendar = Calendar.getInstance();  
        mYear =calendar.get(Calendar.YEAR);  
        mMonth = calendar.get(Calendar.MONTH)+1;  
        mDay  = calendar.get(Calendar.DAY_OF_MONTH);      
        month = (WheelView) findViewById(R.id.month);  
        year = (WheelView) findViewById(R.id.year);  
        day = (WheelView) findViewById(R.id.day);  
          
        // month  
        int curMonth = calendar.get(Calendar.MONTH);  
        month.setViewAdapter(new DateNumericAdapter(this, 1, 12, curMonth));  
        month.setCurrentItem(curMonth);  
        month.addChangingListener(listener);  
        month.setCyclic(true);  
      
        // year  
        int curYear = calendar.get(Calendar.YEAR);  
        year.setViewAdapter(new DateNumericAdapter(this, minYear, curYear, curYear-minYear));  
        year.setCurrentItem(curYear-minYear);  
        year.addChangingListener(listener);  
        year.setCyclic(true);  
          
        //day  
        day.setCyclic(true);  
        updateDays(year, month, day);  
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH)-1);  
        day.addChangingListener(listener);  
    }  
      
    /** 
     * Updates day wheel. Sets max days according to selected month and year 
     */  
    void updateDays(WheelView year, WheelView month, WheelView day) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.YEAR, minYear + year.getCurrentItem());  
        calendar.set(Calendar.MONTH, month.getCurrentItem());  
          
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));  
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);  
        day.setCurrentItem(curDay - 1, true);  
          
        mYear = minYear+year.getCurrentItem();  
        mMonth = month.getCurrentItem()+1;  
        mDay = curDay;  
        calendar.set(Calendar.DAY_OF_MONTH, mDay);  
          
    }  
      
    private void setShowDate(){  
        txtFaultTime.setText(getResources().getString(R.string.fault_time)+mYear+"-"+mMonth+"-"+mDay);  
    } 
      
    OnWheelChangedListener listener = new OnWheelChangedListener() {  
        public void onChanged(WheelView wheel, int oldValue, int newValue) {  
            updateDays(year, month, day); 
            setShowDate();
        }  
    };  
      
    /** 
     * Adapter for numeric wheels. Highlights the current value. 
     */  
    private class DateNumericAdapter extends NumericWheelAdapter {  
        // Index of current item  
        int currentItem;  
        // Index of item to be highlighted  
        int currentValue;  
          
        /** 
         * Constructor 
         */  
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {  
            super(context, minValue, maxValue);  
            this.currentValue = current;  
            setTextSize(fontSize);  
        }  
          
        @Override  
        protected void configureTextView(TextView view) {  
            super.configureTextView(view);  
            if (currentItem == currentValue) {  
                view.setTextColor(0xFF0000F0);  
            }  
            view.setTypeface(Typeface.SANS_SERIF);  
        }  
          
        @Override  
        public View getItem(int index, View cachedView, ViewGroup parent) {  
            currentItem = index;  
            return super.getItem(index, cachedView, parent);  
        }  
    }  
    
    public Dialog initMethodListDlg() {
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.dialog_update, null);
		etxtContent = (EditText) view.findViewById(R.id.etxt_content);
		etxtContent
		.setInputType(InputType.TYPE_CLASS_TEXT);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Message.ADD_PRODUCT_NUMBER);
		builder.setView(view);
		builder.setCancelable(false); // �ǰ�ť
		builder.setNegativeButton(Message.DIALOG_CANCEL,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.setPositiveButton(Message.DIALOG_CONFIRM,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						noList.add(etxtContent.getText().toString());					
						adapterNO = new SpinnerAdapter(mContext, android.R.layout.simple_spinner_item,noList.toArray(noArray));
						adapterNO.setDropDownViewResource(android.R.layout.browser_link_context_header);	
						spNo.setAdapter(adapterNO);
						spNo.setSelection(noList.size()-1);
						productNo = etxtContent.getText().toString();
						dialog.dismiss();
					}
				});
		Dialog mDeleteDlg = builder.create();
		return (mDeleteDlg);
	}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					finish();
					Intent intent = new Intent(RepairFirstActivity.this,
							HomeActivity.class);
					startActivity(intent);
			return false;
		}
		return false;
	}
    
    private void goHome(){
    	finish();
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
    }
    @SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			goHome();
			break;
		case R.id.btn_add:
			dialog.show();
			break;
		case R.id.btn_next:
			if (productNo == null || productNo.equals("")) {
				Toast.makeText(RepairFirstActivity.this, Message.DEVICE_EMPTY,
						Toast.LENGTH_SHORT).show();
				return;
			}
			RepairInfo info = new RepairInfo();
			//info.setCustomer(GlobalObject.customer);
			if (errorDevice != null) {
				Device device = new Device();
				device.setNum(errorDevice.getNum());
				device.setType(errorDevice.getType());
				info.setDevice(device);
			}else {
				Device device = new Device();
				device.setType(productNo);
				info.setDevice(device);
			}
			info.setTime(new Date(mYear-1900, mMonth-1, mDay));
			GlobalObject.info = info;
			GlobalObject.type = getResources().getString(R.string.item_repair);
			Intent intent = new Intent(this,RepairSecondActivity.class);
			startActivityForResult(intent, 100);
		default:
			break;
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
        if(44==resultCode || 100 == resultCode)  
        {  
        	finish();
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }
}  
