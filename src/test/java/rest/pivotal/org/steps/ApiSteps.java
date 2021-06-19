package rest.pivotal.org.steps;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Project;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.testng.Assert;

public class ApiSteps {
    private ApiRequest apiRequest = new ApiRequest();
    private SettingConfigFile config = new SettingConfigFile();
    private ApiResponse response;
    private Project project;

    @Before
    public void createProject() throws JsonProcessingException {
        Project projectCreated = new Project();
        projectCreated.setName("Project for cucumber");
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(projectCreated));
        project = ApiManager.executeWithBody(apiRequest).getBody(Project.class);
    }

    @After
    public void deleteCreatedProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}");
        apiRequest.addPathParam("projectId", project.getId().toString());
        apiRequest.setMethod(ApiMethod.DELETE);
        ApiManager.execute(apiRequest);
    }

    @Given("I build {string} request")
    public void iBuildRequest(String method) {
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
        apiRequest.setMethod(ApiMethod.valueOf(method));
    }

    @When("I executed {string} request")
    public void iExecuteRequest(String endpoint) {
        apiRequest.setEndpoint(endpoint);
        apiRequest.addPathParam("projectId", project.getId().toString());
        response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
    }

    @Then("The response status code should be {string}")
    public void theResponseStatusCodeShouldBe(String statusCode) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
    }
}
