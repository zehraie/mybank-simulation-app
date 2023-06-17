package com.cydeo.service.impl;

import com.cydeo.entity.Account;
import com.cydeo.enums.AccountStatus;
import com.cydeo.enums.AccountType;
import com.cydeo.dto.AccountDTO;
import com.cydeo.mapper.AccountMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }


    @Override
    public AccountDTO createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId) {
       //we need to create Account object
        AccountDTO accountDTO = new AccountDTO();
        //save into the database(repository)
        //return the object created

        return accountRepository.save(accountDTO);


    }

    @Override
    public List<AccountDTO> listAllAccount() {
        /*
        we are getting list of account from repo(database) but we need to return list of
        AccountDTO to controller what we need to do is we will convert Account to AccountsDTO
         */
        List<Account> accountlist = accountRepository.findAll();
// we are converting list of account to accountDTO and returning it.
        return accountlist.stream().map(accountMapper ::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        //find the account object based on id
        AccountDTO accountDTO = accountRepository.findById(id);
        //update the accountStatus of that object.
        accountDTO.setAccountStatus(AccountStatus.DELETED);
    }

    @Override
    public void activateAccount(Long id) {
        //find the account object based on id
        AccountDTO accountDTO = accountRepository.findById(id);

        //update the accountStatus of that object.
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);

    }

    @Override
    public AccountDTO retrieveById(Long id) {

        return accountRepository.findById(id);
    }
}
