package com.vn.tbn.HouseForRent.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house_detail")
public class HouseDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Lob
	@Column(name = "description", columnDefinition = "text", length = 32500)
	private String description;
	
	@Lob
	@Column(name = "share_facility", columnDefinition = "text", length = 32500)
	private String share_facility;
	
	@Lob
	@Column(name = "room_facility", columnDefinition = "text", length = 32500)
	private String room_facility;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "house_id", nullable = false)
	private House house;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShare_facility() {
		return share_facility;
	}

	public void setShare_facility(String share_facility) {
		this.share_facility = share_facility;
	}

	public String getRoom_facility() {
		return room_facility;
	}

	public void setRoom_facility(String room_facility) {
		this.room_facility = room_facility;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
}
