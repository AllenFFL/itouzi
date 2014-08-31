package com.allen.itouzi.bean;

public class UserMonthIn {
	String day;
	float amount;

	public UserMonthIn() {

	}

	public UserMonthIn(String day, float amount) {
		super();
		this.day = day;
		this.amount = amount;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "UserIncome [day=" + day + ", amount=" + amount + "]";
	}

}
