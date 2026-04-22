# Sample Web Project - Cliente do Framework

Projeto de exemplo para consumo do pacote privado:

- groupId: br.com.lumin.qa
- artifactId: automation-framework-base
- repository: GitHub Packages (luminato/qa_projeto_consultoria)

## Pré-requisitos

- Java 17
- Maven 3.8+
- Token GitHub com read:packages

## Setup local (Windows)

1. Criar/editar C:/Users/<seu-usuario>/.m2/settings.xml

Exemplo:

<settings>
  <servers>
    <server>
      <id>github</id>
      <username>SEU_USUARIO_GITHUB</username>
      <password>SEU_TOKEN_READ_PACKAGES</password>
    </server>
  </servers>
</settings>

2. Rodar build de validação:

- mvn -B clean test

## Perfis úteis

- mvn -B test -Psmoke-local
- mvn -B test -Psmoke-mobile
- mvn -B test -Pregression-parallel

## Jenkins local

Este projeto já contém Jenkinsfile pronto para CI local.

Configuração necessária no Jenkins:

1. Criar credential do tipo Secret text com id: github-packages-token
2. Valor do secret: token com read:packages
3. Rodar pipeline normalmente

Parâmetro GITHUB_PACKAGES_USER:

- padrão: luminato
- altere para sua org/usuário se necessário

## Segurança

- Não commitar settings.xml
- Não commitar token
- Não publicar logs com credenciais
