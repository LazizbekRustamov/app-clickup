package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uz.pdp.appclickup.entity.enums.AddType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
public class SpaceMemberDto {

    @NotNull
    private AddType addType;

    @NotNull
    private Long spaceId;

    @NotNull
    private UUID userId;
}
