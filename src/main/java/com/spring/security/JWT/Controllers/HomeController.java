package com.spring.security.JWT.Controllers;

import com.spring.security.JWT.Models.AuthenticationRequest;
import com.spring.security.JWT.Models.AuthenticationResponse;
import com.spring.security.JWT.Services.MyUserDetailsService;
import com.spring.security.JWT.Util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class HomeController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @GetMapping("/hello")
    public ResponseEntity<String> HelloHome() {
        try{
            return new ResponseEntity<String>("Hello Home", HttpStatus.OK);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Exception caught in Hello Home");
        }
    }
    @PostMapping("/authenticate")
    public ResponseEntity <?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
            //getting the user details
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            String jwt = jwtUtils.generateToken(userDetails);
            return new ResponseEntity<>(new AuthenticationResponse(jwt),HttpStatus.OK);

        }
        catch(BadCredentialsException bce) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Incorrect Username or Password");
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Not able to authenticate");
        }
    }
}
