package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.List;

/**
 * �豸�ͺŷ���� ����tb_ProductTypeClass ������ݿ⣺SUPMID
 * @author sunke
 *
 */
public class ProductTypeClass implements Serializable 
{
	private static final long serialVersionUID = 15L;
	
	public static final String TABLE_NAME = "tb_ProductTypeClass";
	/**
	 * Num	���	nvarchar(50)	����
	 */
	private String num;
	/**
	 * Name	���	nvarchar(100)	�ǿ�
	 */
	private String  name;
	/**
	 * Remark	����	nvarchar(1000)	�ɿ�
	 */
	private String  remark;
	
	private List<ProductType> types;
	
	
	public List<ProductType> getTypes() {
		return types;
	}
	public void setTypes(List<ProductType> types) {
		this.types = types;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public ProductTypeClass() {
		super();
	}
	
	
	
}
