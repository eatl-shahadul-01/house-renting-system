package bd.com.squarehealth.authenticator.entities;

import bd.com.squarehealth.authenticator.enumerations.UserType;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "isVerified")
    private Boolean isVerified = false;

    @NotBlank
    @Column(name = "name")
    @Size(min = 1, max = 64)
    private String name;

    @NotBlank
    @Column(name = "email", unique = true)
    @Size(min = 1, max = 512)
    private String email;

    @NotBlank
    @Column(name = "hashedPassword")
    @Size(min = 1, max = 128)
    private String hashedPassword;

    @Nullable
    @Column(name = "hashedOtp")
    @Size(min = 1, max = 128)
    private String hashedOtp;

    @Nullable
    @Column(name = "lastOtpGeneratedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastOtpGeneratedAt;

    @Column(name = "userType")
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.NONE;
}
