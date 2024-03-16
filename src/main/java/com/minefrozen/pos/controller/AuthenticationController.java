package com.minefrozen.pos.controller;

import com.minefrozen.pos.auth.JwtUtil;
import com.minefrozen.pos.dto.UserDto;
import com.minefrozen.pos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-pos/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private UserService userService;

    // Soon
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserDto.LoginRequest loginReq)  {

        try {
            // Token
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
            String email = authentication.getName();
            String role = authentication.getAuthorities().toString();
            log.info("AUTH DETAIL : {}", role);
            UserDto.UserLogin user = new UserDto.UserLogin(1,email,"","", 1);
            String token = jwtUtil.createToken(user);
            UserDto.LoginRes loginRes = new UserDto.LoginRes(email,token);
            return ResponseEntity.ok(loginRes);
        }catch (BadCredentialsException e){
            UserDto.ErrorRes errorResponse = new UserDto.ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            UserDto.ErrorRes errorResponse = new UserDto.ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> loginBasic(@RequestBody UserDto.LoginRequest request) {
//        String username = request.getUsername();
//        String password = request.getPassword();
//
//        Optional<UserDto.UserLogin> userOptional = userService.findByUsername(username);
//
//        if (userOptional.isPresent()) {
//            UserDto.UserLogin user = userOptional.get();
////            if (passwordEncoder.matches(password, user.getPassword())) {
//            if(password.equals(user.getPassword())){
//                user.setRole(userOptional.get().getRole());
//                user.setUserStore(userOptional.get().getUserStore());
//                return ResponseEntity.ok(user);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("Incorrect username or password");
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Incorrect username or password");
//        }
//    }

}
