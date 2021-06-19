Feature: Get account by id
  get account detail for specific Id
  
  @GetAccount
  Scenario: Get account detail
    Given I build the "GET" request
    When I executed "accounts/{accountId}" request with id
    Then The response status code should be "OK" with server
    
    