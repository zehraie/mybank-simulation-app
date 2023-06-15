package com.cydeo.service;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDTO createNewAccount(BigDecimal balance, Date creationDate, AccountType accountType, Long userId);

    List<AccountDTO> listAllAccount();

    void deleteAccount(UUID id);

    void activateAccount(UUID id);

    AccountDTO retrieveById(UUID id);
}
