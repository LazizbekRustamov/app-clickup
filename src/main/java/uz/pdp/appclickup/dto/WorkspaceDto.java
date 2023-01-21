package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDto {
    @NotNull
    private String name;
    @NotNull
    private String color;
    private UUID avatarId;

    private Long id;

    private UUID ownerId;
    private String initialLetter;
}
