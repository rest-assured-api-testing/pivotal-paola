package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Labels;
import entities.Project;
import entities.Stories;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseProjectCreatedTest;

public class LabelTest extends CreateBaseProjectCreatedTest {
    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_FORBIDDEN = 403;
    final int STATUS_NOT_FOUND = 404;

    @Test(groups = {"cleanProject"})
    public void shouldGetAllLabelsFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldValidateLabelSchemaFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", responseLabel.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/label.json");
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotShowLabelsFromProjectsWithWrongId() {
        String fakeId = "987";
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_FORBIDDEN);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotShowLabelsFromProjectsWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldGetASpecificLabelFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", responseLabel.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotGetALabelWithWrongIdFromAProject() {
        String fakeId = "34553";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotGetALabelWithNullIdFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateALabelWithEmptyName() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateALabelWithBlankSpaces() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName("                  ");
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateALabelWithNullName() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldCreateALabelWithCorrectNameButWrongIdProject_IdProjectIsNotSend() throws JsonProcessingException {
        Long wrongProjectId = 9878979L;
        Labels newLabel = new Labels();
        newLabel.setName("Label with correct name");
        newLabel.setProject_id(wrongProjectId);
        apiRequest.setEndpoint(config.setConfig().getProperty("LABEL_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotUpdateACreatedLabelWithWrongProjectId() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setProject_id(1234L);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotUpdateACreatedLabelWithNullProjectId() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setProject_id(345345L);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotUpdateACreatedLabelWithEmptyName() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotUpdateACreatedLabelWithBlankSpaces() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName("                        ");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createLabels", "cleanLabels", "cleanProject"})
    public void shouldNotUpdateACreatedLabelWithNullName() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("labelId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
