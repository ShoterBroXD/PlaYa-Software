package com.playa.repository;

import com.playa.model.Follow;
import com.playa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowed(User follower, User followed);
    void deleteByFollowerAndFollowed(User follower, User followed);

    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowed(User followed);

    long countByFollower(User follower);
    long countByFollowed(User followed);
}