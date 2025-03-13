#Feature: Customer Profile
  As a multibags customer user
  Jhon wants to see his profile
  So he can check any outdated information

  Scenario: Should show customer profile
    Given Jhon is logged on the multibags application
      | email    | o181804@g.unicamp.br |
      | password | aA#123456789         |
    When he selects the option to see his profile
    Then his profile should be shown with success
