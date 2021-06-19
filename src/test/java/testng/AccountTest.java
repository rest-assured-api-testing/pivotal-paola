package testng;

import api.ApiManager;
import api.ApiMethod;
import api.ApiResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import testng.bases.CreateBaseTest;

public class AccountTest extends CreateBaseTest {
    final int STATUS_OK = 200;
    final int STATUS_NO_CONTENT = 204;
    final int STATUS_BAD_REQUEST = 400;
    final int STATUS_NOT_FOUND = 404;

    @Test
    public void shouldShowTheActualAccountsRegistered() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ACCOUNT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test
    public void shouldValidateAccountSchema() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_ACCOUNT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("accountId", config.setConfig().getProperty("ID_ACCOUNT"));
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
        response.validateBodySchema("schemas/account.json");
    }

    @Test
    public void shouldShowTheActualAccountsSummaries() {
        apiRequest.setEndpoint("account_summaries/");
        apiRequest.setMethod(ApiMethod.GET);
        ApiResponse response = ApiManager.execute(apiRequest);
        response.getResponse().then().log().body();
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }

    @Test
    public void shouldNotShowAccountWithWrongId() {
        String fakeId = "456546";
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_ACCOUNT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("accountId", fakeId);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test
    public void shouldNotShowAccountWithNullId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_ACCOUNT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("accountId", null);
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_NOT_FOUND);
    }

    @Test
    public void shouldShowAccountIgnoringBlankSpacesInId() {
        apiRequest.setEndpoint(config.setConfig().getProperty("ID_ACCOUNT_URL"));
        apiRequest.setMethod(ApiMethod.GET);
        apiRequest.addPathParam("accountId", "         " + config.setConfig().getProperty("ID_ACCOUNT"));
        ApiResponse response = ApiManager.execute(apiRequest);
        Assert.assertEquals(response.getStatusCode(), STATUS_OK);
    }
}
