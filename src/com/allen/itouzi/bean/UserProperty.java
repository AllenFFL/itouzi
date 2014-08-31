package com.allen.itouzi.bean;

import java.io.Serializable;

public class UserProperty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8099956866824499180L;
	String use_money, no_use_money, no_all_sum, no_all_benjin_sum,
			couponAmount;

	public UserProperty(String use_money, String no_use_money,
			String no_all_sum, String no_all_benjin_sum, String couponAmount) {
		super();
		this.use_money = use_money;
		this.no_use_money = no_use_money;
		this.no_all_sum = no_all_sum;
		this.no_all_benjin_sum = no_all_benjin_sum;
		this.couponAmount = couponAmount;
	}

	public String getUse_money() {
		return use_money;
	}

	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}

	public String getNo_use_money() {
		return no_use_money;
	}

	public void setNo_use_money(String no_use_money) {
		this.no_use_money = no_use_money;
	}

	public String getNo_all_sum() {
		return no_all_sum;
	}

	public void setNo_all_sum(String no_all_sum) {
		this.no_all_sum = no_all_sum;
	}

	public String getNo_all_benjin_sum() {
		return no_all_benjin_sum;
	}

	public void setNo_all_benjin_sum(String no_all_benjin_sum) {
		this.no_all_benjin_sum = no_all_benjin_sum;
	}

	public String getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Double getSum() {

		return Double.parseDouble(use_money) + Double.parseDouble(no_use_money)
				+ Double.parseDouble(no_all_sum)
				+ Double.parseDouble(no_all_benjin_sum)
				+ Double.parseDouble(couponAmount);
	}

	@Override
	public String toString() {
		return "UserProperty [use_money=" + use_money + ", no_use_money="
				+ no_use_money + ", no_all_sum=" + no_all_sum
				+ ", no_all_benjin_sum=" + no_all_benjin_sum
				+ ", couponAmount=" + couponAmount + "]";
	}

}
