package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status, UUID> {
    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    List<Status> findAllByCategoryId(UUID category_id);

    Optional<Status> findByIdAndCategoryId(UUID id, UUID category_id);
}
