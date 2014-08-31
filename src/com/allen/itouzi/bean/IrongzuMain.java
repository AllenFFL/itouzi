package com.allen.itouzi.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class IrongzuMain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5230318022957969222L;
	private int id;
	/**
	 * project_progress 预告 50 已投满
	 */
	private String project_name, project_gruantee, project_gruantee_tab,
			project_profit, project_time, project_amount, project_progress,
			project_paydate;
	/**
	 * 2 新手专享 isForNewUser
	 */
	private int isForNewUser;

	/**
	 * 项目状态 4 已还款3已投满 101 预告 倒计时 100 预发布 显示倒计时 1 正在投
	 */
	private int status;

	public IrongzuMain() {
	}

	public IrongzuMain(int id, String project_name, String project_gruantee,
			String project_gruantee_tab, String project_profit,
			String project_time, String project_amount,
			String project_progress, String project_paydate, int isForNewUser,
			int status) {
		this.id = id;
		this.project_name = project_name;
		this.project_gruantee = project_gruantee;
		this.project_gruantee_tab = project_gruantee_tab;
		this.project_profit = project_profit;
		this.project_time = project_time;
		this.project_amount = project_amount;
		this.project_progress = project_progress;
		this.project_paydate = project_paydate;
		this.isForNewUser = isForNewUser;
		this.status = status;
	}

	public IrongzuMain(Parcel in) {
		id = in.readInt();
		project_name = in.readString();
		project_gruantee = in.readString();
		project_gruantee_tab = in.readString();
		project_profit = in.readString();
		project_time = in.readString();
		project_amount = in.readString();
		project_progress = in.readString();
		project_paydate = in.readString();
		isForNewUser = in.readInt();
		status = in.readInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getProject_gruantee() {
		return project_gruantee;
	}

	public void setProject_gruantee(String project_gruantee) {
		this.project_gruantee = project_gruantee;
	}

	public String getProject_gruantee_tab() {
		return project_gruantee_tab;
	}

	public void setProject_gruantee_tab(String project_gruantee_tab) {
		this.project_gruantee_tab = project_gruantee_tab;
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

	public String getProject_amount() {
		return project_amount;
	}

	public void setProject_amount(String project_amount) {
		this.project_amount = project_amount;
	}

	public String getProject_progress() {
		return project_progress;
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

	public int getForNewUser() {
		return isForNewUser;
	}

	public void setForNewUser(int isForNewUser) {
		this.isForNewUser = isForNewUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// @Override
	// public int describeContents() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// // TODO Auto-generated method stub
	// dest.writeInt(id);
	// dest.writeString(project_name);
	// dest.writeString(project_gruantee);
	// dest.writeString(project_gruantee_tab);
	// dest.writeString(project_profit);
	// dest.writeString(project_time);
	// dest.writeString(project_amount);
	// dest.writeString(project_progress);
	// dest.writeString(project_paydate);
	// dest.writeInt(isForNewUser);
	// dest.writeInt(status);
	// }
	// public static final Parcelable.Creator<IrongzuMain> CREATOR=new
	// Creator<IrongzuMain>() {
	//
	// @Override
	// public IrongzuMain[] newArray(int size) {
	// return new IrongzuMain[size];
	// }
	//
	// @Override
	// public IrongzuMain createFromParcel(Parcel source) {
	// return new IrongzuMain(source);
	// }
	// };

}
