package unicamp.br.inf321;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;

public class ProductReviewStepDefinitions {

    private final CucumberWorld cucumberWorld;

    public ProductReviewStepDefinitions(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    @Given("logins with her credentials")
    public void sheLoginsWithHerCredentials(Map<String, String> table) {
        String username = table.get("email");
        String password = table.get("password");
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .body(requestBody.toString())
                .when().post("/api/v1/customer/login"));
    }

    @Given("o produto com ID {string} existe")
    public void oProdutoComIDExiste(String productId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .get("/api/v1/auth/products/" + productId + "/reviews")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @When("o usuário envia um POST para a URL {string} com os seguintes dados:")
    public void oUsuarioEnviaUmPOSTParaAURLComOsSeguintesDados(String url, Map<String, String> table) throws JsonProcessingException {
        String token = cucumberWorld.getFromNotes("token");

        JSONObject requestBody = new JSONObject();
        requestBody.put("customerId", Integer.parseInt(table.get("customerId")));
        requestBody.put("date", table.get("date"));
        requestBody.put("description", table.get("description"));
        requestBody.put("language", table.get("language"));
        requestBody.put("rating", Integer.parseInt(table.get("rating")));

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post(url));
    }

    @Then("o response status code deve ser {int} Created")
    public void oResponseStatusCodeDeveSerCreated(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("o response body deve conter o ID do review criado")
    public void oResponseBodyDeveConterOIDDoReviewCriado() {
        cucumberWorld.getResponse().then()
                .body("id", is(notNullValue()))
                .body("id", is(greaterThan(0)));
    }

    @When("o usuário envia um novo review para o produto com ID {string} com rating {int} e comentário {string}")
    public void oUsuarioEnviaUmNovoReviewParaOProdutoComIDComRatingEComentario(String productId, int rating, String comentario) throws JsonProcessingException {
        String token = cucumberWorld.getFromNotes("token");

        JSONObject requestBody = new JSONObject();
        requestBody.put("customerId", 654); // Exemplo de ID fixo, pode ser parametrizado
        requestBody.put("date", "2025-01-18");
        requestBody.put("description", comentario);
        requestBody.put("language", "en");
        requestBody.put("rating", rating);
        requestBody.put("productId", Integer.parseInt(productId));

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post("/api/v1/auth/products/" + productId + "/reviews"));
    }

    @Then("o response status code deve ser {int} Bad Request")
    public void oResponseStatusCodeDeveSerBadRequest(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("o response body deve conter o erro {string}")
    public void oResponseBodyDeveConterOErro(String errorMessage) {
        cucumberWorld.getResponse().then()
                .body("message", containsString(errorMessage));
    }

    @Given("o usuário já criou um review para o produto com ID {string}")
    public void oUsuarioJaCriouUmReviewParaOProdutoComID(String productId) {
        String token = cucumberWorld.getFromNotes("token");
        int customerId = 654; // Pode ser parametrizado

        JSONArray reviews = new JSONArray(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .get("/api/v1/products/" + productId + "/reviews")
                .then()
                .statusCode(200)
                .extract()
                .asString());

        boolean alreadyReviewed = IntStream.range(0, reviews.length())
                .mapToObj(reviews::getJSONObject)
                .anyMatch(review -> review.getJSONObject("customer").getInt("id") == customerId);

        if (!alreadyReviewed) {
            throw new AssertionError("Usuário ainda não avaliou o produto, mas deveria ter avaliado.");
        }
    }

    @When("o usuário tenta criar um novo review para o mesmo produto")
    public void oUsuarioTentaCriarUmNovoReviewParaOMesmoProduto() throws JsonProcessingException {
        String token = cucumberWorld.getFromNotes("token");
        String productId = "4";
        JSONObject requestBody = new JSONObject();
        requestBody.put("customerId", 654);
        requestBody.put("date", "2025-01-18");
        requestBody.put("description", "Segundo review");
        requestBody.put("language", "en");
        requestBody.put("rating", 4);
        requestBody.put("productId", Integer.parseInt(productId));

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post("/api/v1/auth/products/" + productId + "/reviews"));
    }


}
