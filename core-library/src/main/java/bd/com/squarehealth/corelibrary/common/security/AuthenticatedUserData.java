package bd.com.squarehealth.corelibrary.common.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

@Getter
public class AuthenticatedUserData extends UsernamePasswordAuthenticationToken {

    private Long userId = 0L;
    private String name = "";
    private String email = "";
    private String role = "";

    public AuthenticatedUserData() {
        super("", "");
    }

    public AuthenticatedUserData(String email) {
        super(email, "");

        this.email = email;
    }

    public AuthenticatedUserData(Long userId, String name, String email, String role) {
        super(email, "", Arrays.asList(new SimpleGrantedAuthority(role)));

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
