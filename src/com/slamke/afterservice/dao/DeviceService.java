package com.slamke.afterservice.dao;

import java.util.List;

import android.content.Context;

import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.domain.Device;

public class DeviceService {
	private MyDBAdapter adapter;

	public DeviceService(Context _context) {
		// TODO Auto-generated constructor stub
		adapter = new MyDBAdapter(_context);
		adapter.open();
	}
	public void insertDevices(List<Device> devices,Customer customer) {
		if(devices == null || customer == null){
			return ;
		}
		for (int i = 0; i < devices.size(); i++) {
			devices.get(i).setCustomer(customer);
			if (!adapter.checkDeviceExist(devices.get(i))) {
				adapter.insertDevice(devices.get(i));
			}
		}
	}
	public List<Device> getDeviceByCustomer(Customer customer){
		return adapter.getDeviceByCustomer(customer);
	}
	/**
	 * 使用结束后必须调用
	 */
	public void close() {
		adapter.close();
	}
}
