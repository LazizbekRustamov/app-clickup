package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.WorkspacePermission;
import uz.pdp.appclickup.entity.enums.WorkspacePermissionName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspacePermissonRepository extends JpaRepository<WorkspacePermission,UUID> {
    Optional<WorkspacePermission> findByWorkspaceRoleIdAndWorkspacePermissionName(UUID workspaceRole_id, WorkspacePermissionName workspacePermissionName);


    List<WorkspacePermission> findByWorkspaceRole_RoleNameAndWorkspaceRole_WorkspaceId(String workspaceRole_roleName, Long workspaceRole_workspace_id);
}
