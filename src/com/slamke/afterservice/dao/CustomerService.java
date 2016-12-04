package com.slamke.afterservice.dao;

import android.content.Context;

import com.slamke.afterservice.domain.Customer;

public class CustomerService {
	private MyDBAdapter adapter;

	public CustomerService(Context _context) {
		// TODO Auto-generated constructor stub
		adapter = new MyDBAdapter(_context);
		adapter.open();
	}
	public void insertCustomer(Customer customer) {
		if(customer == null){
			return ;
		}
		Customer temp = adapter.getCustomerById(customer.getId());
		if (temp != null) {
			adapter.updateCustomer(customer);
			return;
		}
		adapter.insertCustomer(customer);
	}
	public Customer login(String tel){
		return adapter.login(tel);
	}
	/**
	 * 使用结束后必须调用
	 */
	public void close() {
		adapter.close();
	}
}
