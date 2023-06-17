package com.cydeo.service.impl;

import com.cydeo.entity.Account;
import com.cydeo.enums.AccountStatus;
import com.cydeo.dto.AccountDTO;
import com.cydeo.mapper.AccountMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Component;

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
    public void createNewAccount(AccountDTO accountDTO) {
    // accout status and creationDate are not coming from UI
    // we will complete the DTO
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
        accountDTO.setCreationDate(new Date());
    //convert it to entity and save it.
        accountRepository.save(accountMapper.convertToEntity(accountDTO));


    }

    @Override
    public List<AccountDTO> listAllAccount() {
        /*we are getting list of account from repo(database) but we need to return list of
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
        Account account = accountRepository.findById(id).get();
        //update the accountStatus of that object.
        account.setAccountStatus(AccountStatus.DELETED);
        // I need to save this set information above, save the updated account object
        accountRepository.save(account);
    }

    @Override
    public void activateAccount(Long id) {
        //find the account object based on id
        Account account  = accountRepository.findById(id).get();

        //update the accountStatus of that object.
        account.setAccountStatus(AccountStatus.DELETED);
        //save the updated account object
        accountRepository.save(account);

    }

    @Override
    public AccountDTO retrieveById(Long id) {
        //find the account entity based on id, then convert it to dto and return it
        Account account = accountRepository.findById(id).get();
        return accountMapper.convertToDTO(account);
    }
    // listAllActiveAccounts
    //-this method will return only activate account from the database
    // first add this login into Service interface ->AccountService
    @Override
    public List<AccountDTO> listAllActiveAccounts() {
   // we nee active accounts from repository
        List<Account> accountList = accountRepository.findAllByAccountStatus(AccountStatus.ACTIVE);
        // convert active accounts to accountDTO and return
        return accountList.stream().map(accountMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateAcccount(AccountDTO accountDTO) {
        accountRepository.save(accountMapper.convertToEntity(accountDTO));
    }

}
