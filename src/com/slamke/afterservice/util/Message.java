package com.slamke.afterservice.util;

public class Message {
	private Message() {
	}

	public static final String SELECT_METHOD = "请选择信息的传输方式？";
	public static final String SETTING_UPDATE = "请输入更新后的设置信息？";
	public static final String ADD_PRODUCT_NUMBER = "请输入需要添加的产品序列号？";
	public final static String TEL_EMPTY = "检测不到本机号码，无法登录";
	public final static String ADVICE_EMPTY = "请输入您的建议内容";
	public final static String COMMENT_UNSTATISFY_REASON = "请输入您对于服务不满意的原因";
	public final static String DEVICE_EMPTY = "请选择您保修的产品序列号";
	public final static String REPAIR_EMPTY = "请输入您报修产品的故障现象";
	public final static String COMPLAINT_EMPTY = "请输入您的投诉内容";
	public final static String SELL_EMPTY = "请输入您想要购买的备件细项内容";
	public final static String TEL_ERROR = "请您输入正确的手机号码";
	public final static String NETWORK_ERROR = "网络连接错误，请您重新提交";
	
	public final static String NETWORK_ERROR_LOGIN = "网络连接错误，请您重新登录";
	public final static String NETWORK_ERROR_TRY_AGAIN = "网络连接错误，请您稍后再试";
	
	public final static String NETWORK_ERROR_EVALUATE_LAST_ONLY = "网络连接错误,您只能通过短信的形式评价最新完成的服务请求";
	
	public final static String NO_UNCLOSED_TASK = "目前没有可供查询的事项";
	
	public final static String NO_SIM_CARD = "请插入您的SIM卡后，重新启动服务快手";
	
	public final static String NO_UNEVALUATED_TASK = "您已经对所有事项进行了评价";
	
	public final static String LOGIN_CHECK_TIP = "由于无法检测到您的本机号码，我们将通过短信的形式获取您的手机号码以验证身份。请在收到团结普瑞玛信息化平台的短信回复后，重新尝试登录。登录前请不要删除该短信，谢谢！";
	
	public final static String INTERNET_ERROR_TRY_AGAIN = "当前网络连接状态不良，请您稍后再试";
	
	public final static String SERVER_CONFIG_ERROR = "无法连接至服务器，请检查网络配置";
	
	public static final String DIALOG_CANCEL = "取消";
	public static final String DIALOG_CONFIRM = "确定";
	public final static String PREFERENCE_NAME = "union_afterservice";
	
	public final static String PREFERENCE_USERNAME = "username";
	public final static String PREFERENCE_PASSWORD = "password";
	public final static String PREFERENCE_EMAIL = "email";
	public final static String PREFERENCE_EMAIL_ENABLE = "email_enable";
	public final static String PREFERENCE_PHONE_SERVER = "phone";
	public final static String PREFERENCE_SERVER = "server";
	public final static String PREFERENCE_PHONE_NUMBER = "phone_number";
	public final static String PREFERENCE_SIM_NUMBER = "SIM_number";
	public final static String PREFERENCE_SIM_DATE = "SIM_Date";
	
	
	public static final String ERROR = "error";

	public static final String SUCCESS = "success";
	public final static String NETWORK_FAIL = "network_error";
	public final static String TIP_LOGIN = "正在登录。。。";
	public final static String TIP_LOADING_DATA = "正在加载数据。。。";	
	public final static String TIP_UPLOADING_DATA = "正在上传数据。。。";
	public final static String TIP_SEARCHING_DATA = "正在查询数据。。。";
	

	public static final String ITEM_SPEC_TYPE = "item_spec_type";
	public static final String ITEM_SPEC_CONTENT = "item_spec_content";
	
	public static final String MENU_ITEM_IMAGE = "menu_item_image";
	public static final String MENU_ITEM_TEXT = "menu_item_text";
	
}
