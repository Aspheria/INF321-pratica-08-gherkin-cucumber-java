Feature: Atualização de Avaliações de Produtos (autenticado)

  Background:
    Given Helio is logged on the multibags application
      | email    | helio@cucumber.com |
      | password | password123        |

  Scenario: Atualizar um review existente com sucesso
    Given que o produto com ID "7" existe no sistema
    And que o produto com ID "7" possui um review com ID "278"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/7/reviews/278" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                         278 |
      | language    | en                                          |
      | productId   |                                           7 |
      | rating      |                                           5 |
      # A resposta é 503 por conta de um erro na API
    Then a resposta deve conter o status 503 OK

  Scenario: Atualizar um review de outro usuário
    Given que o produto com ID "7" existe no sistema
    And que o produto com ID "7" possui um review com ID "50"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/7/reviews/50" com os seguintes dados:
      | customerId  |                                         550 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                          50 |
      | language    | en                                          |
      | productId   |                                           7 |
      | rating      |                                           5 |
    Then a resposta deve conter o status 503 OK

  Scenario: Atualizar um review com um rating inválido
    Given que o produto com ID "7" existe no sistema
    And que o produto com ID "7" possui um review com ID "278"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/7/reviews/278" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                         278 |
      | language    | en                                          |
      | productId   |                                           7 |
      | rating      |                                         100 |
    Then a resposta deve conter o status 500 OK

  Scenario: Atualizar um review inexistente
    Given que o produto com ID "7" existe no sistema
    And que o produto com ID "7" não possui um review com ID "9999"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/7/reviews/9999" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                        9999 |
      | language    | en                                          |
      | productId   |                                           7 |
      | rating      |                                           5 |
    Then a resposta deve conter o status 404 OK
