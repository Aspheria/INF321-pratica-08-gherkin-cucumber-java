@minha_tag
Feature: Feature: Exclusão de Avaliações de Produtos
  As a multibags customer user
  July wants to review a product
  So she can review any product

  Background:
    Given Jhon is logged on the multibags application
      | email    | o181804@g.unicamp.br |
      | password | aA#123456789         |

  Scenario: Excluir um review existente com sucesso
    Given que o usuário está autenticado e possui um token válido
    And que o usuário já criou um review para o produto "123" com reviewId "456"
    When o usuário envia uma requisição DELETE para "/api/v1/auth/products/123/reviews/456"
    Then a resposta deve conter o status 204 No Content
    And o review deve ser removido do sistema
# need to run with >mvn test -Dcucumber.filter.tags="@minha_tag"