package com.slamke.afterservice.domain;

import java.io.Serializable;

public class AdviceInfo implements Serializable 
{
	private static final long serialVersionUID = 22L;
	
	private String type;
	private String content;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "类型："+type+
				"\n内容："+content;
	}
	
	public String toMessage() {
		return "GKJY\n" + "类型："+type+
				"\n内容："+content;
	}
	public AdviceInfo(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}
}
