package uz.pdp.appclickup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsLongEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
// Quyidaqi 1 ta user bir hil nomdagi workspace ocholmasin, lekin boshqa user ocholadi, 1 ta userga 1 ta uniq workspace name
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "owner_id"})})
// Bu birdaniga bir necha fieldni birga uniq qilish uchun
public class Workspace extends AbsLongEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;
    @Column(nullable = false)
    private String initialLetter;
    @ManyToOne(fetch = FetchType.LAZY)
    private Attachment avatar;


    @PrePersist   // Malumotlar omboriga qoshishdan oldin
    @PreUpdate    // Update qilishdan oldin
    public void setInitialLetterMyMethod() {
        this.initialLetter = name.substring(0, 1);
    }

    public Workspace(String name, String color, User owner, Attachment avatar) {
        this.name = name;
        this.color = color;
        this.owner = owner;
        this.avatar = avatar;
    }
}
