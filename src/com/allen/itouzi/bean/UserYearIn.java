package com.allen.itouzi.bean;

public class UserYearIn {
	String month, amount;

	public UserYearIn() {
		// TODO Auto-generated constructor stub
	}

	public UserYearIn(String month, String amount) {
		super();
		this.month = month;
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "UserIncome [month=" + month + ", amount=" + amount + "]";
	}

}
