package br.com.fintech.valoresareceber.controller;

import br.com.fintech.valoresareceber.model.User;
import br.com.fintech.valoresareceber.service.GovBrService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GovBrService govBrService = new GovBrService();

    @GetMapping("/status")
    public Map<String, Object> getAccountStatus(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> status = new HashMap<>();
        status.put("nivelGovBr", govBrService.obterNivelConta(oidcUser));
        status.put("twoFactorEnabled", govBrService.isTwoFactorEnabled(oidcUser));
        return status;
    }
}