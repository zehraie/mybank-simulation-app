package com.cydeo.repository;

import com.cydeo.dto.AccountDTO;
import com.cydeo.exception.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountRepository {

    public static List<AccountDTO> accountDTOList = new ArrayList<>();

    public AccountDTO save(AccountDTO accountDTO){
        accountDTOList.add(accountDTO);
        return accountDTO;
    }

    public List<AccountDTO> findAll() {
        return accountDTOList;
    }

    public AccountDTO findById(Long id) {
        //TASK
        //write a method, that find the account inside the list, if not
        //throws RecordNotFoundException
        return accountDTOList.stream().filter(account -> account.getId().equals(id))
                .findAny().orElseThrow(()-> new RecordNotFoundException("Account not exist in the database."));
    }
}
