import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Project;
import org.junit.jupiter.api.BeforeEach;
import org.testng.Assert;
import org.testng.annotations.*;

public class ProjectTest {

    ApiRequest apiRequest;
    SettingConfigFile config;
    Project defaultProjectData;
    Project projectCreated;
    final int STATUS_OK = 200;
    final int STATUS_CREATED = 201;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_NOT_FOUND = 404;

    @BeforeMethod(groups = "cleanRequest")
    public void setAuthForTests() {
        config = new SettingConfigFile();
        apiRequest  = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
    }

    @BeforeMethod(onlyForGroups = "createProject")
    public void setDefaultProjectForTests() throws JsonProcessingException {
        Project defaultProject = new Project();
        defaultProject.setName("Default Project Test");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultProject));
        defaultProjectData = ApiManager.executeWithBody(apiRequest).getBody(Project.class);
    }

    @AfterMethod(onlyForGroups = "cleanProject")
    public void deleteDefaultProjectForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiManager.execute(apiRequest);
    }

    @Test(groups = {"cleanRequest"})
    public void shouldCreateANewProjectOnlyWithProjectName() throws JsonProcessingException {
        projectCreated = new Project();
        Project newProject = new Project();
        newProject.setName("Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse apiResponse = ApiManager.executeWithBody(apiRequest);
        projectCreated = apiResponse.getBody(Project.class);
        Assert.assertEquals(apiResponse.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"cleanRequest"})
    public void shouldDeleteAProjectById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", projectCreated.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NO_CONTENT);
    }

    @Test(groups = {"cleanRequest"})
    public void shouldGetAllProjects() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldGetProjectById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldVerifySchemaForProjectById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        response.validateBodySchema("schemas/project.json");
    }
}


/*
@BeforeMethod(onlyForGroups = "createdProject")
    public void setCreatedProjectConfig() throws JsonProcessingException {
        Project projectTemp = new Project();
        projectTemp.setName("Project to test");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", configFile.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(configFile.setConfig().getProperty("BASE_URI"));
        apiRequest.setEndpoint("projects");
        apiRequest.setMethod(ApiMethod.valueOf("POST"));
        apiRequest.setBody(new ObjectMapper().writeValueAsString(projectTemp));
        project = ApiManager.executeWithBody(apiRequest).getBody(Project.class);
    }

    @AfterMethod(onlyForGroups = "deleteCreatedProject")
    public void deleteCreatedProjectConfig() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", String.valueOf(project.getId()));
        ApiManager.execute(apiRequest);
    }


    @Test(groups = {"createdProject","project","deleteCreatedProject"})
    public void itShouldGetAProject() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", String.valueOf(project.getId()));
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusOk);
    }

    @Test(groups = "project")
    public void itShouldNotGetAProjectWithInvalidId() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", "25045054564131");
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusNotFound);
    }

    @Test(groups = "project")
    public void itShouldGetAllProjects() {
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.setEndpoint("/projects");
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusOk);
    }

    @Test(groups = {"createdProject","project"})
    public void itShouldDeleteAProject() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", String.valueOf(project.getId()));
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusNoContent);
    }

    @Test(groups = "project")
    public void itShouldNotDeleteAProjectWithInvalidId() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", "250508554112");
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusNotFound);
    }

    @Test(groups = "project")
    public void itShouldNotDeleteAProjectWithoutId() {
        apiRequest.setEndpoint("/projects/{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", "");
        ApiResponse apiResponse = ApiManager.execute(apiRequest);
        Assert.assertEquals(apiResponse.getStatusCode(), statusNotFound);
    }

    @Test(groups = {"project","deleteCreatedProject"})
    public void itShouldCreateAProject() throws JsonProcessingException {
        Project projectTemp = new Project();
        projectTemp.setName("Project created");
        apiRequest.setEndpoint("projects");
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(projectTemp));
        ApiResponse apiResponse = ApiManager.executeWithBody(apiRequest);
        apiResponse.getResponse().then().log().body();
        project = apiResponse.getBody(Project.class);
        Assert.assertEquals(apiResponse.getStatusCode(), statusOk);
    }

    @Test(groups = "project")
    public void itShouldNotCreateAProjectWithEmptyName() throws JsonProcessingException {
        Project projectTemp = new Project();
        projectTemp.setName("");
        apiRequest.setEndpoint("projects");
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(projectTemp));
        ApiResponse apiResponse = ApiManager.executeWithBody(apiRequest);
        apiResponse.getResponse().then().log().body();
        Assert.assertEquals(apiResponse.getStatusCode(), statusBadRequest);
    }
 */