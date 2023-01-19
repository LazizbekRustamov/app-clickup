package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.WorkspacePermission;

import java.util.UUID;

public interface WorkspacePermissonRepository extends JpaRepository<WorkspacePermission,UUID> {
}
