package uz.pdp.appclickup.entity.template;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass  // Qolgan klasslar bu klasni ota sifatida korishi uchun
public abstract class AbsUUIDEntity extends AbsMainEntity{
    @Id
    @GeneratedValue(generator = "uuid2")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GenericGenerator(name = "uuid2",strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
}
