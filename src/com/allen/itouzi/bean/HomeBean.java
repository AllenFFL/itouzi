package com.allen.itouzi.bean;

import java.io.Serializable;

public class HomeBean implements Serializable {
	private HomeData homedata;
	private String code, info;

	public HomeBean() {
	}

	public HomeBean(HomeData homedata, String code, String info) {
		super();
		this.homedata = homedata;
		this.code = code;
		this.info = info;
	}

	public HomeData getHomedata() {
		return homedata;
	}

	public void setHomedata(HomeData homedata) {
		this.homedata = homedata;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "HomeBean [homedata=" + homedata + ", code=" + code + ", info="
				+ info + "]";
	}

}
