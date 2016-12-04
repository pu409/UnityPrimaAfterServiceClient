package com.slamke.afterservice.util;

import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.domain.ProductType;
import com.slamke.afterservice.domain.ProductTypeClass;
import com.slamke.afterservice.domain.RepairInfo;

public class GlobalObject {
	public static String type="";
	//public static boolean server= false;
	public static String message="";
	public static Customer customer;

	public static ProductTypeClass theClass;
	public static ProductType theType;
	public static RepairInfo info;
	public static final int TIME_OUT_SHORT = 15*1000;
	public static final int TIME_OUT_LONG = 20*1000;
	public static final int  TRY_TIMES = 3;
}
