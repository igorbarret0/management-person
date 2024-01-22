<h1 align="center">
  USER MANAGER
</h1>

API para gerenciamento usuários (CRUD) 

## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Mysql](https://dev.mysql.com/downloads/)
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Test Containers](https://testcontainers.com/)

## Práticas adotadas

- DRY, TDD
- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de respostas de erro
- Geração automática do Swagger com a OpenAPI 3

## Como Executar

- Clonar repositório git
```
git clone git@github.com:igorbarret0/management-person.git
```

- Construir o projeto:
```
./mvnw clean package
```


A API poderá ser acessada em [localhost:8080](http://localhost:8080).
O Swagger poderá ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/)

- Salvar usuário
```
$ POST http://localhost:8080/person

[
  {
    "firstName": "The",
    "lastName": "Rock",
    "address": "USA Address",
    "gender": "Male",
    "email": "therock@email.com"
  }
]
```

- Listar Todos Usuários
```
$ GET http://localhost:8080/person

[
  {
    "firstName": "The",
    "lastName": "Rock",
    "address": "USA Address",
    "gender": "Male",
    "email": "therock@email.com"
  }
]
```

- Listar Usuário Pelo ID
```
$ GET http://localhost:8080/person/1

[
  {
    "firstName": "The",
    "lastName": "Rock",
    "address": "USA Address",
    "gender": "Male",
    "email": "therock@email.com"
  }
]
```

- Atualizar Usuário
```
$ PUT http://localhost:8080/person

[
  {
    "id": 1,
    "firstName": "The",
    "lastName": "Rock",
    "address": "LA Address",
    "gender": "Male",
    "email": "therock@email.com"
  }
]
```

- Remover Usuário
```
 DELETE http://localhost:8080/person/1
