package uz.pdp.appclickup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.template.AbsUUIDEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category_id"})})
public class Task extends AbsUUIDEntity {
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task parentTask;

    private Timestamp dueDate;  // vazifa muddati

    @Column(updatable = false)
    private Timestamp activedDate;  // vazifa aktivlashtirilgan vaxt

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Attachment> attachmentList;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Tag> tags;


    public Task(String name, String description, Priority priority, Timestamp dueDate, Timestamp activedDate, List<Attachment> attachmentList,List<Tag> tagList) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.activedDate = activedDate;
        this.attachmentList = attachmentList;
        this.tags = tagList;
    }

    public Task(String name, String description, Status status, Category category, Priority priority, Task parentTask, Timestamp dueDate, Timestamp activedDate, List<Attachment> attachmentList) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.parentTask = parentTask;
        this.dueDate = dueDate;
        this.activedDate = activedDate;
        this.attachmentList = attachmentList;
    }
}
