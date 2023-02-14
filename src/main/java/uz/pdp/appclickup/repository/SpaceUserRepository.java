package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.appclickup.entity.SpaceUser;

import java.util.UUID;

public interface SpaceUserRepository extends JpaRepository<SpaceUser, UUID> {
    @Transactional // hato bolishini oldini olish
    @Modifying // natijasi kerak emas void
    void deleteByUserIdAndSpaceId(UUID user_id, Long space_id);


    boolean existsByUserId(UUID user_id);
}