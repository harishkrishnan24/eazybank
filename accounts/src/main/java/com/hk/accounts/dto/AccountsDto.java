package com.hk.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

    @Pattern(regexp = "(^$|[0-9]{10})", message = "AccountNumber must be 10 digits")
    @NotEmpty(message = "AccountNumber cannot be null or empty")
    private Long accountNumber;

    @NotEmpty(message = "AccountType cannot be null or empty")
    private String accountType;

    @NotEmpty(message = "BranchAddress cannot be null or empty")
    private String branchAddress;
}
