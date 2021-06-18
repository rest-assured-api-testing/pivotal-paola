Feature: Request for project endpoint
  description scenario for the endpoint project

  @CreateProject
  Scenario: Get a Project
    Given I build "GET" request
    When I executed "projects/{projectId}" request
    Then The response status code should be "OK"