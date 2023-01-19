package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
//  Bu NotNullar ishlashi uchun
//                 @PostMapping
//                 private HttpEntity<?> registerUser(@Valid@RequestBody RegisterDto registerDto){
//                 return ResponseEntity.ok("");
//                 }
//    @Valid anotatsiyasini qoyishimiz kerak
