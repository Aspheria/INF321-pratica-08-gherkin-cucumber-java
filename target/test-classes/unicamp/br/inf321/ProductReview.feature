Feature: Product Review API Multi Bags
  As a multibags customer user
  July wants to review a product
  So she can review any product

  Background:
    Given Jhon is logged on the multibags application
      | email    | o181804@g.unicamp.br |
      | password | aA#123456789         |

  Scenario: Review realizada com sucesso
    When o usuário envia um POST para a URL "/api/v1/auth/products/4/reviews" com os seguintes dados:
      | customerId | 654              |
      | date       | 2025-01-18       |
      | description| Excelente teste! |
      | language   | en               |
      | rating     | 5                |
      | productId  | 4                |
    Then o response status code deve ser 201 Created
    And o response body deve conter o ID do review criado

  Scenario: Excluir um review existente com sucesso
    Given que o usuário está autenticado e possui um token válido
    And que o usuário já criou um review para o produto "123" com reviewId "456"
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/456"
    Then a resposta deve conter o status 204 No Content
    And o review deve ser removido do sistema

  Scenario: Tentar excluir um review sem autenticação
    Given que o usuário não está autenticado
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/456"
    Then a resposta deve conter o status 401 Unauthorized
    And a mensagem de erro deve indicar "Unauthorized"

  Scenario: Excluir um review inexistente
    Given que o usuário está autenticado e possui um token válido
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/999999"
    Then a resposta deve conter o status 404 Not Found
    And a mensagem de erro deve indicar "Review not found"

  Scenario: Excluir um review de outro usuário
    Given que o usuário está autenticado e possui um token válido
    And que o review com ID "456" pertence a outro usuário
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/456"
    Then a resposta deve conter o status 403 Forbidden
    And a mensagem de erro deve indicar "Forbidden"

  Scenario: Excluir um review antes de criar um novo
    Given que o usuário já possui um review para o produto "123"
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/456"
    Then a resposta deve conter o status 204 No Content
    And o review deve ser removido do sistema
    And o usuário pode criar um novo review para o produto "123"
