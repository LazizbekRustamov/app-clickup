package uz.pdp.appclickup.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllBySpaceId(Long space_id);

    boolean existsByNameAndSpaceId(String name, Long space_id);
}
