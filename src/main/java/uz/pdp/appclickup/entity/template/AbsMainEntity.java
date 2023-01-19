package uz.pdp.appclickup.entity.template;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.appclickup.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@MappedSuperclass  // Qolgan klasslar bu klasni ota sifatida korishi uchun
@EntityListeners(AuditingEntityListener.class)
public abstract class AbsMainEntity {

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp apdatedAt;
                            // @Column bn @JoinColumn ni farqi createdAt bu oddiy field createdBy esa User klassi ozim yaratgan klass
    @JoinColumn(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User updatedBy;


}
