package com.allen.itouzi.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Idanbao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 128102036355030536L;
	/**
	 * 首页推荐 id_encode "name" guarInfo.name担保公司 apr年化收益
	 * formal_time-repayment_time期限 account金额 scale进度 priority_type1一般2新手 status
	 * 发布状态 repayment_time还款日期 project_gruantor_tab=null 1404702000 1436198400
	 * 详情页 lowest_account 起投金额
	 */
	private String id, project_name, project_gruantor_tab, project_profit,
			project_time, lowest_account, project_paydate, project_progress,
			invest_persons, use_detail, repayment_source, content,
			guarantor_name, guarantor_summary, guarantor_bankbranch,
			guarantor_bankcardid, mortgage_info, risk_control,
			guarantor_opinion;
	private long project_investdate;

	/**
	 * 2 新手专享 isForNewUser
	 */
	private int forNewUser, project_amount, account_remain, account_yes;

	/**
	 * 项目状态 4 已还款3已投满 101 预告 倒计时 100 预发布 显示倒计时 1 正在投
	 */
	private int status;

	public Idanbao() {
	}

	public Idanbao(String id, String project_name, String project_profit,
			String project_time, int project_amount, String lowest_account,
			long project_investdate, String project_paydate,
			String project_progress, String invest_persons,
			Integer account_remain, int account_yes, int forNewUser,
			int status, String use_detail, String repayment_source,
			String content, String guarantor_name, String project_gruantor_tab,
			String guarantor_summary, String guarantor_bankbranch,
			String guarantor_bankcardid, String mortgage_info,
			String risk_control, String guarantor_opinion) {
		super();
		this.id = id;
		this.project_name = project_name;
		this.project_gruantor_tab = project_gruantor_tab;
		this.project_profit = project_profit;
		this.project_time = project_time;
		this.lowest_account = lowest_account;
		this.project_paydate = project_paydate;
		this.project_progress = project_progress;
		this.invest_persons = invest_persons;
		this.use_detail = use_detail;
		this.repayment_source = repayment_source;
		this.content = content;
		this.guarantor_name = guarantor_name;
		this.guarantor_summary = guarantor_summary;
		this.guarantor_bankbranch = guarantor_bankbranch;
		this.guarantor_bankcardid = guarantor_bankcardid;
		this.mortgage_info = mortgage_info;
		this.risk_control = risk_control;
		this.guarantor_opinion = guarantor_opinion;
		this.project_investdate = project_investdate;
		this.forNewUser = forNewUser;
		this.project_amount = project_amount;
		this.account_remain = account_remain;
		this.account_yes = account_yes;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getProject_gruantor_tab() {
		return project_gruantor_tab;
	}

	public void setProject_gruantor_tab(String project_gruantee_tab) {
		this.project_gruantor_tab = project_gruantee_tab;
	}

	public String getProject_profit() {
		return project_profit;
	}

	public void setProject_profit(String project_profit) {
		this.project_profit = project_profit;
	}

	public String getProject_time() {
		return project_time;
	}

	public void setProject_time(String project_time) {
		this.project_time = project_time;
	}

	public void setProject_amount(int project_amount) {
		this.project_amount = project_amount;
	}

	public int getProject_amount() {
		return project_amount;
	}

	public String getLowest_account() {
		return lowest_account;
	}

	public void setLowest_account(String lowest_account) {
		this.lowest_account = lowest_account;
	}

	public String getProject_progress() {
		return project_progress;
	}

	public String getInvest_persons() {
		return invest_persons;
	}

	public void setInvest_persons(String invest_persons) {
		this.invest_persons = invest_persons;
	}

	public void setProject_progress(String project_progress) {
		this.project_progress = project_progress;
	}

	public String getProject_paydate() {
		return project_paydate;
	}

	public void setProject_paydate(String project_paydate) {
		this.project_paydate = project_paydate;
	}

	public long getProject_investdate() {
		return project_investdate;
	}

	public void setProject_investdate(long project_investdate) {
		this.project_investdate = project_investdate;
	}

	public int getForNewUser() {
		return forNewUser;
	}

	public void setForNewUser(int isForNewUser) {
		this.forNewUser = isForNewUser;
	}

	public int getAccount_remain() {
		return account_remain;
	}

	public void setAccount_remain(int account_remain) {
		this.account_remain = account_remain;
	}

	public int getAccount_yes() {
		return account_yes;
	}

	public void setAccount_yes(int account_yes) {
		this.account_yes = account_yes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUse_detail() {
		return use_detail;
	}

	public void setUse_detail(String use_detail) {
		this.use_detail = use_detail;
	}

	public String getRepayment_source() {
		return repayment_source;
	}

	public void setRepayment_source(String repayment_source) {
		this.repayment_source = repayment_source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGuarantor_name() {
		return guarantor_name;
	}

	public void setGuarantor_name(String guarantor_name) {
		this.guarantor_name = guarantor_name;
	}

	public String getGuarantor_summary() {
		return guarantor_summary;
	}

	public void setGuarantor_summary(String guarantor_summary) {
		this.guarantor_summary = guarantor_summary;
	}

	public String getGuarantor_bankbranch() {
		return guarantor_bankbranch;
	}

	public void setGuarantor_bankbranch(String guarantor_bankbranch) {
		this.guarantor_bankbranch = guarantor_bankbranch;
	}

	public String getGuarantor_bankcardid() {
		return guarantor_bankcardid;
	}

	public void setGuarantor_bankcardid(String guarantor_bankcardid) {
		this.guarantor_bankcardid = guarantor_bankcardid;
	}

	public String getMortgage_info() {
		return mortgage_info;
	}

	public void setMortgage_info(String mortgage_info) {
		this.mortgage_info = mortgage_info;
	}

	public String getRisk_control() {
		return risk_control;
	}

	public void setRisk_control(String risk_control) {
		this.risk_control = risk_control;
	}

	public String getGuarantor_opinion() {
		return guarantor_opinion;
	}

	public void setGuarantor_opinion(String guarantor_opinion) {
		this.guarantor_opinion = guarantor_opinion;
	}

}
