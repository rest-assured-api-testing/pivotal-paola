package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Workspace;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseTest;

public class WorkspaceTest extends CreateBaseTest {

    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_NOT_FOUND = 404;

    @Test
    public void shouldShowAllWorkspaces() {
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test
    public void shouldNotCreateWorkspaceWithEmptyName() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateWorkspaceWithNullObject() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateWorkspaceWithOnlyBlankSpaces() throws JsonProcessingException {
        Workspace defaultWorkspace = new Workspace();
        defaultWorkspace.setName("                              ");
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultWorkspace));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test
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

    @Test
    public void shouldNotShowWorkspaceWithWrongId() {
        String fakeId = "739473";
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test
    public void shouldNotShowWorkspacesWithNullId() {
        String fakeId = null;
        apiRequest.setEndpoint(config.setConfig().getProperty("WORKSPACES_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
