package com.playa.controller;

import com.playa.model.Follow;
import com.playa.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followerId}/follow/{artistId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LISTENER') or hasRole('ARTIST')")
    public ResponseEntity<String> follow(@PathVariable Long followerId, @PathVariable Long artistId) {
        return ResponseEntity.ok(followService.followArtist(followerId, artistId));
    }

    @DeleteMapping("/{followerId}/unfollow/{artistId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LISTENER') or hasRole('ARTIST')")
    public ResponseEntity<String> unfollow(@PathVariable Long followerId, @PathVariable Long artistId) {
        return ResponseEntity.ok(followService.unfollowArtist(followerId, artistId));
    }

    @GetMapping("/{userId}/following")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LISTENER')")
    public ResponseEntity<List<Follow>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowedArtists(userId));
    }

    @GetMapping("/{artistId}/followers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<List<Follow>> getFollowers(@PathVariable Long artistId) {
        return ResponseEntity.ok(followService.getFollowers(artistId));
    }

    @GetMapping("/{artistId}/followers/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ARTIST')")
    public ResponseEntity<Long> countFollowers(@PathVariable Long artistId) {
        return ResponseEntity.ok(followService.countFollowers(artistId));
    }

    @GetMapping("/{userId}/following/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LISTENER') or hasRole('ARTIST')")
    public ResponseEntity<Long> countFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.countFollowing(userId));
    }
}
