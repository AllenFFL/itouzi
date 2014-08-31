package com.allen.itouzi.bean;

import java.io.Serializable;
import java.util.List;

public class HomeData implements Serializable {
	private List<HomeImageMap> images;
	private List<IdanbaoProject> idanbaos;
	private List<IrongzuProject> irongzus;

	public HomeData() {
	}

	public HomeData(List<HomeImageMap> images, List<IdanbaoProject> idanbaos,
			List<IrongzuProject> irongzus) {
		super();
		this.images = images;
		this.idanbaos = idanbaos;
		this.irongzus = irongzus;
	}

	public List<HomeImageMap> getImages() {
		return images;
	}

	public void setImages(List<HomeImageMap> images) {
		this.images = images;
	}

	public List<IdanbaoProject> getIdanbaos() {
		return idanbaos;
	}

	public void setIdanbaos(List<IdanbaoProject> idanbaos) {
		this.idanbaos = idanbaos;
	}

	public List<IrongzuProject> getIrongzus() {
		return irongzus;
	}

	public void setIrongzus(List<IrongzuProject> irongzus) {
		this.irongzus = irongzus;
	}

	@Override
	public String toString() {
		return "HomeData [images=" + images + ", idanbaos=" + idanbaos
				+ ", irongzus=" + irongzus + "]";
	}

}
