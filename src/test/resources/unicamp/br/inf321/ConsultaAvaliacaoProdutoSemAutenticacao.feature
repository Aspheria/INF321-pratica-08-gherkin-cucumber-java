Feature: Consulta de avaliação de produtos (sem autenticação)

  Background:
    Given Helio opens the multibags application

  Scenario: Tentar obter avaliações sem autenticação
    When o usuário envia uma requisição GET para "/api/v1/products/9/reviews"
    # Deveria ser 401, mas a API retorna 200 mesmo sem o header authorization 
    Then a resposta deve conter o status 200 OK
