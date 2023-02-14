package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.Tag;
import uz.pdp.appclickup.entity.Workspace;
import uz.pdp.appclickup.entity.enums.AddType;


import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

    @NotNull
    private String name;

    @NotNull
    private String color;

    @NotNull
    private Long workspaceId;
}
