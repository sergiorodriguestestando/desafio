# Desafio Sicreci 
Esse arquivo tem objetivo de descrever como executar os testes de integração usando o framework rest assured, com a linguagem de programação JAVA.

**Pré-requisitos**

- Java 11
- IDE Eclipse ou InteliJ
- maven


**Executando os testes**
Abrir CMD ou Bash para maquinas linux, ir até a pasta raíz do projeto e executar o comando abaixo.

    mvn test

**Bugs**

Cenário: BugBuscarProdutosRotaIncorreta

Dado que realize uma requisão para "/auth/product"
Então o status deve ser "404"

??? Cenário: validarBuscarProdutosStatus200TokenRefresh ???
Dado que realize uma requisão para "https://dummyjson.com/auth/login"
E usar o campo "refreshToken" para autenticação
Então o status deve ser "200" ??