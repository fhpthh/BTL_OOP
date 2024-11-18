package org.dungha.blooddonateweb.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
public class CustomAuthentication extends org.springframework.security.authentication.AbstractAuthenticationToken {
    private final UserDetails principal;
    public CustomAuthentication(UserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.setAuthenticated(true); // Đánh dấu là đã xác thực
    }
    @Override
    public Object getCredentials() {
        return null; // Không cần lưu thông tin mật khẩu tại đây
    }
    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}