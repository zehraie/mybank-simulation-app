package com.cydeo.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    @NotNull
    private UUID sender;
    @NotNull
    private UUID receiver;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    @Size(min = 2,max = 250)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String message;
    private Date creationDate;
}
