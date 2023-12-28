package io.rezarria.service;

import io.jsonwebtoken.Claims;
import io.rezarria.model.Account;
import io.rezarria.model.AccountRole;
import io.rezarria.model.AccountRoleKey;
import io.rezarria.model.User;
import io.rezarria.security.AccountIdInfoAuthority;
import io.rezarria.security.config.CustomUserDetailsService.CustomUserDetails;
import io.rezarria.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AccountService accountService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetails userDetailsService;

    public List<Account> getAllAccount() {
        return accountService.getAll();
    }

    public JwtAndRefreshRecord login(String username, String password) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return new JwtAndRefreshRecord(jwtUtils.createToken(auth), jwtUtils.createRefreshToken(auth));
    }

    public Account register(String username, String password) {
        var account = accountService.register(username, password);
        if (account == null)
            throw new RuntimeException("Tạo tài khoản thất bại");
        var roles = roleService.getAll().stream()
                .map(role -> AccountRole.builder().id(AccountRoleKey.builder()
                                .roleId(role.getId())
                                .build()).account(account).role(role)
                        .build())
                .toList();
        account.getRoles().addAll(roles);
        var user = User.builder().build();
        account.setUser(user);
        accountService.update(account);
        return account;
    }

    @Nullable
    public Account register(RegisterDTO dto) throws Exception {
        var account = accountService.register(dto.username, dto.password);
        if (account == null)
            throw new Exception("tạo tài khoản thất bại");
        User user = new User();
        user.setAvatar(dto.avatar);
        user.setName(dto.name);
        user.setDob(dto.dob);
        account.setUser(user);
        return accountService.update(account);
    }

    public JwtAndRefreshRecord refresh(String token) {
        Claims claims = jwtUtils.decode(token);
        UUID accountId = UUID.fromString(claims.get(AccountIdInfoAuthority.NAME, String.class));
        var userDetails = userDetailsService.loadUserByAccountId(accountId);
        var authentication = new PreAuthenticatedAuthenticationToken(userDetails, null);
        return new JwtAndRefreshRecord(jwtUtils.createToken(authentication),
                jwtUtils.createRefreshToken(authentication));
    }

    /**
     * JwtAndRefreshRecord
     */
    public record JwtAndRefreshRecord(String jwt, String refresh) {
    }

    public record RegisterDTO(String username, String password, String avatar, String name, Date dob) {
    }
}
