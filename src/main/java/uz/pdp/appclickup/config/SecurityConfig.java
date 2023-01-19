package uz.pdp.appclickup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.appclickup.security.JwtFilter;
import uz.pdp.appclickup.service.AuthService;

import java.util.Properties;

@Configuration
@EnableWebSecurity        // Config klaassiga shu ikkalasini qoyish shart bu securityligini bildiradi
@EnableGlobalMethodSecurity(prePostEnabled = true)   // // Bu  @PreAuthorize(value = "hasAuthority('ADD_ROLE')") bu ishlashiga uxsat beradi yani tekshirishga ruxsat beradi
public class SecurityConfig extends WebSecurityConfigurerAdapter { // Buni extend qilish shart

    @Autowired
    AuthService authService;
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();           // Parollarni solishtirib beradi
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JavaMailSender javaMailSender(){

        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("aziziakaxon@gmail.com");
        mailSender.setPassword("Qwerty4455");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.debug","true");

        return mailSender;
    }
}
