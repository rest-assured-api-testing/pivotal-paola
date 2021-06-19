package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Comments;
import entities.Project;
import entities.Stories;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseStoriesForComments;

public class CommentTest extends CreateBaseStoriesForComments {
    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_FORBIDDEN = 403;
    final int STATUS_NOT_FOUND = 404;

    @Test(groups = {"createComment", "cleanProject"})
    public void shouldGetAllCommentsFromASpecificStory() {
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldValidateCommentSchemaFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("commentId", responseComment.getBody(Comments.class).getId().toString());
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/comment.json");
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotShowCommentsFromProjectsWithWrongId() {
        String fakeId = "6756756";
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", fakeId);
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotShowCommentsFromProjectWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", null);
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotShowCommentsFromStoriesWithWrongId() {
        String fakeId = "6756756";
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("storiesId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotShowCommentsFromStoriesWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("storiesId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldGetASpecificCommentFromASpecificStory() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Comments.class).getId().toString());
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotGetACommentWithWrongIdFromAProject() {
        String fakeId = "34553";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotGetACommentWithNullIdFromASpecificProject() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotCreateACommentWithEmptyText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("");
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotCreateACommentWithNullText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotCreateACommentWithOnlyBlankSpacesText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("     ");
        apiRequest.setEndpoint(config.setConfig().getProperty("COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.POST);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithNullText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText(null);
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithEmptyText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithOnlyBlankSpacesInText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("                   ");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldUpdateACreatedCommentIgnoringInitialBlankSpacesInText() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("                   changing");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getBody(Comments.class).getText(), "                   changing");
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithWrongStoryId() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("Correct change comment text");
        String fakeIdStory = "56546456";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", fakeIdStory);
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_BAD_REQUEST);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithNullStoryId() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("Correct change comment text");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", response.getBody(Project.class).getId().toString());
        apiRequest.addPathParam("storiesId", null);
        apiRequest.addPathParam("commentId", responseComment.getBody(Stories.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithWrongProjectId() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("Correct change comment text");
        String fakeIdStory = "56546456";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", fakeIdStory);
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Comments.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test(groups = {"createComment", "cleanComment", "cleanProject"})
    public void shouldNotUpdateACreatedCommentWithNullProjectId() throws JsonProcessingException {
        Comments newComment = new Comments();
        newComment.setText("Correct change comment text");
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_COMMENTS_URL"));
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.addPathParam("projectId", null);
        apiRequest.addPathParam("storiesId", responseStories.getBody(Stories.class).getId().toString());
        apiRequest.addPathParam("commentId", responseComment.getBody(Comments.class).getId().toString());
        apiRequest.setBody(new ObjectMapper().writeValueAsString(newComment));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }
}
