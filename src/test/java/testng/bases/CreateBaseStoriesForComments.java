package testng.bases;

import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.SettingConfigFile;
import entities.Comments;
import entities.Project;
import entities.Stories;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class CreateBaseStoriesForComments {
    protected ApiRequest apiRequest;
    protected ApiResponse response = new ApiResponse();
    protected ApiResponse responseStories = new ApiResponse();
    protected ApiResponse responseComment = new ApiResponse();
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

        Stories newStory = new Stories();
        newStory.setName("Stories created");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        responseStories = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanProject")
    public void deleteDefaultProjectForTests() {
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("SPECIFIC_PROJECT_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }

    @BeforeMethod(dependsOnMethods = "setBasicForTests", onlyForGroups = "createComment")
    public void setDefaultCommentForTest() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("This is a new comment");
        apiRequest.clearPath();
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        responseComment = ApiManager.executeWithBody(apiRequest);
    }

    @AfterMethod(onlyForGroups = "cleanComment")
    public void deleteDefaultCommentForTests() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.DELETE);
        apiRequest.addPathParam("commentId", responseComment.getBody(Comments.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiManager.execute(apiRequest);
    }
}
