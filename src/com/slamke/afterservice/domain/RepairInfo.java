package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RepairInfo implements Serializable {
	private static final long serialVersionUID = 20L;
	private Customer customer;
	private Device device;
	private Date time;
	private String phoneo;
	private boolean isChange;
	private String changeContent;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getPhoneo() {
		return phoneo;
	}

	public void setPhoneo(String phoneo) {
		this.phoneo = phoneo;
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public String getChangeContent() {
		return changeContent;
	}

	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}
	@Override
	public String toString() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return "故障发生时间为" + year + "-" + month + "-" + day 
				+"，故障产品序列号为"+ device.getNum()
				+"，故障现象为"
				+ phoneo + "，发生故障前是否改动过机器或其相关部分：" + (isChange ? "是"+ "，改动为" + (changeContent == null?"":changeContent) : "否。")
				;
	}
	
	public String toMessage() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return "QQFW\n故障发生时间为" + year + "-" + month + "-" + day 
				+"，故障产品序列号为"+ device.getNum()
				+"，故障现象为"
				+ phoneo + "，发生故障前是否改动过机器或其相关部分：" + (isChange ? "是"+ "，改动为" + (changeContent == null?"":changeContent) : "否。")
				;
	}
}
