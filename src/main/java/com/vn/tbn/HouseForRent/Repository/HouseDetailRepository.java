package com.vn.tbn.HouseForRent.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.tbn.HouseForRent.Model.HouseDetail;

@Repository
public interface HouseDetailRepository extends JpaRepository<HouseDetail, Integer>{

}
