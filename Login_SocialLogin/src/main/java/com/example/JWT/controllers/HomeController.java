package com.example.JWT.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.JWT.controllers.request.AuthRequest;
import com.example.JWT.controllers.request.RefreshTokenRequest;
import com.example.JWT.dto.JwtDTO;
import com.example.JWT.dto.RefreshTokenDTO;
import com.example.JWT.exception.RefreshTokenException;
import com.example.JWT.models.RefreshToken;
import com.example.JWT.controllers.request.RegisterRequest;
import com.example.JWT.models.Client;
import com.example.JWT.models.ERole;
import com.example.JWT.models.Role;

import com.example.JWT.repository.RoleRepository;
import com.example.JWT.repository.ClientRepository;
import com.example.JWT.security.SecurityUtil;
import com.example.JWT.security.jwt.JwtUtils;
import com.example.JWT.security.services.UserDetailsImpl;
import com.example.JWT.service.implementation.RefreshTokenService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class HomeController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<JwtDTO> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails.getEmail());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());


        return ResponseEntity.ok(new JwtDTO(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) throws AuthenticationException {
        if (clientRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException("Email is taken");
        }

        // Create new user's account
        Client user = new Client();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName(ERole.ROLE_ADMIN).get();
        roles.add(role);
        user.setRoles(roles);
        clientRepository.saveAndFlush(user);

        return ResponseEntity.ok("You have registered successfully");
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(RefreshToken::getClient)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return ResponseEntity.ok(new RefreshTokenDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));
    }

    @GetMapping("/guest")
    public ResponseEntity<?> socialLogin() {
        return ResponseEntity.ok("Hello ");
    }

}
