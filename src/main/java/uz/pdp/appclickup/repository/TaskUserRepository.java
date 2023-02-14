package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.appclickup.entity.TaskUser;

import java.util.UUID;

public interface TaskUserRepository extends JpaRepository<TaskUser, UUID> {
    @Transactional // hato bolishini oldini olish
    @Modifying // natijasi kerak emas void
    void deleteByUserIdAndTaskId(UUID user_id, UUID task_id);
}
