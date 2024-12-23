package com.gsalles.carrental;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.exception.ErrorMessage;
import com.gsalles.carrental.jwt.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"/sql/usuario/usuario-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/usuario/usuario-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void authenticateUsuario_withValidCredentials_shouldReturnStatusCode200AndJwtToken(){
        JwtToken response = webTestClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getToken()).isNotNull();

        response = webTestClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getToken()).isNotNull();

        response = webTestClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("lucas123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getToken()).isNotNull();
    }

    @Test
    public void authenticateUsuario_withInvalidCredentials_shouldReturnStatusCode400AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel123", "1234567"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);

        response = webTestClient
                .post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel1234", "123456"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }
}
