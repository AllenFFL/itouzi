package com.allen.itouzi.bean;

public class UserTrade {
	String type_cn, detail_cn, direction, money, use_money, addtime;

	public UserTrade() {
	}

	/**
	 * 
	 * @param type_cn
	 *            操作类型
	 * @param detail_cn
	 *            具体操作名字
	 * @param direction
	 *            1为+ 其他-
	 * @param money
	 *            金额
	 * @param use_money
	 *            余额
	 * @param addtime
	 *            日期
	 */
	public UserTrade(String type_cn, String detail_cn, String direction,
			String money, String use_money, String addtime) {
		super();
		this.type_cn = type_cn;
		this.detail_cn = detail_cn;
		this.direction = direction;
		this.money = money;
		this.use_money = use_money;
		this.addtime = addtime;
	}

	public String getType_cn() {
		return type_cn;
	}

	public void setType_cn(String type_cn) {
		this.type_cn = type_cn;
	}

	public String getDetail_cn() {
		return detail_cn;
	}

	public void setDetail_cn(String detail_cn) {
		this.detail_cn = detail_cn;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getUse_money() {
		return use_money;
	}

	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "UserTrade [type_cn=" + type_cn + ", detail_cn=" + detail_cn
				+ ", direction=" + direction + ", money=" + money
				+ ", use_money=" + use_money + ", addtime=" + addtime + "]";
	}

}
