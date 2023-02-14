package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.pdp.appclickup.entity.enums.AddType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
@Data
public class CategoryMemberDto {

    @NotNull
    private AddType addType;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID userId;
}
