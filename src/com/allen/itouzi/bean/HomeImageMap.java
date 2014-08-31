package com.allen.itouzi.bean;

import java.io.Serializable;

public class HomeImageMap implements Serializable {
	private String imgUrl, url;

	public HomeImageMap() {
	}

	public HomeImageMap(String imgUrl, String url) {
		super();
		this.imgUrl = imgUrl;
		this.url = url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "HomeImageMap [imgUrl=" + imgUrl + ", url=" + url + "]";
	}
}
