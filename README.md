**# Objetivo**  
Aplicar o uso do **Gherkin** com **Cucumber** e utilizar a ferramenta **RestAssured** para automatizar os cenários de teste escritos em **Gherkin** para **APIs de Product Review** do **Multibags**, que foram escritos no **Lab anterior**.

---

**# Passos Iniciais**  
1. Abra o projeto no **IntelliJ IDEA** e importe-o como projeto **Maven**.
2. Execute os cenários de teste das funcionalidades de **"Login"** e **"Customer Profile"** já implementadas no projeto, localizados nos arquivos:
   - `src/test/resources/unicamp/br/inf321/Login.feature`
   - `src/test/resources/unicamp/br/inf321/CustomerProfile.feature`
3. Os 2 cenários existentes para essas funcionalidades devem estar passando.
4. Os cenários podem ser executados de três formas:
   - Através do **plugin do Cucumber** executando os arquivos **feature**.
   - Através da classe **Runner**:
     ```
     src/test/java/unicamp/br/inf321/RunCucumberTest.java
     ```
   - Utilizando o **Maven**:
     ```
     mvn clean test
     ```

---

**# Implementação dos Cenários de "Product Review"**  
Seguindo:
- A **documentação do RestAssured**.
- Os **exemplos dos passos** dos cenários já implementados no projeto.
- A **documentação das APIs de Product Review** no **Swagger**.

Implemente as **definições de passos** dos cenários escritos em **Gherkin** para a funcionalidade **"Product Review"**.

---

**# Regras de Negócio**  
- **Endpoints:** Apenas **endpoints públicos** devem ser utilizados (**sem `/private` no path**), pois os privados exigem credenciais de admin.
- **Autenticação:** Endpoints com `/auth` exigem autenticação do cliente, sendo necessário enviar o **token de autenticação** no **header** (vide cenário de exemplo na funcionalidade **"Customer Profile"**).
- **Funcionalidades do Cliente:**
  - Cadastrar um **novo review** para um produto.
  - Alterar o **review de um produto**.
  - Excluir um **review enviado** pelo próprio cliente.
  - Visualizar os **reviews de um produto**.
- **Validação de Reviews:**
  - O cliente **somente poderá cadastrar um review** para um produto **se ainda não houver outro review dele para esse produto**.
  - Se já houver um review, ele deve ser **excluído antes** de enviar um novo review.
  - Caso contrário, deve ser exibida a seguinte mensagem de erro com **status code 400**:
    ```
    "You have evaluated this product"
    ```
- **Bug Conhecido:** A API de **alterar review** de um produto **está com bug** e o teste deve capturar essa falha.

---

**# Definições de Passos de Verificação (Then/Então)**  
- **Para requisições de APIs com corpo de resposta:**
  - **Validar o status code**.
  - **Validar o JSON Schema** (arquivos já localizados na pasta `src/test/resources/unicamp/br/inf321`).
  - **Validar os valores da resposta**, garantindo que batem com as informações enviadas nas requisições para as APIs.
- **Para requisições de APIs sem corpo de resposta:**
  - **Apenas o status code deve ser validado**.

---

