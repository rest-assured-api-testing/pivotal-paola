package rest.pivotal.org.steps;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Workspace;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public class CreateWorkspace {

    protected ApiRequest apiRequest;
    protected ApiResponse response;
    protected SettingConfigFile config;

    @Before
    public void setAuthForTests() {
        config = new SettingConfigFile();
        apiRequest  = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
    }

    @Given("I build a {string} request")
    public void iExecutedPathRequest(String method) {
        apiRequest.setMethod(ApiMethod.valueOf(method));
    }

    @When("I executed path {string} with {string}")
    public void iExecutedRequest(String endpoint, String workspaceName) throws JsonProcessingException {
        Workspace work = new Workspace();
        work.setName(workspaceName);
        apiRequest.setEndpoint(endpoint);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(work));
        response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
    }

    @Then("the response status code should be {string}")
    public void theResponseStatusCodeShouldBeOK(String statusCode) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }
}