package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.RegisterDto;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.entity.enums.SystemRole;
import uz.pdp.appclickup.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;

    @Override            // Bu  Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())); dagi bn birga ishlidi
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(email));
    }

    public ApiResponse registerUser(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("Bunday foydalanuvchi mavjud", false);
        }
        User user = new User(
                registerDto.getFullName(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                SystemRole.SYSTEM_ROLE_USER
        );
        int code = new Random().nextInt(999999);
        user.setEmailCode(String.valueOf(code).substring(0,4));

        sendEmail(user.getEmail(), user.getEmailCode());

        userRepository.save(user);
        return new ApiResponse("User saqlandi",true);
    }


    public Boolean sendEmail(String email, String emailCode) {
//        try {     //
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Pdp.com");
        message.setTo(email);
        message.setSubject("Hellouuu nigge");
//        message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + email + "'>Tasdiqlang</a>");
        message.setText(emailCode);
//            javaMailSender.send(message);  //

        System.out.println("***********"+message.getText());

        return true;
//        } catch (Exception e) {   //
//            return false; //
//        }//
    }



    public ApiResponse verify(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (emailCode.equals(user.getEmailCode())){
                user.setEnabled(true);
//                user.setEmailCode(null);   emailni ishlatganimdan keyin
                userRepository.save(user);

                return new ApiResponse("Akaunt tasdiqlandi",true);
            }
            return new ApiResponse("Kod xato",false);
        }
        return new ApiResponse("Bunday user yoq",false);
    }
}
