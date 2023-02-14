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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category_id"})})
public class Status extends AbsUUIDEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Type types;

    public Status(String name, String color, Type types) {
        this.name = name;
        this.color = color;
        this.types = types;
    }
}
