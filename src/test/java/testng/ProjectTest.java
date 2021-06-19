package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Project;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseTest;

public class ProjectTest extends CreateBaseTest {

    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_FORBIDDEN = 403;
    final int STATUS_NOT_FOUND = 404;

    @Test
    public void shouldGetAllProjects() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldGetProjectWithValidId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldUpdateProjectWithValidId() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldVerifySchemaForProjectWithValidId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        response.validateBodySchema("schemas/project.json");
    }

    @Test
    public void shouldNotCreateProjectWithEmptyName() throws JsonProcessingException {
        Project noNameProject = new Project();
        noNameProject.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(noNameProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateProjectWithNullData() throws JsonProcessingException {
        Project noNameProject = new Project();
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(noNameProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateProjectWithVeryLargeName() throws JsonProcessingException {
        Project bigNameProject = new Project();
        bigNameProject.setName("T h i s  i s  a  v e r y  l o n g  ,  b u t  r e a l l y  l o n g  l o n g" +
                "   l o o o o o o o o o o o o o n g  n a m e ");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(bigNameProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateProjectWithOnlyBlankSpaces() throws JsonProcessingException {
        Project blankName = new Project();
        blankName.setName("            ");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(blankName));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldUpdateNameOfProjectWithoutInitialBlankSpace() throws JsonProcessingException {
        Project blankName = new Project();
        blankName.setName("                          Real Project Name");
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(blankName));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Project projectName = response.getBody(Project.class);
        Assert.assertEquals(projectName.getName(), "Real Project Name");
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotShowProjectWithNullId() {
        String emptyId = null;
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + emptyId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotShowProjectWithWrongId() {
        String fakeId = "74638503";
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotShowProjectWithIdOfPrivateProject() {
        String privateProject = "79879";
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + privateProject);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_FORBIDDEN);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotUpdateProjectWithNullId() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotUpdateProjectWithEmptyId() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", "");
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotUpdateProjectWithEmptyName() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createProject", "cleanProject"})
    public void shouldNotUpdateProjectWithNullName() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }
}