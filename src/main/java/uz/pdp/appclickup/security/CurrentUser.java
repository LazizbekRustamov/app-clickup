package uz.pdp.appclickup.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal // bu current userni oberadi getcontextHolerni get Authenticationni get principialini oberadi
public @interface CurrentUser {
}

// Bu anotatsiya
// User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
// shu qiladigan ishni bajaradi
