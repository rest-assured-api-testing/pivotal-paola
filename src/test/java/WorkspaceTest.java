import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Workspace;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WorkspaceTest {
    ApiRequest apiRequest;
    SettingConfigFile config;
    Workspace defaultWorkspaceData;
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

    @BeforeMethod(onlyForGroups = "createWorkspace")
    public void setDefaultWorkspaceForTests() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("New Workspace for Testing");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        defaultWorkspaceData = ApiManager.executeWithBody(apiRequest).getBody(Workspace.class);
    }

    @AfterMethod(onlyForGroups = "cleanWorkspace")
    public void deleteDefaultWorkspaceForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL")+"{workspaceId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("workspaceId", defaultWorkspaceData.getId().toString());
        ApiManager.execute(apiRequest);
    }

    @Test(groups = "cleanRequest")
    public void shouldGetAllWorkspaces() {
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createWorkspace","cleanWorkspace"})
    public void shouldGetWorkspacesById() {
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL") + "{workspaceId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("workspaceId", defaultWorkspaceData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotCreateWorkspaceWithEmptyName() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotCreateWorkspaceWithNullObject() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotCreateWorkspaceWithOnlyBlankSpaces() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("                              ");
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotCreateWorkspaceWithReallyLongName() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("T h i s  i s  a  v e r y  b u t  v e r y  l a r g e  n a m e  t h a t  s h o u l d  " +
                "n o t  b e  s a v e d  b y  w o r k s p a c e , u s e  s h o r t e r  n a m e");
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotShowWorkspacesByWrongId() {
        String fakeId = "739473";
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = "cleanRequest")
    public void shouldNotShowWorkspacesWithNullId() {
        String fakeId = null;
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
