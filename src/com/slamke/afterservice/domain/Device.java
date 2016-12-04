package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * �豸�� ����tb_Device������ݿ⣺ServiceInformationManageSystem
 * @author sunke
 *
 */
public class Device implements Serializable 
{
	private static final long serialVersionUID = 13L;
	
	public static final String TABLE_NAME = "tb_Device";
	/**
	 * Num	���	nvarchar(50)	����
	 */
	private String num;
	/**
	 * Type	�ͺ�	nvarchar(50)	�ǿ�
	 */
	private String type;
	/**
	 * AcceptTime	����ʱ��	datetime	�ɿ�
	 */
	private Date acceptTime;
	
	/**
	 * WarrantyPeriod	�ʱ���	int	�ɿ�
	 */
	private int warrantyPeriod;
	
	/**
	 * Customer	�ͻ����	nvarchar(50)	�ǿգ�����Ӧ�ͻ����еı�ţ�
	 */
	private Customer customer;
	
	/**
	 * ContractNum	��ͬ��	nvarchar(50)	�ɿ�
	 */
	private String contractNum;
	/**
	 * ControlSystem	����ϵͳ	nvarchar(50)	�ɿ�
	 */
	private String controlSystem;
	
	/**
	 * ControlSystemSerial	����ϵͳ���к�	nvarchar(50)	�ɿ�
	 */
	private String controlSystemSerial;
	
	/**
	 * Laser	������	nvarchar(50)	�ɿ�
	 */
	private String laser;
	
	/**
	 * LaserSerial	���������к�	nvarchar(50)	�ɿ�
	 */
	private String laserSerial;
	
	/**
	 * Programming	������	nvarchar(50)	�ɿ�
	 */
	private String programming;
	
	/**
	 * DongleNum	���ܹ����к�	nvarchar(50)	�ɿ�
	 */
	private String dongleNum;
	
	/**
	 * Remark	��ע	nvarchar(MAX)	�ɿ�
	 */
	private String remark;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public int getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(int warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getControlSystem() {
		return controlSystem;
	}

	public void setControlSystem(String controlSystem) {
		this.controlSystem = controlSystem;
	}

	public String getControlSystemSerial() {
		return controlSystemSerial;
	}

	public void setControlSystemSerial(String controlSystemSerial) {
		this.controlSystemSerial = controlSystemSerial;
	}

	public String getLaser() {
		return laser;
	}

	public void setLaser(String laser) {
		this.laser = laser;
	}

	public String getLaserSerial() {
		return laserSerial;
	}

	public void setLaserSerial(String laserSerial) {
		this.laserSerial = laserSerial;
	}

	public String getProgramming() {
		return programming;
	}

	public void setProgramming(String programming) {
		this.programming = programming;
	}

	public String getDongleNum() {
		return dongleNum;
	}

	public void setDongleNum(String dongleNum) {
		this.dongleNum = dongleNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Device() {
		super();
	}

	public Device(String num, String type, Customer customer) {
		super();
		this.num = num;
		this.type = type;
		this.customer = customer;
	}
}
