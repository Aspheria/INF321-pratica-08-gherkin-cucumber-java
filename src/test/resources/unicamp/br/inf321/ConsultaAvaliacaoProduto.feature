Feature: Consulta de avaliação de produtos

  Background:
    Given Helio está logado na aplicação multibags
      | email    | helio@cucumber.com |
      | password | password123        |

  Scenario: Obter todas as avaliações de um produto com sucesso
    Given que existem avaliações para o produto "123"
    When o usuário envia uma requisição GET para "/api/v1/products/123/reviews"
    Then a resposta deve conter o status 200 OK
    And a lista de avaliações deve ser retornada

  Scenario: Tentar obter avaliações sem autenticação
    Given que o usuário não está autenticado
    When o usuário envia uma requisição GET para "/api/v1/products/123/reviews"
    Then a resposta deve conter o status 401 Unauthorized
    And a mensagem de erro deve indicar "Unauthorized"

  Scenario: Obter avaliações de um produto inexistente
    Given que o usuário está autenticado e possui um token válidos
    When o usuário envia uma requisição GET para "/api/v1/products/999999/reviews"
    Then a resposta deve conter o status 404 Not Found
    And a mensagem de erro deve indicar "Product not found"

  Scenario: Falha ao obter avaliações devido a um erro interno no servidor
    Given que o usuário está autenticado e possui um token válido
    And que o sistema está instável
    When o usuário envia uma requisição GET para "/api/v1/products/123/reviews"
    Then a resposta deve conter o status 500 Internal Server Error
    And a mensagem de erro deve indicar "500 message"

  Scenario: Consultar as avaliações de um produto
    Given que o produto "123" possui reviews de múltiplos usuários
    When o usuário envia uma requisição GET para "/api/v1/products/123/reviews"
    Then a resposta deve conter o status 200 OK
    And a lista de avaliações deve conter apenas um review por usuário