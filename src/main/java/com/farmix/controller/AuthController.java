package com.farmix.controller;

import com.farmix.config.JwtProvider;
import com.farmix.entity.Cart;
import com.farmix.entity.USER_ROLE;
import com.farmix.entity.User;
import com.farmix.repository.CartRepository;
import com.farmix.repository.UserRepository;
import com.farmix.request.LoginRequest;
import com.farmix.response.AuthResponse;
import com.farmix.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createNewUser(@RequestBody User user) throws Exception {

        User isUserExist = userRepository.findByEmail(user.getEmail());

        if (isUserExist != null) {
            throw new Exception("User with email " + user.getEmail() + " already exists");
        }

        User newUser = new User();
        newUser.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setRole(user.getRole());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setWhatsappNumber(user.getWhatsappNumber());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(newUser);

        User savedUser = userRepository.save(newUser);

        Cart cartUser = new Cart();
        cartUser.setCustomer(savedUser);
        cartRepository.save(cartUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Successfully created new user");
        authResponse.setRole(user.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest user) {
        String email = user.getEmail();
        String password = user.getPassword();

        Authentication authentication = authenticate(email, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Successfully logged in");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid email...!");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password...!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
