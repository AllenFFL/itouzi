package com.allen.itouzi.bean;

import java.io.Serializable;

public class IrongzuProfit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6042176241320610024L;
	String repayment_account, repayment_time, interest, capital, days;

	public IrongzuProfit() {
	}

	public IrongzuProfit(String repayment_account, String repayment_time,
			String interest, String capital, String days) {
		super();
		this.repayment_account = repayment_account;
		this.repayment_time = repayment_time;
		this.interest = interest;
		this.capital = capital;
		this.days = days;
	}

	public String getRepayment_account() {
		return repayment_account;
	}

	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}

	public String getRepayment_time() {
		return repayment_time;
	}

	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return "IdanbaoProfit [repayment_account=" + repayment_account
				+ ", repayment_time=" + repayment_time + ", interest="
				+ interest + ", capital=" + capital + ", days=" + days + "]";
	}

}
