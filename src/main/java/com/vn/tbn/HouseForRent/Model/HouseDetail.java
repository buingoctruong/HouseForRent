package com.vn.tbn.HouseForRent.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "house_detail")
public class HouseDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@Column(name = "description", columnDefinition = "text", length = 32500)
	private String description;
	
	@Column(name = "share_facility", columnDefinition = "text", length = 32500)
	private String shareFacility;
	
	@Column(name = "room_facility", columnDefinition = "text", length = 32500)
	private String roomFacility;
	
	@Column(name = "link_image", columnDefinition = "text", length = 32500)
	private String linkImage;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
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

	public String getShareFacility() {
		return shareFacility;
	}

	public void setShareFacility(String share_facility) {
		this.shareFacility = share_facility;
	}

	public String getRoomFacility() {
		return roomFacility;
	}

	public void setRoomFacility(String room_facility) {
		this.roomFacility = room_facility;
	}

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
}
