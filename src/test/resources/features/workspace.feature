Feature: Create workspace
  create a new workspace

  @CreateWorkspace
  Scenario: Create workspace detail
    Given I build a "POST" request
    When I executed path "my/workspaces" with "WorkName"
    Then the response status code should be "Ok"