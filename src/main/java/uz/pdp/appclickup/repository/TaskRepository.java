package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByNameAndCategoryId(String name, UUID category_id);

    boolean existsByIdAndPriorityName(UUID id, String priority_name);

    List<Task> findAllByCategoryId(UUID category_id);
}
