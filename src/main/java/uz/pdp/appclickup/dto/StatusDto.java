package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
public class StatusDto {
    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private UUID categoryId;

    @NotNull
    private Integer typesId;
}
