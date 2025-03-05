Feature: Atualização de Avaliações de Produtos (não autenticado)

  Background:
    Given there is no logged user on the multibags application

  Scenario: Tentar atualizar um review sem autenticação
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/7/reviews/278" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                         278 |
      | language    | en                                          |
      | productId   |                                           7 |
      | rating      |                                           5 |
    Then a resposta deve conter o status 401 OK
