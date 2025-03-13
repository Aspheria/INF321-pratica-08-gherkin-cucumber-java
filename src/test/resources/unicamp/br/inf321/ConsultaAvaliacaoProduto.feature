Feature: Consulta de avaliação de produtos

  Background:
    Given Helio is logged on the multibags application
      | email    | helio@cucumber.com |
      | password | password123        |

  Scenario: Obter todas as avaliações de um produto com sucesso
    Given que existem avaliações para o produto "9"
    When o usuário envia uma requisição GET para "/api/v1/products/9/reviews"
    Then a resposta deve conter o status 200 OK
    And a lista de avaliações deve ser retornada

   Scenario: Obter avaliações de um produto inexistente
     When o usuário envia uma requisição GET para "/api/v1/products/999999/reviews"
     Then a resposta deve conter o status 404 OK

