package uz.pdp.appclickup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appclickup.entity.enums.ClickAppsEnum;
import uz.pdp.appclickup.entity.template.AbsLongEntity;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClickApps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClickAppsEnum clickAppsEnum;

    @OneToOne(fetch = FetchType.LAZY)
    private Attachment avatar;
}
