package unicamp.br.inf321;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginStepDefinitions {
    private final CucumberWorld cucumberWorld;

    public LoginStepDefinitions(CucumberWorld cucumberWorld) {
        this.cucumberWorld = cucumberWorld;
    }

    boolean isJWT(String jwt) {
        if (jwt == null || jwt.isEmpty())
            return false;

        String[] jwtSplitted = jwt.split("\\.");
        if (jwtSplitted.length != 3) // The JWT is composed of three parts
            return false;
        try {
            String jsonFirstPart = new String(Base64.getDecoder().decode(jwtSplitted[0]));
            JSONObject firstPart = new JSONObject(jsonFirstPart); // The first part of the JWT is a JSON
            if (!firstPart.has("alg")) // The first part has the attribute "alg"
                return false;
            String jsonSecondPart = new String(Base64.getDecoder().decode(jwtSplitted[1]));
            JSONObject secondPart = new JSONObject(jsonSecondPart); // The first part of the JWT is a JSON
            // Put the validations you think are necessary for the data the JWT should take
            // to validate
        } catch (JSONException err) {
            return false;
        }
        return true;
    }

    @Given("^(?:[A-Za-z]+) (?:is registered on the multibags website|opens the multibags application)$")
    public void isRegisteredOnTheMultibagsWebsite() {
        this.cucumberWorld.setRequest(given().log().all().baseUri("http://multibags.1dt.com.br")
                .contentType(ContentType.JSON.toString())
                .accept(ContentType.JSON.toString()));
    }

    @When("she logins with her credentials")
    public void loginsWithValidCredentials(Map<String, String> table) {
        String username = table.get("email");
        String password = table.get("password");
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        cucumberWorld.setResponse(cucumberWorld.getRequest()
                .body(requestBody.toString())
                .when().post("/api/v1/customer/login"));
    }

    @Then("she should be logged with success")
    public void shouldBeLoggedWithSuccess() {
        String token = cucumberWorld.getResponse().then().log().all().assertThat()
                // validates status code
                .statusCode(HttpStatus.SC_OK)
                // validates JSON schema (JSON schema generated using
                // https://www.jsonschema.net/app/schemas/377249)
                .body(matchesJsonSchemaInClasspath("unicamp/br/inf321/LoginJsonSchema.json"))
                // assert response body contents
                .onFailMessage("token should not be empty")
                .body("token", not(blankOrNullString()))
                .extract().body().jsonPath().get("token");
        assertThat("should be a jwt token", isJWT(token), is(true));
        cucumberWorld.addToNotes("token", token);
    }

    @Given("^(?:[A-Za-z]+) is logged on the multibags application$")
    public void jhonIsLoggedOnTheMultibagsApplication(Map<String, String> table) {
        this.cucumberWorld.addToNotes("email", table.get("email"));
        this.cucumberWorld.addToNotes("password", table.get("password"));
        // reusing already existent steps to avoid code duplication
        isRegisteredOnTheMultibagsWebsite();
        loginsWithValidCredentials(table);
        shouldBeLoggedWithSuccess();
    }

    @Given("there is no logged user on the multibags application")
    public void thereIsNoLoggedUser() {
        String token = cucumberWorld.getFromNotes("token");
        assertFalse(isJWT(token), "should be no jwt token");
    }
}
