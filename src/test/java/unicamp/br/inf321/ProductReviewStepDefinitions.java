package unicamp.br.inf321;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.Map;

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

}
