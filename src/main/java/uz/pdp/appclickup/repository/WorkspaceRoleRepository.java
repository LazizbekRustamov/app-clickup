package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.WorkspaceRole;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRoleRepository extends JpaRepository<WorkspaceRole,UUID> {
    boolean existsByWorkspaceIdAndRoleName(Long workspace_id, String roleName);

    Optional<WorkspaceRole> findByRoleName(String roleName);
}
