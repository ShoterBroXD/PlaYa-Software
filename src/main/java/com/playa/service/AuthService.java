package com.playa.service;

import com.playa.dto.request.LoginRequest;
import com.playa.dto.request.RegisterRequest;
import com.playa.dto.response.AuthResponse;
import com.playa.exception.DuplicateEmailException;
import com.playa.exception.RoleNotFoundException;
import com.playa.model.Customer;
import com.playa.model.Role;
import com.playa.model.RoleType;
import com.playa.model.User;
import com.playa.repository.CustomerRepository;
import com.playa.repository.RoleRepository;
import com.playa.repository.UserRepository;
import com.playa.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Role ROLE_USER not found"));
        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        // Crear Customer asociado al User con todos los datos
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setDni(request.dni());
        customer.setAddress(request.address());
        customer.setDateOfBirth(request.dateOfBirth());
        customer.setNationality(request.nationality());
        customer.setOccupation(request.occupation());
        Customer savedCustomer = customerRepository.save(customer);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedCustomer.getName(), savedCustomer.getId());

        return new AuthResponse(token, savedUser.getEmail(), request.name());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found for user"));

        String token = jwtUtil.generateToken(user.getEmail(), customer.getName(), customer.getId());

        return new AuthResponse(token, user.getEmail(), customer.getName());
    }
}