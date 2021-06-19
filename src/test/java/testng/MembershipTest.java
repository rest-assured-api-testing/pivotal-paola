package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Membership;
import entities.MembershipPerson;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseTest;

public class MembershipTest extends CreateBaseTest {
    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_NOT_FOUND = 404;
    final String DEFAULT_MEMBERSHIP_ID = "3403900";

    @Test
    public void shouldShowTheActualMembershipOfAccount() {
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test
    public void shouldValidateMembershipAccountSchema() {
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + DEFAULT_MEMBERSHIP_ID);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/membership.json");
    }

    @Test
    public void shouldShowMembershipWithId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + DEFAULT_MEMBERSHIP_ID);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test
    public void shouldNotShowMembershipWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + null);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test
    public void shouldNotShowMembershipWithWrongId() {
        String fakeId = "2434635";
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + fakeId);
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test
    public void shouldUpdateMembershipAdmin() throws JsonProcessingException {
        Membership member = new Membership();
        member.setAdmin(true);
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + DEFAULT_MEMBERSHIP_ID);
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(member));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getBody(Membership.class).isAdmin(), true);
    }

    @Test
    public void shouldUpdateMembershipOwner() throws JsonProcessingException {
        Membership member = new Membership();
        member.setOwner(false);
        apiRequest.setEndpoint(config.setConfig().getProperty("MEMBERSHIP_URL") + DEFAULT_MEMBERSHIP_ID);
        apiRequest.setMethod(ApiMethod.PUT);
        apiRequest.setBody(new ObjectMapper().writeValueAsString(member));
        ApiResponse response = ApiManager.executeWithBody(apiRequest);
        Assert.assertEquals(response.getBody(Membership.class).isOwner(), false);
    }
}
