package com.vn.tbn.HouseForRent.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house")
public class House {
	@Id
	@Column(name = "code", nullable = false)
	private String code;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "price", nullable = false)
	private String price;
	
	@Column(name = "url", nullable = false)
	private String url;
	
	/*
	 * type=1  meaning Women-only
	 * type=0  meaning both
	 */
	@Column(name = "type", nullable = false)
	private Integer type;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@OneToOne(mappedBy = "house", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private HouseDetail houseDetail;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
