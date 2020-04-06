package com.vn.tbn.HouseForRent.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.tbn.HouseForRent.Model.House;

@Repository
public interface HouseRepository extends JpaRepository<House, String> {
}
