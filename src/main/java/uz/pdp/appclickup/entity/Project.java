package uz.pdp.appclickup.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsUUIDEntity;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "space_id"})})
public class Project extends AbsUUIDEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private AccesTypes accesTypes;

    @Column(nullable = false)
    private boolean archived;

    public Project(String name, String color,AccesTypes accesTypes, boolean archived) {
        this.name = name;
        this.color = color;
        this.accesTypes = accesTypes;
        this.archived = archived;
    }
}
