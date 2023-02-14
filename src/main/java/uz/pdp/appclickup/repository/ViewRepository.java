package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.View;
import uz.pdp.appclickup.entity.enums.Views;

import java.util.List;
import java.util.Optional;

public interface ViewRepository extends JpaRepository<View,Integer> {
    Optional<View> findByViews(Views views);
}
