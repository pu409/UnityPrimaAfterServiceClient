package com.slamke.afterservice.domain;
/**
 * 手机号码分类表 表名：tb_TelStart 所在数据库：MessageManage
 * @author sunke
 *
 */
public class CTelStart {
	public static final String TABLE_NAME = "tb_TelStart";
	
	private int ID;
	private String TelStart;
	private String SendNo;
	private String AlisaNo;
	private String Supplier;
	private String AcceptNum;
	
	
	public String getAcceptNum() {
		return AcceptNum;
	}
	public void setAcceptNum(String acceptNum) {
		AcceptNum = acceptNum;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTelStart() {
		return TelStart;
	}
	public void setTelStart(String telStart) {
		TelStart = telStart;
	}
	public String getSendNo() {
		return SendNo;
	}
	public void setSendNo(String sendNo) {
		SendNo = sendNo;
	}
	public String getAlisaNo() {
		return AlisaNo;
	}
	public void setAlisaNo(String alisaNo) {
		AlisaNo = alisaNo;
	}
	public String getSupplier() {
		return Supplier;
	}
	public void setSupplier(String supplier) {
		Supplier = supplier;
	}
	
}
