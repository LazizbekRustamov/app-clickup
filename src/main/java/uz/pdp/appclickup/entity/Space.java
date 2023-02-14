package uz.pdp.appclickup.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsLongEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida

// Ikkata columnni unique qiladi
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","workspace_id"}))
public class Space extends AbsLongEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Workspace workspace;

    @Column(nullable = false)
    private String initialLetter;

    @OneToOne(fetch = FetchType.LAZY)
    private Attachment avatar;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccesTypes accesTypes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<View> view;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<ClickApps> clickApps;


    @PrePersist   // Malumotlar omboriga qoshishdan oldin
    @PreUpdate    // Update qilishdan oldin
    public void setInitialLetterMyMethod() {
        this.initialLetter = name.substring(0,1);
    }


    public Space(String name, String color, String initialLetter, Attachment avatar, AccesTypes accesTypes) {
        this.name = name;
        this.color = color;
        this.initialLetter = initialLetter;
        this.avatar = avatar;
        this.accesTypes = accesTypes;
    }
}
