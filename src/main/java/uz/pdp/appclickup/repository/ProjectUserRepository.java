package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.appclickup.entity.ProjectUser;

import java.util.UUID;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, UUID> {

    @Transactional // hato bolishini oldini olish
    @Modifying     // natijasi kerak emas void
    void deleteByUserIdAndProjectId(UUID user_id, UUID project_id);

    boolean existsByUserId(UUID userId);

}
