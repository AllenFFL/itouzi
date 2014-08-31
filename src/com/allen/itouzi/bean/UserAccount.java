package com.allen.itouzi.bean;

public class UserAccount {
	String id, user_id, total, user_money, no_use_money, collection,
			withdraw_free, use_virtual_money, no_use_virtual_money,
			invested_money, recharge_amount;

	public UserAccount() {
		// TODO Auto-generated constructor stub
	}

	public UserAccount(String id, String user_id, String total,
			String user_money, String no_use_money, String collection,
			String withdraw_free, String use_virtual_money,
			String no_use_virtual_money, String invested_money,
			String recharge_amount) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.total = total;
		this.user_money = user_money;
		this.no_use_money = no_use_money;
		this.collection = collection;
		this.withdraw_free = withdraw_free;
		this.use_virtual_money = use_virtual_money;
		this.no_use_virtual_money = no_use_virtual_money;
		this.invested_money = invested_money;
		this.recharge_amount = recharge_amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUser_money() {
		return user_money;
	}

	public void setUser_money(String user_money) {
		this.user_money = user_money;
	}

	public String getNo_use_money() {
		return no_use_money;
	}

	public void setNo_use_money(String no_use_money) {
		this.no_use_money = no_use_money;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getWithdraw_free() {
		return withdraw_free;
	}

	public void setWithdraw_free(String withdraw_free) {
		this.withdraw_free = withdraw_free;
	}

	public String getUse_virtual_money() {
		return use_virtual_money;
	}

	public void setUse_virtual_money(String use_virtual_money) {
		this.use_virtual_money = use_virtual_money;
	}

	public String getNo_use_virtual_money() {
		return no_use_virtual_money;
	}

	public void setNo_use_virtual_money(String no_use_virtual_money) {
		this.no_use_virtual_money = no_use_virtual_money;
	}

	public String getInvested_money() {
		return invested_money;
	}

	public void setInvested_money(String invested_money) {
		this.invested_money = invested_money;
	}

	public String getRecharge_amount() {
		return recharge_amount;
	}

	public void setRecharge_amount(String recharge_amount) {
		this.recharge_amount = recharge_amount;
	}

}
