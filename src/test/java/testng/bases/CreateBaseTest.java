package testng.bases;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Membership;
import entities.Project;
import entities.Workspace;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class CreateBaseTest {
    protected ApiRequest apiRequest;
    protected ApiResponse response = new ApiResponse();
    protected ApiResponse responseWorkspace = new ApiResponse();
    protected SettingConfigFile config;
    protected ApiResponse responseMembership = new ApiResponse();
    protected Membership memberData;

    @BeforeMethod
    public void setAuthForTests() {
        config = new SettingConfigFile();
        apiRequest  = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
    }

    @BeforeMethod(dependsOnMethods = "setAuthForTests", onlyForGroups = "createProject")
    public void setDefaultProjectForTests() throws JsonProcessingException {
        Project defaultProject = new Project();
        defaultProject.setName("Default Project Test");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultProject));
        response = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanProject")
    public void deleteDefaultProjectForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }

    @BeforeMethod(dependsOnMethods = "setAuthForTests", onlyForGroups = "createWorkspace")
    public void setDefaultWorkspaceForTests() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("New Workspace for Testing");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        response = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanWorkspace")
    public void deleteDefaultWorkspaceForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("workspaceId", response.getBody(Workspace.class).getId().toString());
        ApiManager.execute(apiRequest);
    }
}
