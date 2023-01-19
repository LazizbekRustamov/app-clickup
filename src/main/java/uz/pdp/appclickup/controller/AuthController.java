package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.LoginDto;
import uz.pdp.appclickup.dto.RegisterDto;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.JwtProvider;
import uz.pdp.appclickup.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;  // Parollarni solishtirib beradi configda public AuthenticationManager authenticationManagerBean() ga
    @Autowired
    JwtProvider jwtProvider;


    @PostMapping("/register")
    private HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    private HttpEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            // Bu AuthServide dagi public UserDetails loadUserByUsername(String s) dagi bn birga ishlidi
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()));  // Lohin yoki parolni tekshirib beradi

            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail());

            return ResponseEntity.ok(token);
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse("Parol yoki login hato",false));
        }
    }

    @PutMapping("/verifyEmail")
    private HttpEntity<?> verifyEmail(@RequestParam String email,@RequestParam String emailCode) {
        ApiResponse apiResponse = authService.verify(email,emailCode);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
