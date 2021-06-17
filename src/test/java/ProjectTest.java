import api.ApiManager;
import api.ApiMethod;
import api.ApiRequest;
import api.ApiResponse;
import entities.Project;
import org.junit.jupiter.api.BeforeAll;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ProjectTest {
    ApiRequest apiRequest = new ApiRequest();
    Project project;

    @BeforeTest
    public void setAuthForProjectTests() {
        apiRequest.setToken("ba7f89d3644330f22910f05322e584db");
        apiRequest.setBaseUri("https://www.pivotaltracker.com/services/v5/");
    }

    @BeforeMethod(onlyForGroups = "createProject")
    public void createProject() {

    }

    @Test
    public void getAllProjects() {
        apiRequest.setEndpoint("projects");
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = new ApiResponse(ApiManager.execute(apiRequest));
        int expectedStatusCode = 200;
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @Test(groups = "createProject")
    public void getProjectById() {
        apiRequest.setEndpoint("projects/{}");
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("projectId", "2504950");

        ApiResponse response = new ApiResponse(ApiManager.execute(apiRequest));
        project = response.getBody(Project.class);
        Assert.assertEquals(project.getName(), "Another project");

    }
}
