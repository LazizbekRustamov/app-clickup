package uz.pdp.appclickup.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsUUIDEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)    // Null qiymatdagi qaytganlarni korsatma get methodida
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "project_id"})})
public class Category extends AbsUUIDEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private AccesTypes accesTypes;

    @Column(nullable = false)
    private boolean archived;

    public Category(String name, String color,AccesTypes accesTypes, boolean archived) {
        this.name = name;
        this.color = color;
        this.accesTypes = accesTypes;
        this.archived = archived;
    }
}
