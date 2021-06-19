package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Project;
import entities.Stories;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseProjectCreatedTest;

public class StoriesTest extends CreateBaseProjectCreatedTest {

    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_FORBIDDEN = 403;
    final int STATUS_NOT_FOUND = 404;


    @Test(groups = {"cleanProject"})
    public void shouldGetAllStoriesFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldValidateStoriesSchemaFromASpecificProject() {
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/stories.json");
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotShowStoriesFromProjectsWithWrongId() {
        String fakeId = "2342";
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_FORBIDDEN);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotShowStoriesFromProjectsWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldGetASpecificStoriesFromASpecificProject() {
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotGetAStoriesWithWrongIdFromASpecificProject() {
        String fakeId = "34553";
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotGetAStoriesWithNullIdFromASpecificProject() {
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAStoryOfASpecificProjectWithEmptyName() throws JsonProcessingException {
        Stories newStory = new Stories();
        newStory.setName("");
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAStoryOfASpecificProjectWithBlankSpaces() throws JsonProcessingException {
        Stories newStory = new Stories();
        newStory.setName("                  ");
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"cleanProject"})
    public void shouldNotCreateAStoryOfASpecificProjectWithNullName() throws JsonProcessingException {
        Stories newStory = new Stories();
        newStory.setName(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("STORIES_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newStory));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotUpdateACreatedStoriesWithEmptyName() throws JsonProcessingException {
        Stories emptyStories = new Stories();
        emptyStories.setName("");
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(emptyStories));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotUpdateACreatedStoriesWithBlankSpaces() throws JsonProcessingException {
        Stories emptyStories = new Stories();
        emptyStories.setName("               ");
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(emptyStories));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createStories", "cleanStories", "cleanProject"})
    public void shouldNotUpdateACreatedStoriesWithNullName() throws JsonProcessingException {
        Stories nullStories = new Stories();
        nullStories.setName(null);
        apiRequest.setEndpoint("/projects/{projectId}/stories/{storyId}");
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storyId", null);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(nullStories));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
