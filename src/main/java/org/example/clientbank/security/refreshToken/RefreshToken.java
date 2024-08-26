package org.example.clientbank.security.refreshToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.clientbank.AbstractEntity;
import org.example.clientbank.security.SysUser.SysUser;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "refresh_tokens")
public class RefreshToken extends AbstractEntity {

    @Column(name = "refresh_token")
    String refreshToken;

    @Column(name = "is_valid")
    Boolean isValid;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    SysUser sysUser;
}
