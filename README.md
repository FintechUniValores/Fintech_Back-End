# Passo 4: Desenvolvimento do Backend (Spring Boot com Maven)

Nesta etapa, construiremos a **API RESTful** que fornecerá todos os dados e lógica de negócio para o nosso aplicativo React Native.

---

## Objetivos Principais

- Configurar um projeto Spring Boot com Maven.
- Integrar a autenticação com o Gov.br (OAuth 2.0 / OpenID Connect).
- Implementar a verificação de nível de conta e 2FA do Gov.br.
- Integrar com o Firebase Firestore para persistência de dados.
- Criar endpoints para servir os conteúdos dinâmicos (_Guia de Resgate_, _FAQs_, _Produtos do Banco Parceiro_).

---

## Etapa 4.1: Configuração Inicial do Projeto Spring Boot

### Gerar o Projeto com Spring Initializr

- Acesse: [https://start.spring.io/](https://start.spring.io/)
- **Configurações:**
  - **Project:** Maven Project
  - **Language:** Java
  - **Spring Boot:** Versão estável mais recente (ex: 3.x.x)
  - **Group:** `br.com.fintech.valoresareceber` (ou outro de sua preferência)
  - **Artifact:** `backend-valores-a-receber`
  - **Name:** `backend-valores-a-receber`
  - **Package Name:** `br.com.fintech.valoresareceber`
  - **Packaging:** Jar
  - **Java:** Versão LTS mais recente (ex: 17 ou 21)
  - **Dependencies:**
    - Spring Web (APIs RESTful)
    - Spring Security (segurança e OAuth 2.0)
    - OAuth2 Client (OAuth 2.0/OpenID Connect com Gov.br)
    - Spring Data JPA (*Opcional*, se for usar banco relacional)
    - Lombok (*Opcional*, reduz boilerplate)
- Clique em **Generate** para baixar o projeto.

### Importar no Ambiente de Desenvolvimento (IDE)

- Descompacte o arquivo ZIP.
- Importe o projeto na sua IDE favorita (IntelliJ, Eclipse, VS Code) como projeto Maven.

### Estrutura Inicial do Projeto

```
backend-valores-a-receber/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── br/com/fintech/valoresareceber/
    │   │       └── BackendValoresAReceberApplication.java
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       └── templates/
    └── test/
        └── java/
            └── br/com/fintech/valoresareceber/
                └── BackendValoresAReceberApplicationTests.java
```

---

## Etapa 4.2: Configuração do `application.properties` (Básico)

No arquivo `src/main/resources/application.properties`, adicione as configurações básicas:

```properties
# Server Port
server.port=8080

# Application Name (for logging, etc.)
spring.application.name=backend-valores-a-receber

# Logging (ajuste conforme necessário)
logging.level.br.com.fintech.valoresareceber=DEBUG
```

---

## Etapa 4.3: Configuração da Segurança e OAuth2 Client (Gov.br)

O Spring Security e o OAuth2 Client precisam ser configurados para interagir com o Gov.br.

### Configuração de Credenciais Gov.br

No seu `application.properties`, adicione (estes são placeholders):

```properties
# OAuth2 Client Configuration for Gov.br
spring.security.oauth2.client.registration.govbr.client-id=SEU_CLIENT_ID_GOVBR
spring.security.oauth2.client.registration.govbr.client-secret=SEU_CLIENT_SECRET_GOVBR
spring.security.oauth2.client.registration.govbr.client-authentication-method=client_secret_post
spring.security.oauth2.client.registration.govbr.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.govbr.redirect-uri={baseUrl}/login/oauth2/code/govbr
spring.security.oauth2.client.registration.govbr.scope=openid,profile,email,acr
spring.security.oauth2.client.registration.govbr.client-name=Gov.br

# Provider Configuration for Gov.br
spring.security.oauth2.client.provider.govbr.authorization-uri=https://sso.acesso.gov.br/authorize
spring.security.oauth2.client.provider.govbr.token-uri=https://sso.acesso.gov.br/token
spring.security.oauth2.client.provider.govbr.jwk-set-uri=https://sso.acesso.gov.br/jwks
spring.security.oauth2.client.provider.govbr.user-info-uri=https://sso.acesso.gov.br/userinfo
spring.security.oauth2.client.provider.govbr.user-name-attribute=sub
```

> **Observação:** Em ambiente real, obtenha `client-id` e `client-secret` no portal do Gov.br ao registrar sua aplicação.

---

### Criação da Classe de Configuração de Segurança

Crie `SecurityConfig.java` em `br.com.fintech.valoresareceber.config`:

```java
package br.com.fintech.valoresareceber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .authorizationEndpoint(authorization -> authorization
                    .baseUri("/oauth2/authorization")
                )
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/login/oauth2/code/*")
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(new OidcUserService())
                )
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("http://localhost:19006/auth-success");
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("http://localhost:19006/auth-failure?error=" + exception.getMessage());
                })
            );
        return http.build();
    }
}
```

#### Explicação dos pontos chave:

- `csrf.disable()`: Desabilita CSRF para API REST. Analise alternativas para produção.
- `authorizeHttpRequests`: Define quais endpoints são públicos/privados.
- `oauth2Login`: Configura o fluxo OAuth2/OIDC.
- `successHandler` e `failureHandler`: Redirecionam o usuário para o aplicativo React Native após login/falha. Os links são exemplos para Expo em desenvolvimento; configure o `app.json` no React Native para interceptar esses redirecionamentos.

---

## Próximos Passos

- **Implementar Integração com Firebase Firestore**: Adicionar dependências e configurar acesso ao Firebase.
- **Criar Modelos de Dados**: Definir classes Java para Usuário, Item do Guia, FAQ, Produto.
- **Criar Repositórios**: Interfaces para interação com o Firebase.
- **Criar Services**: Camada de lógica de negócio.
- **Criar Controllers**: Endpoints REST para o frontend.

---

> **Sugestão:** Comece configurando o projeto e o `application.properties` com os placeholders. Depois, avance para a integração com o Firebase e implementação das APIs.
