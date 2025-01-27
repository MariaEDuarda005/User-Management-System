package com.project.usersmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.usersmanagementsystem.entity.OurUsers;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // especificar quando um campo deve ser incluído na saída serializada, para garantir que o campo só seja incluído na serialização se tiver um valor não nulo
@JsonIgnoreProperties(ignoreUnknown = true) // dirá ao Jackson para ignorar atributos desconhecidos ao realizar a desserialização de json’s para objetos dessa classe.
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String email;
    private String password;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;
}
