# Texo IT App

API RESTful para possibilitar a leitura da lista de indicados e vencedores
da categoria Pior Filme do Golden Raspberry Awards.


Esta aplicação foi desenvolvida utilizando:  

- **Java 19**
- **Quarkus v.3.7.2**
- **Banco de dados em memória H2.**
- **OpenCSV 5.3**


### Instalação
É necessário executar o comando abaixo para instalar e configurar todas as dependências do projeto.
```
.\mvnw install
```
### Execução
O comando abaixo executa a aplicação para validação da estrutura de resposta proposta no desafio.
```
.\mvnw quarkus:dev
```

URL da API para validação do JSON: http://localhost:8080/movies

### Testes
O comando abaixo executa os testes de integrações isolados de casos especifícos como: 
 - Validação da estrutura JSON
 - Teste de performance da API
 - Teste te integração com Banco de Dados H2
```
.\mvnw test
```