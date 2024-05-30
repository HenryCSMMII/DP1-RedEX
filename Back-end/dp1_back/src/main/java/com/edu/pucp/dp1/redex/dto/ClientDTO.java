package com.edu.pucp.dp1.redex.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ClientDTO {

    private int id;
    private String fullName;
    private String docType;
    private int docNumber;
    private String email;
    private String cellphone;
}
