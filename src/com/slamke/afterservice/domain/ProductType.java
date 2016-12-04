package com.slamke.afterservice.domain;

import java.io.Serializable;

/**
 * 1��	�豸�ͺű� ����tb_PoductType ������ݿ⣺SUPMID
 * @author sunke
 *
 */
public class ProductType implements Serializable 
{
	private static final long serialVersionUID = 14L;
	public static final String TABLE_NAME = "tb_PoductType";
	/**
	 * ID	ID	int	����ID������1
	 */
	private int id;
	/**
	 * Num	���	nvarchar(50)	�ǿ�
	 */
	private String num;
	

	/**
	 * Name	���	nvarchar(100)	�ɿ�
	 */
	private String name;
	/**
	 * Model	�ͺ�	nvarchar(100)	�ǿ�
	 */
	private String model;
	/**
	 * Class	������	nvarchar(1000)	�ǿգ���Ӧ�豸�ͺŷ�����еı�ţ�
	 */
	private ProductTypeClass TypeClass;
	/**
	 * Specification	���	nvarchar(1000)	�ɿ�
	 */
	private String specification;
	
	/**
	 * Remark	��ע	nvarchar(1000)	�ɿ�
	 */
	private String remark;

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}


	public ProductTypeClass getTypeClass() {
		return TypeClass;
	}

	public void setTypeClass(ProductTypeClass typeClass) {
		TypeClass = typeClass;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ProductType() {
		super();
	}
	@Override
	public String toString() {
		return "ProductType [id=" + id + ", num=" + num + ", name=" + name
				+ ", model=" + model + ", TypeClass=" + TypeClass
				+ ", specification=" + specification + ", remark=" + remark
				+ "]";
	}
}
