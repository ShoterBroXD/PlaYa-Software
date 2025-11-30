package com.playa.controller;

import com.playa.dto.FollowResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponseDto>> getFollowing(@PathVariable Long userId) {
        // Mock data: return empty list for now
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/{artistId}")
    public ResponseEntity<Void> followArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> unfollowArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{artistId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long artistId) {
        return ResponseEntity.ok(false);
    }
}
