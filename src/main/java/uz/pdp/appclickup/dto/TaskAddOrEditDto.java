package uz.pdp.appclickup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import uz.pdp.appclickup.entity.Attachment;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAddOrEditDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private UUID statusId;

    @NotNull
    private UUID categoryId;

    private UUID parentTaskId;

    private Timestamp dueDate;  // vazifa muddati

    private List<Attachment> attachmentList;
}
