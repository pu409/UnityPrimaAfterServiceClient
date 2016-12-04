package com.slamke.afterservice.domain;

import java.io.Serializable;

public class ConsultInfo implements Serializable 
{
	private static final long serialVersionUID = 21L;
	
	private ProductTypeClass typeClass;
	private ProductType type;
	private String remark;
	public ProductTypeClass getTypeClass() {
		return typeClass;
	}
	public void setTypeClass(ProductTypeClass typeClass) {
		this.typeClass = typeClass;
	}
	public ProductType getType() {
		return type;
	}
	public void setType(ProductType type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	public ConsultInfo() {
		super();
	}
	public ConsultInfo(ProductTypeClass typeClass, ProductType type,
			String remark) {
		super();
		this.typeClass = typeClass;
		this.type = type;
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "GKZX\n设备型号分类：" + typeClass.getName() 
				+ "\n设备型号：" + type.getModel()
				+ "\n留言：" + remark;
	}
	
	public String toMessage() {
		return "GKZX\n设备型号分类：" + typeClass.getName() 
				+ "\n设备型号：" + type.getModel()
				+ "\n留言：" + remark;
	}

}
