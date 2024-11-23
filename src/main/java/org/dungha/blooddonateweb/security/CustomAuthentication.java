package org.dungha.blooddonateweb.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
public class CustomAuthentication extends AbstractAuthenticationToken {
    private final UserDetails userDetails;

    public CustomAuthentication(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        setAuthenticated(true); // Đánh dấu người dùng đã được xác thực
    }

    @Override
    public Object getCredentials() {
        return null; // Vì bạn không lưu thông tin về mật khẩu trong CustomAuthentication
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
