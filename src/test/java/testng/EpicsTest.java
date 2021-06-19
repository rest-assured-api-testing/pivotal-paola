package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Epics;
import entities.Project;
import entities.Stories;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseProjectCreatedTest;

public class EpicsTest extends CreateBaseProjectCreatedTest {
    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_FORBIDDEN = 403;
    final int STATUS_NOT_FOUND = 404;

    @Test(groups = {"cleanProject"})
    public void shouldGetAllEpicsFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldValidateEpicsSchemaFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", responseEpic.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/epics.json");
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotShowEpicsFromProjectsWithWrongId() {
        String fakeId = "987";
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_FORBIDDEN);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotShowEpicsFromProjectsWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldGetASpecificEpicFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", responseEpic.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotGetAEpicsWithWrongIdFromASpecificProject() {
        String fakeId = "34553";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotGetAEpicWithNullIdFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAEpicOfASpecificProjectWithEmptyName() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAEpicOfASpecificProjectWithBlankSpaces() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName("                  ");
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAEpicOfASpecificProjectWithNullName() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotUpdateACreatedEpicWithEmptyName() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotUpdateACreatedEpicWithBlankSpaces() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName("               ");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotUpdateACreatedEpicWithNullName() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotUpdateACreatedEpicWithWrongProjectId() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setProject_id(1234L);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createEpics", "cleanEpics", "cleanProject"})
    public void shouldNotUpdateACreatedEpicWithNullProjectId() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setProject_id(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("epicId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
