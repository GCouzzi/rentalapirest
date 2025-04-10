package com.gsalles.carrental;

import com.gsalles.carrental.dto.UsuarioDTO;
import com.gsalles.carrental.dto.UsuarioPasswordDTO;
import com.gsalles.carrental.dto.rdto.UsuarioResponseDTO;
import com.gsalles.carrental.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"/sql/usuario/usuario-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/usuario/usuario-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_withValidUsernameAndPassword_ReturnUsuarioWithStatusCode201(){
        UsuarioResponseDTO response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel@gmail", "12345678"))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("gabriel@gmail");
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_withInvalidUsername_ReturnErrorMessageWithStatusCode422(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel", "12345678"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabrielgabrielgabriel", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_withInvalidPassword_ReturnErrorMessageWithStatusCode422(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel1234", "12345"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel1234", "123456789"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_withExistentUsername_ReturnErrorMessageWithStatusCode409(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioDTO("gabriel123", "12345678"))
                .exchange()
                .expectStatus()
                .isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUsuarioById_withExistentUsuarioAdmin_ReturnUsuarioWithStatusCode200(){
        UsuarioResponseDTO response = webTestClient
                .get()
                .uri("api/v1/usuarios/1")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("gabriel123");
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getRole()).isEqualTo("ADMIN");

        response = webTestClient
                .get()
                .uri("api/v1/usuarios/2")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UsuarioResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("pedro123");
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void findUsuarioById_withInexistentUsuario_ReturnErrorMessageAndStatusCode404(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/usuarios/4")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void findUsuarioById_withIdNotAllowedAndRoleCliente_ReturnStatusCode403(){
        webTestClient
                .get()
                .uri("api/v1/usuarios/3")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isForbidden();

        webTestClient
                .get()
                .uri("api/v1/usuarios/1")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    public void findUsuarioById_withoutAuthentication_ReturnStatusCode401(){
        webTestClient
                .get()
                .uri("api/v1/usuarios/3")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void updatePasswordUsuario_withPasswordsMatching_ReturnStatusCode204(){
        webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "1234567", "1234567"))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void updatePasswordUsuario_withPasswordsNotMatching_ReturnStatusCode400(){
        ErrorMessage response = webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("1234567", "12345678", "12345678"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);

        response = webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "12345678", "1234567"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void updatePasswordUsuario_withNewPasswordEqualsCurrentPassword_ReturnStatusCode400(){
        ErrorMessage response = webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "123456", "123456"))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void updatePasswordUsuario_withDifferentUsuarioId_ReturnStatusCode403(){
        webTestClient
                .put()
                .uri("api/v1/usuarios/2")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "1234567", "1234567"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    public void updatePasswordUsuario_withNotValidPasswords_ReturnStatusCode422(){
        ErrorMessage response = webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "123456789", "123456789"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .put()
                .uri("api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new UsuarioPasswordDTO("123456", "12345", "12345"))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void findAllUsuario_withAdminRole_ReturnListUsuarioAndStatusCode200(){
        List<UsuarioResponseDTO> response = webTestClient
                .get()
                .uri("api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UsuarioResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isNotNull();
    }

    @Test
    public void findAllUsuario_withClienteRole_ReturnStatusCode403() {
        webTestClient
                .get()
                .uri("api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}