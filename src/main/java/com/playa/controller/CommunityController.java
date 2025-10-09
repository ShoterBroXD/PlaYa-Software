package com.playa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.playa.service.CommunityService;
import com.playa.dto.CommunityRequestDto;
import com.playa.dto.CommunityResponseDto;
import com.playa.dto.UserResponseDto;
import com.playa.dto.JoinCommunityDto;
import com.playa.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/communities")
public class CommunityController {
    
    @Autowired
    private CommunityService communityService;
    
    // POST /api/v1/communities - Crear comunidad
    @PostMapping
    public ResponseEntity<CommunityResponseDto> createCommunity(@RequestBody CommunityRequestDto communityRequestDto) {
        try {
            CommunityResponseDto createdCommunity = communityService.createCommunity(communityRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCommunity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/communities/{id} - Ver comunidad
    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponseDto> getCommunityById(@PathVariable Long id) {
        Optional<CommunityResponseDto> community = communityService.getCommunityById(id);
        return community.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/communities/{id}/users - Unirse a comunidad
    @PostMapping("/{id}/users")
    public ResponseEntity<String> joinCommunity(@PathVariable Long id, @RequestBody JoinCommunityDto joinDto) {
        try {
            communityService.joinCommunity(id, joinDto);
            return ResponseEntity.ok("Usuario unido exitosamente a la comunidad");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/communities/{id}/users - Listar miembros
    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserResponseDto>> getCommunityMembers(@PathVariable Long id) {
        try {
            List<UserResponseDto> members = communityService.getCommunityMembers(id);
            return ResponseEntity.ok(members);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/v1/communities - Obtener todas las comunidades
    @GetMapping
    public ResponseEntity<List<CommunityResponseDto>> getAllCommunities() {
        List<CommunityResponseDto> communities = communityService.getAllCommunities();
        if(communities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(communities);
    }
}