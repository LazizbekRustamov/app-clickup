package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private UUID id;
    private UUID roleId;
    private AddType addType;  // ADD, EDIT, REMOVE
}
