package unicamp.br.inf321;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

public class UpdateProductRatingStepDefinitions {

    private final CucumberWorld cucumberWorld;

    public UpdateProductRatingStepDefinitions(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    @Given("que o produto com ID {string} existe no sistema")
    public void queOProdutoComIDExiste(String productId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .get("/api/v1/products/" + productId));

        cucumberWorld.getResponse().then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Given("que o produto com ID {string} possui um review com ID {string}")
    public void queOProdutoComIDPossuiUmReviewComID(String productId, String reviewId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.setResponse(
                cucumberWorld.getRequest()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .get("/api/v1/products/" + productId + "/reviews"));

        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");
        assertThat("A lista não deve ser vazia", reviews.size() > 0);

        boolean idFound = reviews.stream().anyMatch(review -> review.get("id").equals(Integer.parseInt(reviewId)));
        assertTrue(idFound, "Review com ID " + reviewId + " não encontrado.");
    }

    @Given("que o produto com ID {string} não possui um review com ID {string}")
    public void queOProdutoComIDNaoPossuiUmReviewComID(String productId, String reviewId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.setResponse(
                cucumberWorld.getRequest()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .get("/api/v1/products/" + productId + "/reviews"));

        List<Map<String, Object>> reviews = cucumberWorld.getResponse().getBody().jsonPath().getList("$");
        assertThat("A lista não deve ser vazia", reviews.size() > 0);

        boolean idFound = reviews.stream().anyMatch(review -> review.get("id").equals(Integer.parseInt(reviewId)));
        assertFalse(idFound, "Review com ID " + reviewId + " encontrado.");
    }

    @When("o usuário envia uma requisição PUT para {string} com os seguintes dados:")
    public void oUsuarioEnviaUmaRequisicaoPUTPara(String url, Map<String, String> table)
            throws JsonProcessingException {
        String token = cucumberWorld.getFromNotes("token");

        if (token == null) {
            cucumberWorld.addToNotes(token, "Invalid token");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("customerId", Integer.parseInt(table.get("customerId")));
        requestBody.put("date", table.get("date"));
        requestBody.put("description", table.get("description"));
        requestBody.put("language", table.get("language"));
        requestBody.put("rating", Integer.parseInt(table.get("rating")));

        System.out.println(token);
        System.out.println(cucumberWorld.getRequest());

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .put(url));

    }

    @Then("a resposta deve conter o status {int} OK")
    public void aRespostaDeveConterOStatusOK(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }
}