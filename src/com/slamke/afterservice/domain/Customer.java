package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.List;

/**
 * �ͻ���Ϣ�� ����tb_Customer ������ݿ⣺ServiceInformationManageSystem
 * @author sunke
 *
 */
public class Customer implements Serializable 
{
	private static final long serialVersionUID = 12L;
	public static final String TABLE_NAME = "tb_Customer";
	
	/**
	 * ID	ID	int	��������1
	 */
	private int id;
	/**
	 * Num	�ͻ����	nvarchar(50)	�ǿ�
	 */
	private String num;
	/**
	 * Name	�ͻ���	nvarchar(100)	�ǿ�
	 */
	private String name;
	/**
	 * Mail	����	nvarchar(100)	�ɿ�
	 */
	private String mail;
	/**
	 * SmsTel	�����˺�	nvarchar(50)	�ǿ�
	 */
	private String smsTel;
	
	/**
	 * FaxNum	�����	nvarchar(50)	�ǿ�
	 */
	private String faxNum;
	
	/**
	 * WebAddress	��ַ	nvarchar(500)	�ɿ�
	 */
	private String webAddress;
	/**
	 * DefaultContactor	Ĭ����ϵ��	nvarchar(50)	�ǿ�
	 */
	private String defaultContactor;
	
	/**
	 * DefaultContactorTel	Ĭ����ϵ�˵绰	nvarchar(50)	�ǿ�
	 */
	private String defaultContactorTel;
	
	/**
	 * Chairman	���³�	nvarchar(50)	�ɿ�
	 */
	private String chairman;
	/**
	 * ChairmanTel	���³��绰	nvarchar(50)	�ɿ�
	 */
	private String chairmanTel;
	
	/**
	 * LegalPerson	����	nvarchar(50)	�ɿ�
	 */
	private String legalPerson;
	
	/**
	 * LegalPersonTel	���˵绰	nvarchar(50)	�ɿ�
	 */
	private String legalPersonTel;
	
	/**
	 * GeneralManager	�ܾ���	nvarchar(50)	�ɿ�
	 */
	private String generalManager;
	
	/**
	 * GeneralManagerTel	�ܾ���绰	nvarchar(50)	�ɿ�
	 */
	private String generalManagerTel;
	
	/**
	 * FinanceSupervisor	��������	nvarchar(50)	�ɿ�
	 */
	private String financeSupervisor;
	
	/**
	 * FinanceSupervisorTel	�������ܵ绰	nvarchar(50)	�ɿ�
	 */
	private String financeSupervisorTel;
	
	/**
	 * MachineSupervisor	�豸����	nvarchar(50)	�ɿ�
	 */
	private String machineSupervisor;
	
	/**
	 * MachineSupervisorTel	�豸���ܵ绰	nvarchar(50)	�ɿ�
	 */
	private String machineSupervisorTel;
	
	/**
	 * OperationStaff	������	nvarchar(50)	�ɿ�
	 */
	private String operationStaff;
	
	/**
	 * OperationStaffTel	�������绰	nvarchar(50)	�ɿ�
	 */
	private String operationStaffTel;
	
	/**
	 * Remark	��ע	nvarchar(MAX)	�ɿ�
	 */
	private String remark;
	
	private List<Device> devices;
	
	

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getSmsTel() {
		return smsTel;
	}

	public void setSmsTel(String smsTel) {
		this.smsTel = smsTel;
	}

	public String getFaxNum() {
		return faxNum;
	}

	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getDefaultContactor() {
		return defaultContactor;
	}

	public void setDefaultContactor(String defaultContactor) {
		this.defaultContactor = defaultContactor;
	}

	public String getDefaultContactorTel() {
		return defaultContactorTel;
	}

	public void setDefaultContactorTel(String defaultContactorTel) {
		this.defaultContactorTel = defaultContactorTel;
	}

	public String getChairman() {
		return chairman;
	}

	public void setChairman(String chairman) {
		this.chairman = chairman;
	}

	public String getChairmanTel() {
		return chairmanTel;
	}

	public void setChairmanTel(String chairmanTel) {
		this.chairmanTel = chairmanTel;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getLegalPersonTel() {
		return legalPersonTel;
	}

	public void setLegalPersonTel(String legalPersonTel) {
		this.legalPersonTel = legalPersonTel;
	}

	public String getGeneralManager() {
		return generalManager;
	}

	public void setGeneralManager(String generalManager) {
		this.generalManager = generalManager;
	}

	public String getGeneralManagerTel() {
		return generalManagerTel;
	}

	public void setGeneralManagerTel(String generalManagerTel) {
		this.generalManagerTel = generalManagerTel;
	}

	public String getFinanceSupervisor() {
		return financeSupervisor;
	}

	public void setFinanceSupervisor(String financeSupervisor) {
		this.financeSupervisor = financeSupervisor;
	}

	public String getFinanceSupervisorTel() {
		return financeSupervisorTel;
	}

	public void setFinanceSupervisorTel(String financeSupervisorTel) {
		this.financeSupervisorTel = financeSupervisorTel;
	}

	public String getMachineSupervisor() {
		return machineSupervisor;
	}

	public void setMachineSupervisor(String machineSupervisor) {
		this.machineSupervisor = machineSupervisor;
	}

	public String getMachineSupervisorTel() {
		return machineSupervisorTel;
	}

	public void setMachineSupervisorTel(String machineSupervisorTel) {
		this.machineSupervisorTel = machineSupervisorTel;
	}

	public String getOperationStaff() {
		return operationStaff;
	}

	public void setOperationStaff(String operationStaff) {
		this.operationStaff = operationStaff;
	}

	public String getOperationStaffTel() {
		return operationStaffTel;
	}

	public void setOperationStaffTel(String operationStaffTel) {
		this.operationStaffTel = operationStaffTel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
