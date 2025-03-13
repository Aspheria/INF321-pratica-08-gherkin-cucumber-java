package unicamp.br.inf321;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import org.apache.http.HttpStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsultaAvalicaoProdutos {

    private final CucumberWorld cucumberWorld;

    public ConsultaAvalicaoProdutos(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    @Given("que existem avaliações para o produto {string}")
    public void queExistemAvaliacoesParaOProduto(String productId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get("/api/v1/products/" + productId));

        cucumberWorld.getResponse().then()
                .statusCode(HttpStatus.SC_OK);

        // Verificar se existem avaliações para este produto
        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get("/api/v1/products/" + productId + "/reviews"));

        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");
        assertThat("A lista de avaliações não deve ser vazia", reviews.size() > 0);
    }

    @Given("que o usuário não está autenticado")
    public void queOUsuarioNaoEstaAutenticado() {
        // Não é necessário configurar nada, apenas não incluir o token de autenticação na requisição
    }

    @Given("que o usuário está autenticado e possui um token válido")
    public void queOUsuarioEstaAutenticadoEPossuiUmTokenValido() {
        // Verificar se o token existe nas notas
        String token = cucumberWorld.getFromNotes("token");
        assertThat("Token deve existir", token, notNullValue());
    }

    @Given("que o sistema está instável")
    public void queOSistemaEstaInstavel() {
        // Simular sistema instável (este seria um mock em um ambiente real)
        cucumberWorld.addToNotes("systemUnstable", "true");
    }

    @Given("que o produto {string} possui reviews de múltiplos usuários")
    public void queOProdutoPossuiReviewsDeMultiplosUsuarios(String productId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get("/api/v1/products/" + productId + "/reviews"));

        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");
        assertThat("A lista de avaliações não deve ser vazia", reviews.size() > 0);

        // Verificar se existem reviews de mais de um usuário
        Set<Object> uniqueCustomers = reviews.stream()
                .map(review -> review.get("customerId"))
                .collect(Collectors.toSet());

        assertThat("Deve haver avaliações de mais de um usuário", uniqueCustomers.size() > 1);
    }

    @When("o usuário envia uma requisição GET para {string}")
    public void oUsuarioEnviaUmaRequisicaoGETPara(String url) {
        String token = cucumberWorld.getFromNotes("token");
        String systemUnstable = cucumberWorld.getFromNotes("systemUnstable");

        if (systemUnstable != null && systemUnstable.equals("true")) {
            // Simular erro 500 para o cenário de sistema instável
            cucumberWorld.addToNotes("forceError", "500");
        }

        if (token == null) {
            cucumberWorld.setResponse(cucumberWorld.getRequest()
                    .header("Content-Type", "application/json")
                    .get(url));
            return;
        }

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get(url));
    }

    @Then("a resposta deve conter o status {int} OK")
    public void aRespostaDeveConterOStatusOK(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("a resposta deve conter o status {int} Unauthorized")
    public void aRespostaDeveConterOStatusUnauthorized(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("a resposta deve conter o status {int} Not Found")
    public void aRespostaDeveConterOStatusNotFound(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("a resposta deve conter o status {int} Internal Server Error")
    public void aRespostaDeveConterOStatusInternalServerError(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @And("a lista de avaliações deve ser retornada")
    public void aListaDeAvaliacoesDeveSerRetornada() {
        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");
        assertThat("A lista de avaliações não deve ser vazia", reviews.size() > 0);

        // Verificar se os campos essenciais estão presentes
        Map<String, Object> firstReview = reviews.get(0);
        assertThat("Review deve conter ID", firstReview.containsKey("id"));
        assertThat("Review deve conter customerId", firstReview.containsKey("customerId"));
        assertThat("Review deve conter rating", firstReview.containsKey("rating"));
    }

    @And("a mensagem de erro deve indicar {string}")
    public void aMensagemDeErroDeveIndicar(String errorMessage) {
        String responseBody = cucumberWorld.getResponse().getBody().asString();
        assertThat(responseBody, containsString(errorMessage));
    }

    @And("a lista de avaliações deve conter apenas um review por usuário")
    public void aListaDeAvaliacoesDeveConterApenasUmReviewPorUsuario() {
        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");

        // Extrair IDs de clientes
        List<Object> customerIds = reviews.stream()
                .map(review -> review.get("customerId"))
                .collect(Collectors.toList());

        // Verificar se há duplicatas
        Set<Object> uniqueCustomerIds = customerIds.stream().collect(Collectors.toSet());

        // Se o número de elementos únicos for igual ao número total, não há duplicatas
        assertThat("Cada usuário deve ter apenas um review", uniqueCustomerIds.size() == customerIds.size());
    }
}