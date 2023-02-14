package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.appclickup.entity.CategoryUser;

import java.util.UUID;

public interface CategoryUserRepository extends JpaRepository<CategoryUser, UUID> {
    @Transactional // hato bolishini oldini olish
    @Modifying     // natijasi kerak emas void
    void deleteByUserIdAndCategoryId(UUID userId, UUID categoryId);

    boolean existsByUserId(UUID user_id);
}