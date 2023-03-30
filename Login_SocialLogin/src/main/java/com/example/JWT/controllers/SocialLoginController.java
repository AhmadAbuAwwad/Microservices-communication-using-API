package com.example.JWT.controllers;

import com.example.JWT.controllers.request.AuthRequest;
import com.example.JWT.controllers.request.RefreshTokenRequest;
import com.example.JWT.controllers.request.RegisterRequest;
import com.example.JWT.dto.JwtDTO;
import com.example.JWT.dto.RefreshTokenDTO;
import com.example.JWT.exception.RefreshTokenException;
import com.example.JWT.models.Client;
import com.example.JWT.models.ERole;
import com.example.JWT.models.RefreshToken;
import com.example.JWT.models.Role;
import com.example.JWT.repository.ClientRepository;
import com.example.JWT.repository.RoleRepository;
import com.example.JWT.security.SecurityUtil;
import com.example.JWT.security.jwt.JwtUtils;
import com.example.JWT.security.services.UserDetailsImpl;
import com.example.JWT.service.implementation.RefreshTokenService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class SocialLoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("/")
    public ResponseEntity<JwtDTO> hello(OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        //  Add new user && login || Login if exist
        String userEmail = oAuth2AuthenticationToken.getPrincipal().getAttributes().get("email").toString();
        String username = oAuth2AuthenticationToken.getPrincipal().getAttributes().get("name").toString();

        //  Create User
        Optional<Client> client = clientRepository.findByEmail(userEmail);
        if (!client.isPresent()) {
            // Create new user's account
            Client user = new Client();
            user.setEmail(userEmail);
            user.setUsername(username);

            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByRoleName(ERole.ROLE_ADMIN).get();
            roles.add(role);
            user.setRoles(roles);
            client = Optional.of(clientRepository.saveAndFlush(user));
        }

        String jwt = jwtUtils.generateJwtToken(userEmail);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(client.get().getId());

        List<String> roles = client.get().getRoles().stream().map(item -> item.getRoleName().toString())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtDTO(jwt, refreshToken.getToken(), client.get().getId(),
                client.get().getUsername(), client.get().getEmail(), roles));
    }
}
