package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean succes;
    private String token;


    public ApiResponse(String message, boolean succes) {
        this.message = message;
        this.succes = succes;
    }
}
