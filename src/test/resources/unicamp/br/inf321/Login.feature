Feature: Login
  As a multibags customer user
  July wants to login on the application
  So she can buy new products



  Scenario: Login with valid credentials
    Given July is registered on the multibags website
    When she logins with her credentials
      | email    | o181804@g.unicamp.br |
      | password | aA#123456789         |
    Then she should be logged with success
