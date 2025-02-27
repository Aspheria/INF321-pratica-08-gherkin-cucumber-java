package unicamp.br.inf321;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

import static org.hamcrest.Matchers.*;

public class ExclusaoProductReviewStepDefinitions {

    private final CucumberWorld cucumberWorld;

    public ExclusaoProductReviewStepDefinitions(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    @Given("He is logged on the multibags application")
    public void jhonIsLoggedOnTheMultibagsApplication(io.cucumber.datatable.DataTable table) {
        String email = table.cell(1, 0);
        String password = table.cell(1, 1);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", email);
        requestBody.put("password", password);

        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .body(requestBody.toString())
                .when().post("/api/v1/customer/login"));

        String token = cucumberWorld.getResponse().jsonPath().getString("token");
        cucumberWorld.addToNotes("token", token);
    }

    @Given("que o usuário está autenticado e possui um token válido")
    public void usuarioEstaAutenticado() {
        String token = cucumberWorld.getFromNotes("token");
        assert token != null : "O token de autenticação não foi gerado.";
    }

    @Given("que o usuário já criou um review para o produto {string} com reviewId {string}")
    public void usuarioJaCriouUmReviewParaOProduto(String productId, String reviewId) {
        String token = cucumberWorld.getFromNotes("token");

        cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .get("/api/v1/auth/products/" + productId + "/reviews/" + reviewId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @When("o usuário envia uma requisição DELETE para \"{string}\"")
    public void usuarioEnviaRequisicaoDelete(String url) {
        String token = cucumberWorld.getFromNotes("token");
        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .delete(url));
    }

    @Then("a resposta deve conter o status {int} No Content")
    public void respostaDeveConterStatusNoContent(int statusCode) {
        cucumberWorld.getResponse().then()
                .statusCode(statusCode);
    }

    @Then("o review deve ser removido do sistema")
    public void reviewDeveSerRemovidoDoSistema() {
        String token = cucumberWorld.getFromNotes("token");
        cucumberWorld.getRequest()
                .header("Authorization", "Bearer " + token)
                .get("/api/v1/auth/products/123/reviews/456")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
