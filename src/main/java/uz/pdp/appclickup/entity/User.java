package uz.pdp.appclickup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.appclickup.entity.enums.SystemRole;
import uz.pdp.appclickup.entity.template.AbsUUIDEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends AbsUUIDEntity implements UserDetails {

    private String fullName;
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    private String color;
    private String initialLetter;
    private String emailCode;
    @OneToOne(fetch = FetchType.LAZY)
    private Attachment attachment;
    private Timestamp lastActiveTime;

    private boolean enabled;
    private boolean credentialsNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean accountNonExpired=true;
    @Enumerated(value = EnumType.STRING)  // Bu field enum bolsa qoyiladi
    private SystemRole systemRoleName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority(this.systemRoleName.name());
        return Arrays.asList(authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public User(String fullName, String email, String password, SystemRole systemRoleName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.systemRoleName = systemRoleName;
    }
}


