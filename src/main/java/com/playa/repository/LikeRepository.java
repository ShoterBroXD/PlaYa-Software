package com.playa.repository;

import com.playa.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.playa.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {


}
