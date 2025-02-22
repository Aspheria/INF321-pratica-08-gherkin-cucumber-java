package unicamp.br.inf321;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class CustomerProfileStepDefinitions {

    // CucumberWorld is used to share state between steps
    private final CucumberWorld cucumberWorld;

    public CustomerProfileStepDefinitions(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    @When("he selects the option to see his profile")
    public void heSelectsTheOptionToSeeHisProfile() {
        String token = cucumberWorld.getFromNotes("token");
        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .get("/api/v1/auth/customers/profile"));
    }

    @Then("his profile should be shown with success")
    public void hisProfileShouldBeShownWithSuccess() {
        String email = cucumberWorld.getFromNotes("email");
        String customerId = cucumberWorld.getResponse().then().log().all().assertThat()
                // validates status code
                .statusCode(HttpStatus.SC_OK)
                // validates JSON schema (JSON schema generated using https://www.jsonschema.net/app/schemas/377249)
                .body(matchesJsonSchemaInClasspath("unicamp/br/inf321/CustomerProfileJsonSchema.json"))
                // assert response body contents
                .onFailMessage("id should be a valid number")
                .body("id", is(greaterThan(0)))
                .body("emailAddress", is(equalTo(email)))
                .extract().jsonPath().getString("id");
        cucumberWorld.addToNotes("customerId", customerId);
    }
}
