package com.Acerise.System_api.Config.Security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProp
        (RSAPublicKey publicKey, RSAPrivateKey privateKey){
}
