package com.slamke.afterservice.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.slamke.afterservice.domain.AdviceInfo;
import com.slamke.afterservice.domain.CTelStart;
import com.slamke.afterservice.domain.CommentInfo;
import com.slamke.afterservice.domain.ComplaintInfo;
import com.slamke.afterservice.domain.Customer;
import com.slamke.afterservice.domain.ProductTypeClass;
import com.slamke.afterservice.domain.RepairInfo;
import com.slamke.afterservice.domain.SellInfo;
import com.slamke.afterservice.domain.Task;

public class ClassParse {

	private Gson gson;

	public ClassParse() {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

//	public String customer2String(Customer customer) {
//		try {
//			if (customer != null) {
//				String result = gson.toJson(customer);
//				return result;
//			} else {
//				return null;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
	public String adviceInfo2String(AdviceInfo info) {
		try {
			if (info != null) {
				String result = gson.toJson(info);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public String commentInfo2String(CommentInfo info) {
		try {
			if (info != null) {
				String result = gson.toJson(info);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public String repairInfo2String(RepairInfo info) {
		try {
			if (info != null) {
				String result = gson.toJson(info);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public String complaintInfo2String(ComplaintInfo info) {
		try {
			if (info != null) {
				String result = gson.toJson(info);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public String sellInfo2String(SellInfo info) {
		try {
			if (info != null) {
				String result = gson.toJson(info);
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public Customer string2Customer(String content) {
		try {
			Type type = new TypeToken<Customer>() {
			}.getType();
			if (content != null) {
				Customer record = gson.fromJson(content, type);
				return record;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	public Map<String, String> string2Map(String content) {
		try {
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			if (content != null) {
				Map<String, String> record = gson.fromJson(content, type);
				return record;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	public CTelStart string2CTelStart(String content) {
		try {
			Type type = new TypeToken<CTelStart>() {
			}.getType();
			if (content != null) {
				CTelStart record = gson.fromJson(content, type);
				return record;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	
	public List<ProductTypeClass> string2ProductTypeClassList(String content) {
		try {
			Type type = new TypeToken<List<ProductTypeClass>>() {
			}.getType();
			if (content != null) {
				List<ProductTypeClass> records = gson.fromJson(content, type);
				return records;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	public List<Task> string2TaskList(String content) {
		try {
			Type type = new TypeToken<List<Task>>() {
			}.getType();
			if (content != null) {
				List<Task> records = gson.fromJson(content, type);
				return records;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	//List<Map<Task, String>> taskPersonPair
	public Map<String, String> string2taskPersonPair(String content) {
		try {
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			if (content != null) {
				Map<String, String> records = gson.fromJson(content, type);
				return records;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
