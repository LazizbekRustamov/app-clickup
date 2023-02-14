package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.ClickApps;
import uz.pdp.appclickup.entity.View;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
public class SpaceDto {

    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private Long workspaceId;

    private UUID avatarId;

    @NotNull
    private Integer accesType_id;

    private List<View> views;

    private List<ClickApps> clickApps;

    public SpaceDto(String name, String color, Long workspaceId, UUID avatarId, Integer accesType_id) {
        this.name = name;
        this.color = color;
        this.workspaceId = workspaceId;
        this.avatarId = avatarId;
        this.accesType_id = accesType_id;
    }
}
