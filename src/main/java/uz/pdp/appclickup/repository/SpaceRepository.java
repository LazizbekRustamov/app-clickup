package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Space;

import java.util.List;
import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space,Long> {
    boolean existsByWorkspaceIdAndName(Long workspace_id, String name);
    List<Space> findAllByOwnerId(UUID owner_id);

    List<Space> findByWorkspaceId(Long workspace_id);
}
