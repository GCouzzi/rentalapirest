package com.gsalles.carrental;

import com.gsalles.carrental.dto.AluguelDTO;
import com.gsalles.carrental.dto.rdto.AluguelResponseDTO;
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
@Sql(scripts = {"/sql/automovel/automovel-insert.sql", "/sql/usuario/usuario-insert.sql", "/sql/aluguel/aluguel-insert.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/aluguel/aluguel-delete.sql", "/sql/automovel/automovel-delete.sql", "/sql/usuario/usuario-delete.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AluguelIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void checkIn_withValidAluguelDto_shouldReturnStatusCode201AndAluguelResponseDto(){
        AluguelResponseDTO response = webTestClient
                .post()
                .uri("api/v1/alugueis/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AluguelDTO("gabriel123", "XXX-0000"))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getAutomovelRecibo()).isNotNull();
        Assertions.assertThat(response.getAutomovelCor()).isEqualTo("Cinza");
        Assertions.assertThat(response.getAutomovelMarca()).isEqualTo("Chevrolet");
        Assertions.assertThat(response.getAutomovelPlaca()).isEqualTo("XXX-0000");
        Assertions.assertThat(response.getAutomovelModelo()).isEqualTo("Tracker");
        Assertions.assertThat(response.getUsuarioUsername()).isEqualTo("gabriel123");
        Assertions.assertThat(response.getDataFim()).isNull();
        Assertions.assertThat(response.getValor()).isNull();
    }

    @Test
    public void checkIn_withoutAdminPermission_shouldReturnStatusCode403AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/alugueis/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .bodyValue(new AluguelDTO("pedro123", "XXX-0000"))
                .exchange()
                .expectStatus()
                .isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    public void checkIn_withInvalidAutomovelPlaca_shouldReturnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/alugueis/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AluguelDTO("gabriel123", "XXC-0000"))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void checkIn_withInvalidUsuarioUsername_shouldReturnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/alugueis/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AluguelDTO("gabriel1234", "XXX-0000"))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void checkIn_withAutomovelAlugado_shouldReturnStatusCode409AndErrorMessage(){
        ErrorMessage response = webTestClient
                .post()
                .uri("api/v1/alugueis/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .bodyValue(new AluguelDTO("gabriel123", "XXX-0002"))
                .exchange()
                .expectStatus()
                .isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    public void findByRecibo_withValidRecibo_shouldReturnStatusCode200(){
        AluguelResponseDTO response = webTestClient
                .get()
                .uri("api/v1/alugueis/{recibo}", "20240327123951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBody(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getAutomovelMarca()).isEqualTo("Chevrolet");
        Assertions.assertThat(response.getAutomovelPlaca()).isEqualTo("ZZZ-0000");
        Assertions.assertThat(response.getAutomovelModelo()).isEqualTo("Tracker");
        Assertions.assertThat(response.getAutomovelRecibo()).isEqualTo("20240327123951");
        Assertions.assertThat(response.getUsuarioUsername()).isEqualTo("gabriel123");
        Assertions.assertThat(response.getAutomovelCor()).isEqualTo("Cinza");
    }

    @Test
    public void findByRecibo_withoutAdminPermission_shouldReturnStatusCode403(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/{recibo}", "20240327123951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
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
    public void findByRecibo_withInvalidRecibo_shouldReturnStatusCode404(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/{recibo}", "20240327123952")
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
    public void checkOut_withValidRecibo_shouldReturnStatusCode200AndAluguelResponseDto(){
        AluguelResponseDTO response = webTestClient
                .get()
                .uri("api/v1/alugueis/checkout/{recibo}", "20240325143951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response.getAutomovelRecibo()).isNotNull();
        Assertions.assertThat(response.getAutomovelCor()).isEqualTo("Preto");
        Assertions.assertThat(response.getAutomovelMarca()).isEqualTo("Honda");
        Assertions.assertThat(response.getAutomovelPlaca()).isEqualTo("ZZZ-0002");
        Assertions.assertThat(response.getAutomovelModelo()).isEqualTo("Accord");
        Assertions.assertThat(response.getUsuarioUsername()).isEqualTo("lucas123");
        Assertions.assertThat(response.getValor()).isNotNull();
        Assertions.assertThat(response.getDataFim()).isNotNull();
    }

    @Test
    public void checkOut_withInvalidRecibo_shouldReturnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/checkout/{recibo}", "20240327123952")
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
    public void checkOut_withoutAdminPermission_shouldReturnStatusCode403AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/checkout/{recibo}", "20240327123951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lucas123", "123456"))
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
    public void checkOut_withCheckOuttedRecibo_shouldReturnStatusCode409AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/checkout/{recibo}", "20240327123951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);

        response = webTestClient
                .get()
                .uri("api/v1/alugueis/checkout/{recibo}", "20240326133951")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
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
    public void findAllAlugueis_withAnyUsuarioPermission_shouldReturnStatusCode200AndAluguelResponseDTOList(){
        List<AluguelResponseDTO> response = webTestClient
                .get()
                .uri("api/v1/alugueis")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);

        response = webTestClient
                .get()
                .uri("api/v1/alugueis")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lucas123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);

        response = webTestClient
                .get()
                .uri("api/v1/alugueis")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "pedro123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);
    }

    @Test
    public void findAllAlugueisByUsername_withAdminPermissionAndValidUsuario_shouldReturnStatusCode200AndAluguelResponseDTOList(){
        List<AluguelResponseDTO> response = webTestClient
                .get()
                .uri("api/v1/alugueis/username/{username}", "gabriel123")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);

        response = webTestClient
                .get()
                .uri("api/v1/alugueis/username/{username}", "lucas123")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);

        response = webTestClient
                .get()
                .uri("api/v1/alugueis/username/{username}", "pedro123")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "gabriel123", "123456"))
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBodyList(AluguelResponseDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(1);
    }

    @Test
    public void findAllAlugueisByUsername_withClientePermission_shouldReturnStatusCode403AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/username/{username}", "gabriel123")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "lucas123", "123456"))
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
    public void findAllAlugueisByUsername_withNoExistentUsuario_shouldReturnStatusCode404AndErrorMessage(){
        ErrorMessage response = webTestClient
                .get()
                .uri("api/v1/alugueis/username/{username}", "biel123")
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
}
