package com.slamke.afterservice.domain;

import java.util.Date;

/**
 * 任务表 表名：tb_Task 所在数据库: ServiceInformationManageSystem
 * @author sunke
 * 
 */
public class Task {
	public static final String TABLE_NAME = "tb_Task";
	/**
	 * 任务号 Nvarchar(50)
	 */
	private String Num;
	/**
	 * 客户编号 Nvarchar(50)
	 */
	private String CustomerNum;
	/**
	 * 来源 Nvarchar(100) 手机程序为:其它
	 */
	private String Source;
	/**
	 * 联系人 Nvarchar(100)
	 */
	private String DefaultContactor;
	/**
	 * 联系人电话 Nvarchar(100)
	 */
	private String DefaultContactorTel;
	/**
	 * 设备编号 Nvarchar(50)
	 */
	private String DeviceNum;
	/**
	 * 内容 Nvarchar(Max)
	 */
	private String Content;
	/**
	 * 备注 Nvarchar(Max)
	 */
	private String Remark;
	/**
	 * 状态 Nvarchar(50)
	 */
	private String Status;

	private String Name;

	private String ContractNum;
	private String FastMailNum;
	private String ControlledObjectNum;
	private String AnalysePerson;
	/**
	 * 分析判断时间
	 */
	private Date AnalyseTime;
	private String AnalyseResult;
	private int PreScore;
	private int Score;
	private String ResetScoreCause;
	private int PreCost;
	private int Cost;
	private String TaskType;
	private float PreUseDay;
	/**
	 * 执行开始时间
	 */
	private Date StartTime;
	/**
	 * 完成时间
	 */
	private Date CompleteTime;
	/**
	 * 客户评价时间
	 */
	private Date ConfirmCompleteTime;
	/**
	 * 客户评价
	 */
	private String Evaluate;
	/**
	 * 添加任务的时间
	 */
	private Date InsertTime;
	/**
	 * 下达时间
	 */
	private Date SendTime;
	private boolean Close;
	private boolean IsSimple;
	private String SimpleType;
	private String JoinTask;
	private boolean IsOur;
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getContractNum() {
		return ContractNum;
	}

	public void setContractNum(String contractNum) {
		ContractNum = contractNum;
	}

	public String getFastMailNum() {
		return FastMailNum;
	}

	public void setFastMailNum(String fastMailNum) {
		FastMailNum = fastMailNum;
	}

	public String getControlledObjectNum() {
		return ControlledObjectNum;
	}

	public void setControlledObjectNum(String controlledObjectNum) {
		ControlledObjectNum = controlledObjectNum;
	}

	public String getAnalysePerson() {
		return AnalysePerson;
	}

	public void setAnalysePerson(String analysePerson) {
		AnalysePerson = analysePerson;
	}

	public Date getAnalyseTime() {
		return AnalyseTime;
	}

	public void setAnalyseTime(Date analyseTime) {
		AnalyseTime = analyseTime;
	}

	public String getAnalyseResult() {
		return AnalyseResult;
	}

	public void setAnalyseResult(String analyseResult) {
		AnalyseResult = analyseResult;
	}

	public int getPreScore() {
		return PreScore;
	}

	public void setPreScore(int preScore) {
		PreScore = preScore;
	}

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public String getResetScoreCause() {
		return ResetScoreCause;
	}

	public void setResetScoreCause(String resetScoreCause) {
		ResetScoreCause = resetScoreCause;
	}

	public int getPreCost() {
		return PreCost;
	}

	public void setPreCost(int preCost) {
		PreCost = preCost;
	}

	public int getCost() {
		return Cost;
	}

	public void setCost(int cost) {
		Cost = cost;
	}

	public String getTaskType() {
		return TaskType;
	}

	public void setTaskType(String taskType) {
		TaskType = taskType;
	}

	public float getPreUseDay() {
		return PreUseDay;
	}

	public void setPreUseDay(float preUseDay) {
		PreUseDay = preUseDay;
	}

	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}

	public Date getCompleteTime() {
		return CompleteTime;
	}

	public void setCompleteTime(Date completeTime) {
		CompleteTime = completeTime;
	}

	public Date getConfirmCompleteTime() {
		return ConfirmCompleteTime;
	}

	public void setConfirmCompleteTime(Date confirmCompleteTime) {
		ConfirmCompleteTime = confirmCompleteTime;
	}

	public String getEvaluate() {
		return Evaluate;
	}

	public void setEvaluate(String evaluate) {
		Evaluate = evaluate;
	}

	public Date getInsertTime() {
		return InsertTime;
	}

	public void setInsertTime(Date insertTime) {
		InsertTime = insertTime;
	}

	public Date getSendTime() {
		return SendTime;
	}

	public void setSendTime(Date sendTime) {
		SendTime = sendTime;
	}

	public boolean isClose() {
		return Close;
	}

	public void setClose(boolean close) {
		Close = close;
	}

	public boolean isIsSimple() {
		return IsSimple;
	}

	public void setIsSimple(boolean isSimple) {
		IsSimple = isSimple;
	}

	public String getSimpleType() {
		return SimpleType;
	}

	public void setSimpleType(String simpleType) {
		SimpleType = simpleType;
	}

	public String getJoinTask() {
		return JoinTask;
	}

	public void setJoinTask(String joinTask) {
		JoinTask = joinTask;
	}

	public boolean isIsOur() {
		return IsOur;
	}

	public void setIsOur(boolean isOur) {
		IsOur = isOur;
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = num;
	}

	public String getCustomerNum() {
		return CustomerNum;
	}

	public void setCustomerNum(String customerNum) {
		CustomerNum = customerNum;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public String getDefaultContactor() {
		return DefaultContactor;
	}

	public void setDefaultContactor(String defaultContactor) {
		DefaultContactor = defaultContactor;
	}

	public String getDefaultContactorTel() {
		return DefaultContactorTel;
	}

	public void setDefaultContactorTel(String defaultContactorTel) {
		DefaultContactorTel = defaultContactorTel;
	}

	public String getDeviceNum() {
		return DeviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		DeviceNum = deviceNum;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
