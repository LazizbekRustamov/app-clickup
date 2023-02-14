package uz.pdp.appclickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appclickup.entity.ClickApps;
import uz.pdp.appclickup.entity.enums.ClickAppsEnum;

import java.util.Optional;

public interface ClickAppsRepository extends JpaRepository<ClickApps,Integer> {
    Optional<ClickApps> findByClickAppsEnum(ClickAppsEnum clickAppsEnum);
}
