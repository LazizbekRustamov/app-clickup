package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.Type;

public interface TypeRepository extends JpaRepository<Type,Integer> {
}
