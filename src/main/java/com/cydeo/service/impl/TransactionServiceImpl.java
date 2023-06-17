package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.entity.Transaction;
import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.mapper.TransactionMapper;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;
// oin the service class use serviceClass if you need bind any instance;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }


    @Override
    public TransactionDTO makeTransfer(AccountDTO sender, AccountDTO receiver, BigDecimal amount, Date creationDate, String message) {

        if (!underConstruction) {
            validateAccount(sender,receiver);
            checkAccountOwnership(sender,receiver);
            executeBalanceAndUpdateIfRequired(amount,sender,receiver);
        /*
            after all validations are completed, and money is transferred, we need to  create Transaction object and save/return it.
         */

            TransactionDTO transactionDTO = new TransactionDTO(sender,receiver,amount,message,creationDate);
    transactionRepository.save(transactionMapper.convertToEntity(transactionDTO)); //saved into db as a entity
            return transactionDTO;  //return dto object

        }else {
            throw new UnderConstructionException("App is under construction,try again later.");
        }



    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, AccountDTO sender, AccountDTO receiver) {
        if(checkSenderBalance(sender,amount)){
            //update sender and receiver balance
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
            // get the dto from database for both sender and receiver, update balance and save it.
            //create accountSerrvice updateSccount method and use it for save

            //find the sender account
            AccountDTO senderAcc = accountService.retrieveById(sender.getId());
            senderAcc.setBalance(sender.getBalance());
            //save again to database
            accountService.updateAcccount(senderAcc);
            //find receiver account
            AccountDTO receiverAcc = accountService.retrieveById(receiver.getId());
            receiverAcc.setBalance(receiver.getBalance());
            accountService.updateAcccount(receiverAcc);


        }else{
            throw new BalanceNotSufficientException("Balance is not enough for this transfer.");
        }

    }

    private boolean checkSenderBalance(AccountDTO sender, BigDecimal amount) {
        //verify sender has enough balance to send
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >=0;
    }

    private void checkAccountOwnership(AccountDTO sender, AccountDTO receiver) {
        /*
            write an if statement that checks if one of the account is saving,
            and user of sender or receiver is not the same, throw AccountOwnershipException
         */

        if((sender.getAccountType().equals(AccountType.SAVING)||receiver.getAccountType().equals(AccountType.SAVING))
          && !sender.getUserId().equals(receiver.getUserId())){
            throw new AccountOwnershipException("Since you are using a savings account, the sender and receiver userId must be the same.");
        }

    }

    private void validateAccount(AccountDTO sender, AccountDTO receiver) {
        /*
            -if any of the account is null
            -if account ids are the same(same account)
            -if the accounts exist in the database(repository)
         */

        if(sender==null||receiver==null){
            throw new BadRequestException("Sender or Receiver cannot be null");
        }

        if(sender.getId().equals(receiver.getId())){
            throw new BadRequestException("Sender account needs to be different than receiver");
        }
        //verify if we have sender and receiver in the database
        findAccountById(sender.getId());
        findAccountById(receiver.getId());


    }

    private AccountDTO findAccountById(Long id) {
       return accountService.retrieveById(id);  // this is already DTO
    }

    @Override
    public List<TransactionDTO> findAllTransaction() {
        //get the transaction entity for all and return them as a list of transactionDTO
        List<Transaction> allTransactions = transactionRepository.findAll();
        return allTransactions.stream().map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> last10Transactions() {
        //we want last 10 latest transaction
        //write a native query to get the result for last 10 transaction
        //then convert it to dto and return it
        List<Transaction> last10Transactions = transactionRepository.findLast10Transactions();
        return last10Transactions.stream().map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> findTransactionListById(Long id) {
        //write a JPQL query to retrieve list of transactions by id
        List<Transaction> transactionListById = transactionRepository.findTransactionListById(id);
        //convert it to dto and return it
        return transactionListById.stream().map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
