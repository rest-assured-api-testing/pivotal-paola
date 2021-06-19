package rest.pivotal.org.steps;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import config.SettingConfigFile;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.testng.Assert;

public class AccountSteps {
    private ApiRequest apiRequest = new ApiRequest();
    private SettingConfigFile config = new SettingConfigFile();
    private ApiResponse response;

    @Before
    public void setAuthForCucumber() {
        config = new SettingConfigFile();
        apiRequest = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
    }

    @Given("I build the {string} request")
    public void iExecutedRequestWithId(String method) {
        apiRequest.setMethod(ApiMethod.valueOf(method));
    }

    @When("I executed {string} request with id")
    public void iExecuteRequest(String endpoint) {
        apiRequest.setEndpoint(endpoint);
        apiRequest.addPathParam("accountId", config.setConfig().getProperty("ID_ACCOUNT"));
        response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
    }

    @Then("The response status code should be {string} with server")
    public void theResponseStatusShouldBe(String statusCode) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }
}
