pipeline{
    agent any
     parameters {
            string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Enter the branch name')
        }
    tools{
        maven 'maven_3_9_6'
    }
    stages{
        stage("Build maven project"){
            steps{
                checkout scmGit(branches: [[name: "*/${params.BRANCH_NAME}"]], extensions: [], userRemoteConfigs: [[url: 'https://github.com/PrashantThakurNitP/weather-microservice.git']])
                sh 'mvn clean package'
            }
        }
         stage('Run JUnit Tests') {
                    steps {
                        echo "Running unit tests"
                        sh 'mvn test'
                    }
                }
        stage("Build Docker Image"){
            steps{
                script{
                    sh 'docker build  -t prathaku3docker/weather-microservice .'
                }
            }
        }
        stage("Push Image to Docker Hub"){
            steps{
                script{

                    withCredentials([string(credentialsId: 'dockerhubpwd2', variable: 'dockerhubpwd2')]) {
                         sh 'docker login -u prathaku3docker -p ${dockerhubpwd2}'
                    }
                    sh 'docker push prathaku3docker/weather-microservice:latest'
                }
            }
        }
              stage("Push Docker Image to Minikube") {
                   steps {
                        script {
                      // Set the Minikube context
                    sh 'kubectl config use-context minikube'

                    // Update the deployment.yaml file with your image details
                    sh 'sed -i "s|image: your-image-name:latest|image: prathaku3docker/weather-microservice:latest|g" deploymentservice.yaml || true'

                    // Apply the Kubernetes manifest file to deploy your application
                    sh 'kubectl apply -f deploymentservice.yaml'
                        }
                     }

            }
    }

}