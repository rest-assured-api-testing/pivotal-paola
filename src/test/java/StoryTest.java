import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Project;
import entities.Story;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StoryTest {
    ApiRequest apiRequest;
    SettingConfigFile config;
    Story defaultStoryData;
    Project defaultProjectData;
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

    @BeforeMethod(onlyForGroups = "createProjectForStories")
    public void setDefaultWorkspaceForTests() throws JsonProcessingException {
        Project defaultProject = new Project();
        defaultProject.setName("Default Project For Stories");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(defaultProject));
        defaultProjectData = ApiManager.executeWithBody(apiRequest).getBody(Project.class);
    }

    @AfterMethod(onlyForGroups = "cleanProjectForStories")
    public void deleteDefaultWorkspaceForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL")+"{projectId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiManager.execute(apiRequest);
    }

    @Test(groups = {"createProjectForStories", "cleanProjectForStories"})
    public void shouldGetAllStoriesFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}/"
                + config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProjectForStories", "cleanProjectForStories"})
    public void shouldCreateAStoryOfASpecificProject() throws JsonProcessingException {
        Story newStory = new Story();
        newStory.setName("Story created");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}/"
                + config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createProjectForStories", "cleanProjectForStories"})
    public void shouldNotCreateAStoryOfASpecificProjectWithEmptyName() throws JsonProcessingException {
        Story newStory = new Story();
        newStory.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}/"
                + config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createProjectForStories", "cleanProjectForStories"})
    public void shouldNotCreateAStoryOfASpecificProjectWithNullName() throws JsonProcessingException {
        Story newStory = new Story();
        newStory.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("PROJECT_URL") + "{projectId}/"
                + config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", defaultProjectData.getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }
}
