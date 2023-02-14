package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
public class ProjectDto {
    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private Long spaceId;

    @NotNull
    private Integer accesTypeId;

    @NotNull
    private boolean archived;
}
