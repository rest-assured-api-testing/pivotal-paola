
import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Project;
import org.testng.Assert;
import org.testng.annotations.*;

public class ProjectTest {

    ApiRequest apiRequest;
    SettingConfigFile config;
    Project defaultProjectData;
    Project projectCreated;
    final int STATUS_OK = 200;
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
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        projectCreated = response.getBody(Project.class);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"cleanRequest"})
    public void shouldDeleteAProjectById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}");
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
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldUpdateProjectById() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldVerifySchemaForProjectById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        response.validateBodySchema("schemas/project.json");
    }

    @Test(groups = "cleanRequest")
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

    @Test(groups = "cleanRequest")
    public void shouldNotCreateProjectWithNullData() throws JsonProcessingException {
        Project noNameProject = new Project();
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(noNameProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = "cleanRequest")
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

    @Test(groups = "cleanRequest")
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

    /*@Test(groups = "cleanRequest")
    public void shouldAssignNameOfProjectWithoutInitialBlankSpace() throws JsonProcessingException {
        Project blankName = new Project();
        blankName.setName("                          Real Project Name");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(blankName));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Project projectName = response.getBody(Project.class);
        Assert.assertEquals(projectName.getName(), "Real Project Name");
    }*/

    @Test(groups = {"createProject","cleanProject"})
    public void shouldNotUpdateProjectWithNullData() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldNotUpdateProjectWithEmptyData() throws JsonProcessingException {
        Project newProject = new Project();
        newProject.setName("Update Project Created from API-CONN");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", "");
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newProject));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldNotShowProjectWithNullId() {
        String emptyId = null;
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + emptyId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createProject","cleanProject"})
    public void shouldNotShowProjectWithWrongId() {
        String fakeId = "74638503";
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

}