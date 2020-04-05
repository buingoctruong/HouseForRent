package com.vn.tbn.HouseForRent.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "price", nullable = false)
	private String price;
	
	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "house",
			cascade = CascadeType.ALL)
	private HouseDetail houseDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HouseDetail getHouseDetail() {
		return houseDetail;
	}

	public void setHouseDetail(HouseDetail houseDetail) {
		this.houseDetail = houseDetail;
	}
}
