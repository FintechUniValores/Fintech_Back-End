package br.com.fintech.valoresareceber.service;

import br.com.fintech.valoresareceber.model.User;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class GovBrService {

    // Extrai o nível da conta do acr claim do id_token
    public String obterNivelConta(OidcUser oidcUser) {
        Object acr = oidcUser.getClaims().get("acr");
        return acr != null ? acr.toString() : "N/A";
    }

    // Verifica se o 2FA está habilitado (exemplo: acr = "urn:mace:incommon:iap:multi-factor")
    public boolean isTwoFactorEnabled(OidcUser oidcUser) {
        String acr = obterNivelConta(oidcUser);
        return acr != null && acr.toLowerCase().contains("multi-factor");
    }

    // Persiste ou atualiza o usuário no banco (exemplo simplificado)
    public User persistirOuAtualizar(User user) {
        // Implemente a lógica de persistência usando seu repository
        // Exemplo: return userRepository.save(user);
        return user; // Placeholder
    }
}