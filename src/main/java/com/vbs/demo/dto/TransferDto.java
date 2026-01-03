package com.vbs.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowNonPortable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    String username;
    int id;
    double amount;
}
