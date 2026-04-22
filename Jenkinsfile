pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
    skipDefaultCheckout(true)
  }

  parameters {
    choice(name: 'PROFILE_NAME', choices: ['smoke-local', 'smoke-mobile', 'regression-parallel', 'demo-apresentacao', 'nightly-complete'], description: 'Perfil de execução da suíte')
    booleanParam(name: 'GENERATE_ALLURE', defaultValue: true, description: 'Gerar o relatório HTML do Allure')
    booleanParam(name: 'DB_ENABLED', defaultValue: false, description: 'Persistir métricas no banco')
    string(name: 'CLIENT_NAME', defaultValue: 'demo-client', description: 'Nome do cliente')
    string(name: 'ENVIRONMENT_NAME', defaultValue: 'jenkins', description: 'Nome do ambiente')
    string(name: 'BASE_URL_OVERRIDE', defaultValue: '', description: 'Sobrescrever URL base, se necessário')
    string(name: 'GITHUB_PACKAGES_USER', defaultValue: 'luminatoqa', description: 'Usuário/organização do GitHub Packages')
  }

  environment {
    MAVEN_SETTINGS = 'settings-ci.xml'
    GITHUB_PACKAGES_CREDENTIALS_ID = 'github-packages-token'
    MAVEN_LOG_ARGS = '-q -ntp -Dorg.slf4j.simpleLogger.defaultLogLevel=error'
  }

  stages {
    stage('Checkout') {
      steps {
        deleteDir()
        checkout scm
      }
    }

    stage('Prepare Maven Settings') {
      steps {
        withCredentials([string(credentialsId: env.GITHUB_PACKAGES_CREDENTIALS_ID, variable: 'GITHUB_PACKAGES_TOKEN')]) {
          script {
            def mavenCmd = isUnix() ? 'mvn' : 'mvn.cmd'
            def settingsXml = """<?xml version=\"1.0\" encoding=\"UTF-8\"?>
                                  <settings xmlns=\"http://maven.apache.org/SETTINGS/1.0.0\"
                                            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                                            xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd\">
                                    <servers>
                                      <server>
                                        <id>github</id>
                                        <username>${params.GITHUB_PACKAGES_USER}</username>
                                        <password>${GITHUB_PACKAGES_TOKEN}</password>
                                      </server>
                                    </servers>
                                  </settings>
                                  """
            writeFile file: env.MAVEN_SETTINGS, text: settingsXml
          }
        }
      }
    }

    stage('Install Browser') {
      steps {
        script {
          def mavenCmd = isUnix() ? 'mvn' : 'mvn.cmd'
          if (isUnix()) {
            sh "${mavenCmd} ${env.MAVEN_LOG_ARGS} -s ${env.MAVEN_SETTINGS} exec:java -Dexec.classpathScope=test \"-Dexec.mainClass=com.microsoft.playwright.CLI\" \"-Dexec.args=install chromium\""
          } else {
            bat "${mavenCmd} ${env.MAVEN_LOG_ARGS} -s ${env.MAVEN_SETTINGS} exec:java -Dexec.classpathScope=test \"-Dexec.mainClass=com.microsoft.playwright.CLI\" \"-Dexec.args=install chromium\""
          }
        }
      }
    }

    stage('Run Tests') {
      steps {
        script {
          def mavenCmd = isUnix() ? 'mvn' : 'mvn.cmd'
          def goals = ['clean', 'test']

          def extraArgs = [
            "-P${params.PROFILE_NAME}",
            "-Dclient.name=${params.CLIENT_NAME}",
            "-Denvironment.name=${params.ENVIRONMENT_NAME}",
            "-Ddb.enabled=${params.DB_ENABLED}",
            "-Dheadless=true",
            "-Dlogging.level.org.hibernate=ERROR",
            "-Dlogging.level.org.hibernate.SQL=ERROR",
            "-Dorg.slf4j.simpleLogger.log.org.hibernate=error",
            "-Dorg.slf4j.simpleLogger.log.org.hibernate.SQL=error"
          ]
          if (params.BASE_URL_OVERRIDE?.trim()) {
            extraArgs << "-Dbase.url=${params.BASE_URL_OVERRIDE}"
          }

          def fullCommand = "${mavenCmd} ${env.MAVEN_LOG_ARGS} -s ${env.MAVEN_SETTINGS} ${goals.join(' ')} ${extraArgs.join(' ')}"

          if (isUnix()) {
            sh fullCommand
          } else {
            bat fullCommand
          }
        }
      }
    }
  }

  post {
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**, target/qa-metrics/**, target/allure-results/**', fingerprint: true, allowEmptyArchive: true
      script {
        if (params.GENERATE_ALLURE) {
          if (fileExists('target/allure-results')) {
            // Publica o relatorio no painel do plugin Allure para avaliacao no Jenkins.
            catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
              allure([
                includeProperties: false,
                jdk: '',
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
              ])
            }
          } else {
            echo 'Allure nao publicado: pasta target/allure-results nao encontrada.'
          }
        }
      }
      cleanWs(cleanWhenNotBuilt: false, deleteDirs: true, disableDeferredWipeout: true)
    }
  }
}
