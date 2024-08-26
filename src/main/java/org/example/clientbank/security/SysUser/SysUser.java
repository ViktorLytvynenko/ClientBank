package org.example.clientbank.security.SysUser;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.security.SysRole.SysRole;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "users")
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SysUser extends AbstractEntity {

    @Column(name = "user_name", length = 36, nullable = false)
    private String userName;

    @Column(name = "encrypted_password", length = 128, nullable = false)
    private String encryptedPassword;

    @Column(name = "enabled", length = 1, nullable = false)
    private boolean enabled;

    @OneToMany(mappedBy = "sysUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<SysRole> sysRoles = new ArrayList<>();

    public SysUser(Long id, String userName, String encryptedPassword, boolean enabled) {
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.enabled = enabled;
    }
}
