package bd.com.squarehealth.corelibrary.common.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;

@Getter
public class ExtendedUserDetails extends User {

    private Long userId;
    private String name;
    private String email;
    private String role;

    public ExtendedUserDetails(Long userId, String name, String email, String password, String role) {
        super(email, password, Arrays.asList(new SimpleGrantedAuthority(role)));

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
