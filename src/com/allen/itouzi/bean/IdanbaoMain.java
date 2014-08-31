package com.allen.itouzi.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class IdanbaoMain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7379131397774717725L;

	/**
	 * 首页推荐 id_encode "name" guarInfo.name担保公司 apr年化收益
	 * formal_time-repayment_time期限 account金额 scale进度 priority_type1一般2新手 status
	 * 发布状态 repayment_time还款日期 project_gruantor_tab=null 1404702000 1436198400
	 * 详情页 lowest_account 起投金额
	 */
	/**
	 * project_progress 预告 50 已投满
	 * 
	 */
	private String id, project_name, project_gruantor, project_gruantor_tab,
			project_profit, project_time, project_progress;

	/**
	 * 2 新手专享 isForNewUser
	 */
	private int forNewUser, project_amount;

	/**
	 * 项目状态 4 已还款3已投满 101 预告 倒计时 100 预发布 显示倒计时 1 正在投
	 */
	private int status;

	public IdanbaoMain() {
	}

	public IdanbaoMain(String id, String project_name, String project_gruantor,
			String project_gruantor_tab, String project_profit,
			String project_time, int project_amount, String project_progress,
			int isForNewUser, int status) {
		this.id = id;
		this.project_name = project_name;
		this.project_gruantor = project_gruantor;
		this.project_gruantor_tab = project_gruantor_tab;
		this.project_profit = project_profit;
		this.project_time = project_time;
		this.project_amount = project_amount;
		this.project_progress = project_progress;
		this.forNewUser = isForNewUser;
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

	public String getProject_gruantor() {
		return project_gruantor;
	}

	public void setProject_gruantor(String project_gruantor) {
		this.project_gruantor = project_gruantor;
	}

	public String getProject_progress() {
		return project_progress;
	}

	public void setProject_progress(String project_progress) {
		this.project_progress = project_progress;
	}

	public int getForNewUser() {
		return forNewUser;
	}

	public void setForNewUser(int isForNewUser) {
		this.forNewUser = isForNewUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "IdanbaoMain [id=" + id + ", project_name=" + project_name
				+ ", project_gruantor=" + project_gruantor
				+ ", project_gruantor_tab=" + project_gruantor_tab
				+ ", project_profit=" + project_profit + ", project_time="
				+ project_time + ", project_progress=" + project_progress
				+ ", forNewUser=" + forNewUser + ", project_amount="
				+ project_amount + ", status=" + status + "]";
	}

}
