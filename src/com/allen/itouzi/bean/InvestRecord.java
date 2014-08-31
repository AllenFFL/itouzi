package com.allen.itouzi.bean;

public class InvestRecord {
	private String user, amount, time;

	public InvestRecord(String user, String amount, String time) {
		this.user = user;
		this.amount = amount;
		this.time = time;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
