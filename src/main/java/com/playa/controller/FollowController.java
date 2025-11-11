package com.playa.controller;

import com.playa.model.Follow;
import com.playa.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followerId}/follow/{artistId}")
    public ResponseEntity<String> follow(@PathVariable Long followerId, @PathVariable Long artistId) {
        return ResponseEntity.ok(followService.followArtist(followerId, artistId));
    }

    @DeleteMapping("/{followerId}/unfollow/{artistId}")
    public ResponseEntity<String> unfollow(@PathVariable Long followerId, @PathVariable Long artistId) {
        return ResponseEntity.ok(followService.unfollowArtist(followerId, artistId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Follow>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowedArtists(userId));
    }

    @GetMapping("/{artistId}/followers")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable Long artistId) {
        return ResponseEntity.ok(followService.getFollowers(artistId));
    }

    @GetMapping("/{artistId}/followers/count")
    public ResponseEntity<Long> countFollowers(@PathVariable Long artistId) {
        return ResponseEntity.ok(followService.countFollowers(artistId));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> countFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.countFollowing(userId));
    }
}
