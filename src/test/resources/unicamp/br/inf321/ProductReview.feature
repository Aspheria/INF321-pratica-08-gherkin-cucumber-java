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

  Scenario: Tentativa de criar um review com rating inválido
    When o usuário envia um novo review para o produto com ID "12345" com rating 6 e comentário "Great product!"
    Then o response status code deve ser 400 Bad Request
    And o response body deve conter o erro "Rating must be between 1 and 5"

  Scenario: Obter reviews para um produto existente
    Given o produto com ID "12345" possui reviews
    When o usuário solicita as reviews do produto com ID "12345"
    Then o response status code deve ser 200 OK
    And o response body deve conter a lista de reviews

  Scenario: Obter review de um produto inexistente
    When o usuário envia uma requisição de review para o produto com ID "99999"
    Then o response status code deve ser 404 Not Found
    And o response body deve conter o erro "Product not found"

  Scenario: Criar um review para um produto já avaliado pelo usuário
    Given o usuário já criou um review para o produto com ID "123"
    When o usuário tenta criar um novo review para o mesmo produto
    Then o response status code deve ser 400 Bad Request
    And o response body deve conter o erro "You have evaluated this product"

  Scenario: Criar um novo review após excluir o review anterior
    Given o usuário já criou um review para o produto com ID "123"
    And o usuário excluiu o review existente
    When o usuário envia uma requisição POST para "/api/v1/auth/products/123/reviews" com um novo review válido
    Then o response status code deve ser 201 Created
    And o novo review deve ser salvo no sistema
