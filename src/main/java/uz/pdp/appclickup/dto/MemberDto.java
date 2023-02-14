package uz.pdp.appclickup.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
public class MemberDto {
    private UUID id;
    private UUID roleId;
    private AddType addType;  // ADD, EDIT, REMOVE

    private String fullName;
    private String email;
    private String roleName;
    private Timestamp lastActiveTime;



    public MemberDto(UUID id, UUID roleId, AddType addType) {
        this.id = id;
        this.roleId = roleId;
        this.addType = addType;
    }
}
