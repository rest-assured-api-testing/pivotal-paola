Feature: Request for project endpoint
  Get a account

  @CreateProject
  Scenario: Get account detail
    Given I build "GET" request
    When I executed "accounts/{accountId}" request
    Then The response status code should be "OK"