package com.cydeo.service.impl;

import com.cydeo.enums.AccountStatus;
import com.cydeo.enums.AccountType;
import com.cydeo.dto.AccountDTO;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId) {
       //we need to create Account object
        AccountDTO accountDTO = AccountDTO.builder().id(UUID.randomUUID())
                    .userId(userId).balance(balance).accountType(accountType).creationDate(creationDate)
                    .accountStatus(AccountStatus.ACTIVE).build();
        //save into the database(repository)
        //return the object created

        return accountRepository.save(accountDTO);


    }

    @Override
    public List<AccountDTO> listAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(UUID id) {
        //find the account object based on id
        AccountDTO accountDTO = accountRepository.findById(id);
        //update the accountStatus of that object.
        accountDTO.setAccountStatus(AccountStatus.DELETED);
    }

    @Override
    public void activateAccount(UUID id) {
        //find the account object based on id
        AccountDTO accountDTO = accountRepository.findById(id);

        //update the accountStatus of that object.
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);

    }

    @Override
    public AccountDTO retrieveById(UUID id) {

        return accountRepository.findById(id);
    }
}
