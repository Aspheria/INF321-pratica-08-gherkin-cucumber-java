#@review
#Feature: Product Review API Multi Bags
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
    When o usuário envia um novo review para o produto com ID "4" com rating 6 e comentário "Great product!"
    Then o response status code deve ser 400 Bad Request
    And o response body deve conter o erro "must be less than or equal to 5"

  Scenario: Criar um review para um produto já avaliado pelo usuário
    Given o usuário já criou um review para o produto com ID "4"
    When o usuário tenta criar um novo review para o mesmo produto
    Then o response status code deve ser 400 Bad Request
    And o response body deve conter o erro "You have evaluated this product"
#
#  Scenario: Criar um novo review após excluir o review anterior
#    Given o usuário já criou um review para o produto com ID "4"
#    And o usuário excluiu o review existente
#    When o usuário envia uma requisição POST para "/api/v1/auth/products/4/reviews" com um novo review válido
#    Then o response status code deve ser 201 Created
#    And o novo review deve ser salvo no sistema
