package com.vbs.demo.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    int id;
    String key;
    String value;
}
