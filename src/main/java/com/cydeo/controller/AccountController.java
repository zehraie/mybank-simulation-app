package com.cydeo.controller;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountType;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/index")
    public String getIndex(Model model){

        model.addAttribute("accountList",accountService.listAllAccount());
        return "account/index";
    }

    @GetMapping("/create-form")
    public String getCreateForm(Model model){

        //empty account object provided
        model.addAttribute("account", new AccountDTO());
        //account type enum needs to fill dropdown
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create-account";
    }

    //create method to capture information from UI,
    //print them on the console.
    //trigger createAccount method, create the account based on user input.
  //post means the user click the create button
    @PostMapping("/create")   // @Valid @ModelAttribute("account")->capturing empty form we provide as a empty opject above.
    public String createAccount(@Valid @ModelAttribute("account") AccountDTO accountDTO, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){

            model.addAttribute("accountTypes", AccountType.values());
            return "account/create-account";
        }

        System.out.println(accountDTO);
        accountService.createNewAccount(accountDTO);

        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long id){
        System.out.println(id);

        //trigger deleteAccount method
        accountService.deleteAccount(id);

        return "redirect:/index";
    }

    @GetMapping("/activate/{id}")
    public String activateAccount(@PathVariable("id") Long id){

        accountService.activateAccount(id);

        return "redirect:/index";
    }



}
