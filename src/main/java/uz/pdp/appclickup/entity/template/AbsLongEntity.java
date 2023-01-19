package uz.pdp.appclickup.entity.template;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass  // Qolgan klasslar bu klasni ota sifatida korishi uchun
public abstract class AbsLongEntity extends AbsMainEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
