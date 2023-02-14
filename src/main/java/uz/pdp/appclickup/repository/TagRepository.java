package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Tag;


public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNameAndColor(String name, String color);
}
