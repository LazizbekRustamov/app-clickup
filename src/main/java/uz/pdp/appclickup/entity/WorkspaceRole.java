package uz.pdp.appclickup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;
import uz.pdp.appclickup.entity.template.AbsUUIDEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"workspace_id", "roleName"})})
public class WorkspaceRole extends AbsUUIDEntity {
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Workspace workspace;
    @Column(nullable = false)
    private String roleName;
    @Enumerated(EnumType.STRING)
    private WorkspaceRoleName extendsRole;
}