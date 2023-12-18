package io.rezarria.sanbong.security.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.RegisterTemplate;
import io.rezarria.sanbong.model.RegisterTemplateRole;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.security.Details;
import io.rezarria.sanbong.security.config.CustomUserDetailsService.CustomUserDetails;
import io.rezarria.sanbong.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AccountService accountService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RegisterTemplateService registerTemplateService;
    private final CustomUserDetails userDetailsService;

    public List<Account> getAllAccount() {
        return accountService.getAll();
    }

    /**
     * JwtAndRefreshRecord
     */
    public record JwtAndRefreshRecord(String jwt, String refresh) {
    }

    public JwtAndRefreshRecord login(String username, String password) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Optional<Account> result = accountService.getAccountByUsername(username);
        Account account = result.orElseThrow();
        Details details = new Details();
        if (account.getUser() != null) {
            details.setUserId(account.getUser().getId());
        }
        details.setAccountId(account.getId());
        ((AbstractAuthenticationToken) auth).setDetails(details);
        return new JwtAndRefreshRecord(jwtUtils.createToken(auth), jwtUtils.createRefreshToken(auth));
    }

    public Account register(String username, String password) {

        return accountService.register(username, password, roleService.getAll().stream());
    }

    @Nullable
    public Account register(RegisterDTO dto) throws Exception {
        RegisterTemplate template = registerTemplateService.getNewest().orElseThrow();
        var account = accountService.register(dto.username, dto.password,
                template.getRoles().stream().map(i -> i.getRole()));
        if (account == null)
            throw new Exception("tạo tài khoản thất bại");
        User user = new User();
        user.setAvatar(dto.avatar);
        user.setName(dto.name);
        user.setDob(dto.dob);
        account.setUser(user);
        return accountService.add(account);
    }

    public JwtAndRefreshRecord refresh(String token) {
        Claims claims = jwtUtils.decode(token);
        Details details = Details.from(claims);
        UUID accountId = details.getAccountId();
        var userDetails = userDetailsService.loadUserByAccountId(accountId);
        var authentication = new PreAuthenticatedAuthenticationToken(userDetails, null);
        Account account = accountService.get(accountId);
        details = new Details();
        if (account.getUser() != null) {
            details.setUserId(account.getUser().getId());
        }
        details.setAccountId(account.getId());
        authentication.setDetails(details);
        return new JwtAndRefreshRecord(jwtUtils.createToken(authentication),
                jwtUtils.createRefreshToken(authentication));
    }

    public record RegisterDTO(String username, String password, String avatar, String name, Date dob) {
    }
}
