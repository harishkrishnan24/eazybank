package com.hk.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
@Data
public class AccountsDto {

    @Pattern(regexp = "(^$|[0-9]{10})", message = "AccountNumber must be 10 digits")
    @NotEmpty(message = "AccountNumber cannot be null or empty")
    @Schema(
            description = "Account number",
            example = "1234567890"
    )
    private Long accountNumber;

    @NotEmpty(message = "AccountType cannot be null or empty")
    @Schema(
            description = "Account type of the account",
            example = "Savings"
    )
    private String accountType;

    @Schema(
            description = "Eazy Bank branch address",
            example = "123 NewYork"
    )
    @NotEmpty(message = "BranchAddress cannot be null or empty")
    private String branchAddress;
}
