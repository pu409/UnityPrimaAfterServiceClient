package com.slamke.afterservice.domain;

import java.io.Serializable;
import java.util.Date;

public class SellInfo implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3953112068192729411L;
	
	private Date time;
	private String content;
	
	
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return content;
	}
	
	public String toMessage() {
		return "GMBJ\n"  
				+content;
	}
	public SellInfo(String content) {
		super();
		this.time = new Date();
		this.content = content;
	}
}

