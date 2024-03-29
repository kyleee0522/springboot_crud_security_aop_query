package com.kt.edu.thirdproject.common.security.controller;


import com.kt.edu.thirdproject.common.security.domain.JwtRequest;
import com.kt.edu.thirdproject.common.security.domain.JwtResponse;
import com.kt.edu.thirdproject.common.security.service.JwtUserDetailsService;
import com.kt.edu.thirdproject.common.security.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins ="*")
public class JwtAuthenticationController {

    //@Autowired
    private final AuthenticationManager authenticationManager;

    //@Autowired
    private final JwtTokenUtil jwtTokenUtil;

    //@Autowired
    private final JwtUserDetailsService userDetailsService;

    @PostMapping("/api/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
