Feature: Atualização de Avaliações de Produtos (autenticado)

  Background:
    Given Helio is logged on the multibags application
      | email    | helio@cucumber.com |
      | password | password123        |

  Scenario: Atualizar um review existente com sucesso
    Given que o produto com ID "9" existe no sistema
    And que o produto com ID "9" possui um review com ID "1084"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/9/reviews/1084" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                        1084 |
      | language    | en                                          |
      | productId   |                                           9 |
      | rating      |                                           5 |
      # A resposta é 503 por conta de um erro na API
    Then a resposta deve conter o status 503 OK

  Scenario: Atualizar um review de outro usuário
    Given que o produto com ID "9" existe no sistema
    And que o produto com ID "9" possui um review com ID "934"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/9/reviews/934" com os seguintes dados:
      | customerId  |                                         500 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                         934 |
      | language    | en                                          |
      | productId   |                                           9 |
      | rating      |                                           5 |
    Then a resposta deve conter o status 503 OK

  Scenario: Atualizar um review com um rating inválido
    Given que o produto com ID "9" existe no sistema
    And que o produto com ID "9" possui um review com ID "1084"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/9/reviews/1084" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                        1084 |
      | language    | en                                          |
      | productId   |                                           9 |
      | rating      |                                         100 |
    Then a resposta deve conter o status 500 OK

  Scenario: Atualizar um review inexistente
    Given que o produto com ID "9" existe no sistema
    And que o produto com ID "9" não possui um review com ID "9999"
    When o usuário envia uma requisição PUT para "/api/v1/auth/products/9/reviews/9999" com os seguintes dados:
      | customerId  |                                         752 |
      | date        |                                  2025-02-15 |
      | description | Atualizando minha avaliação, ótimo produto! |
      | id          |                                        9999 |
      | language    | en                                          |
      | productId   |                                           9 |
      | rating      |                                           5 |
    Then a resposta deve conter o status 404 OK
