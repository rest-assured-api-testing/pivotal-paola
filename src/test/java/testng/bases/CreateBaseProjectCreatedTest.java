package testng.bases;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class CreateBaseProjectCreatedTest {
    protected ApiRequest apiRequest;
    protected ApiResponse response = new ApiResponse();
    protected ApiResponse responseEpic = new ApiResponse();
    protected ApiResponse responseLabel = new ApiResponse();
    protected ApiResponse responseStories = new ApiResponse();
    protected SettingConfigFile config;

    @BeforeMethod
    public void setBasicForTests() throws JsonProcessingException {
        config = new SettingConfigFile();
        apiRequest  = new ApiRequest();
        apiRequest.setHeaders("X-TrackerToken", config.setConfig().getProperty("TOKEN"));
        apiRequest.setBaseUri(config.setConfig().getProperty("BASE_URI"));
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
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }

    @BeforeMethod(dependsOnMethods = "setBasicForTests", onlyForGroups = "createEpics", groups = "cleanProject")
    public void setDefaultEpicForTest() throws JsonProcessingException {
        Epics newEpic = new Epics();
        newEpic.setName("Epics created");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("EPICS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newEpic));
        responseEpic = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanEpics")
    public void deleteDefaultEpicForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_EPICS_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("epicId", responseEpic.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }

    @BeforeMethod(dependsOnMethods = "setBasicForTests", onlyForGroups = "createLabels", groups = "cleanProject")
    public void setDefaultLabelForTest() throws JsonProcessingException {
        Labels newLabel = new Labels();
        newLabel.setName("Label created");
        apiRequest.clearPath();
        apiRequest.setEndpoint("projects/{projectId}/labels/");
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newLabel));
        responseLabel = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanLabels")
    public void deleteDefaultLabelForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_LABEL_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("labelId", responseLabel.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }

    @BeforeMethod(dependsOnMethods = "setBasicForTests", onlyForGroups = "createStories", groups = "cleanProject")
    public void setDefaultStoriesForTest() throws JsonProcessingException {
        Stories newStory = new Stories();
        newStory.setName("Stories created");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        responseStories = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanStories")
    public void deleteDefaultStoriesForTests() {
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("storyId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }
}
