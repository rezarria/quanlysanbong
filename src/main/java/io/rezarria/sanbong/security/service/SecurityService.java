package io.rezarria.sanbong.security.service;

import io.jsonwebtoken.Claims;
import io.rezarria.sanbong.model.Account;
import io.rezarria.sanbong.model.RegisterTemplate;
import io.rezarria.sanbong.model.RegisterTemplateRole;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.security.Details;
import io.rezarria.sanbong.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AccountService accountService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RegisterTemplateService registerTemplateService;

    public List<Account> getAllAccount() {
        return accountService.getAll();
    }

    public String login(String username, String password) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Optional<Account> result = accountService.getAccountByUsername(username);
        Account account = result.orElseThrow();
        Details details = new Details();
//        if (account.getUser() != null) {
//            details.setUserId(account.getUser().getId());
//        }
        details.setAccountId(account.getId());
        ((AbstractAuthenticationToken) auth).setDetails(details);
        return jwtUtils.createToken(auth);
    }

    public Account register(String username, String password) {
        return accountService.register(username, password, roleService.getAll().stream());
    }

    @Nullable
    public Account register(RegisterDTO dto) {
        RegisterTemplate template = registerTemplateService.getNewest().orElseThrow();
        Account account = accountService.make(dto.username, dto.password, template.getRoles().stream().map(RegisterTemplateRole::getRole));
        account.setActive(template.isActive());
        User user = new User();
        user.setAvatar(dto.avatar);
        user.setName(dto.name);
        user.setDob(dto.dob);
        account.setUser(user);
        return accountService.add(account);
    }

    public void refresh(String token) {
        Claims claims = jwtUtils.refresh(token);
        Details details = Details.from(claims);
    }

    public record RegisterDTO(String username, String password, String avatar, String name, Date dob) {
    }
}
