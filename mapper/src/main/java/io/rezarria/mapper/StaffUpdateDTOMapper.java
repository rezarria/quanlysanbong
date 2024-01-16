package io.rezarria.mapper;

import io.rezarria.dto.update.StaffUpdateDTO;
import io.rezarria.model.Staff;
import io.rezarria.repository.AccountRepository;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StaffUpdateDTOMapper {

    @Autowired
    private AccountRepository accountRepository;

    public abstract void convert(StaffUpdateDTO dto, @MappingTarget Staff staff);

    public void patch(StaffUpdateDTO dto, @MappingTarget Staff user) {
        convert(dto, user);
        if (dto.getAccountId() == null) {
            user.getAccount().setUser(null);
            user.setAccount(null);
        } else if (user.getAccount() == null) {
            var account = accountRepository.findById(dto.getAccountId()).orElseThrow();
            account.setUser(user);
            user.setAccount(account);
        } else {
            var account = user.getAccount();
            account.setUser(null);
            account = accountRepository.findById(dto.getAccountId()).orElseThrow();
            user.setAccount(account);
        }
    }
}
