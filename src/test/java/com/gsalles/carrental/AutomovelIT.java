package com.gsalles.carrental;

import com.gsalles.carrental.dto.AutomovelDTO;
import com.gsalles.carrental.dto.rdto.AutomovelResponseDTO;
import com.gsalles.carrental.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"/sql/automovel/automovel-insert.sql", "/sql/usuario/usuario-insert.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/automovel/automovel-delete.sql", "/sql/usuario/usuario-delete.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutomovelIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createAutomovel_withValidAutomovelDataAndUsuarioRoleAdmin_returnStatusCode201AndAutomovelResponseDto(){
        AutomovelResponseDTO response = webTestClient
                .post()
                .uri("api/v1/automoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AutomovelDTO("Chevrolet", "Cruze", "Preto", "CCC-1111", BigDecimal.valueOf(0.4)))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getPlaca()).isEqualTo("CCC-1111");
        Assertions.assertThat(response.getMarca()).isEqualTo("Chevrolet");
        Assertions.assertThat(response.getModelo()).isEqualTo("Cruze");
        Assertions.assertThat(response.getCor()).isEqualTo("Preto");
        Assertions.assertThat(response.getValorPorMinuto()).isEqualTo(BigDecimal.valueOf(0.4));
        Assertions.assertThat(response.getStatus()).isEqualTo("LIVRE");
    }

    @Test
    public void createAutomovel_withValidAutomovelDataAndUsuarioRoleCliente_returnStatusCode403AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/automoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .bodyValue(new AutomovelDTO("Chevrolet", "Cruze", "Preto", "CCC-1111", BigDecimal.valueOf(0.4)))
                .exchange()
                .expectStatus()
                .isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void createAutomovel_withNegativeOrZeroAutomovelValorPorMinuto_returnStatusCode422AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/automoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AutomovelDTO("Chevrolet", "Cruze", "Preto", "CCC-1111", BigDecimal.valueOf(-0.1)))
                .exchange()
                .expectStatus()
                .isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .post()
                .uri("api/v1/automoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AutomovelDTO("Chevrolet", "Cruze", "Preto", "CCC-1111", BigDecimal.valueOf(0)))
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
    public void createAutomovel_withExistentAutomovelPlaca_returnStatusCode409AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/automoveis")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AutomovelDTO("Chevrolet", "Cruze", "Preto", "XXX-0000", BigDecimal.valueOf(0.4)))
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
    public void findAutomovelById_withExistentAutomovelIdRoleAdminAndCliente_returnStatusCode200AndAutomovelResponseDto(){
        AutomovelResponseDTO response = webTestClient
                .get()
                .uri("api/v1/automoveis/10")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getPlaca()).isEqualTo("XXX-0000");
        Assertions.assertThat(response.getMarca()).isEqualTo("Chevrolet");
        Assertions.assertThat(response.getModelo()).isEqualTo("Tracker");
        Assertions.assertThat(response.getCor()).isEqualTo("Cinza");
        Assertions.assertThat(response.getStatus()).isEqualTo("LIVRE");

        response = webTestClient
                .get()
                .uri("api/v1/automoveis/30")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lucas123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getPlaca()).isEqualTo("XXX-0002");
        Assertions.assertThat(response.getMarca()).isEqualTo("Honda");
        Assertions.assertThat(response.getModelo()).isEqualTo("Accord");
        Assertions.assertThat(response.getCor()).isEqualTo("Preto");
        Assertions.assertThat(response.getStatus()).isEqualTo("ALUGADO");
    }

    @Test
    public void findAutomovelById_withInexistentAutomovelIdRoleAdminAndCliente_returnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/automoveis/11")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        response = webTestClient
                .get()
                .uri("api/v1/automoveis/11")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
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
    public void findAutomovelByPlaca_withExistentAutomovelPlacaRoleAdminAndCliente_returnStatusCode200AndAutomovelResponseDto(){
        AutomovelResponseDTO response = webTestClient
                .get()
                .uri("api/v1/automoveis/placa/XXX-0000")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getPlaca()).isEqualTo("XXX-0000");
        Assertions.assertThat(response.getMarca()).isEqualTo("Chevrolet");
        Assertions.assertThat(response.getModelo()).isEqualTo("Tracker");
        Assertions.assertThat(response.getCor()).isEqualTo("Cinza");
        Assertions.assertThat(response.getStatus()).isEqualTo("LIVRE");

        response = webTestClient
                .get()
                .uri("api/v1/automoveis/placa/XXX-0002")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lucas123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getPlaca()).isEqualTo("XXX-0002");
        Assertions.assertThat(response.getMarca()).isEqualTo("Honda");
        Assertions.assertThat(response.getModelo()).isEqualTo("Accord");
        Assertions.assertThat(response.getCor()).isEqualTo("Preto");
        Assertions.assertThat(response.getStatus()).isEqualTo("ALUGADO");
    }

    @Test
    public void findAutomovelByPlaca_withInexistentAutomovelPlacaRoleAdminAndCliente_returnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/automoveis/placa/CCC-0000")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        response = webTestClient
                .get()
                .uri("api/v1/automoveis/placa/CCC-0001")
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
    public void findAllAutomoveis_withRoleAdminAndCliente_returnStatusCode404AndListOfAutomovelResponseDto(){
        List<AutomovelResponseDTO> response = webTestClient
                .get()
                .uri("api/v1/automoveis")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();

        response = webTestClient
                .get()
                .uri("api/v1/automoveis")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AutomovelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
    }
}
