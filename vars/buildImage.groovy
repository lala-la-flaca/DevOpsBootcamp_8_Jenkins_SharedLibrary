#!/user/bin/env groovy

def call() {

    echo "building the docker Image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
        sh """
            cd java-maven-app/
            docker build -t lala011/demo-app:jma-2.0 .
            echo $PWD | docker login -u $USER --password-stdin
            docker push lala011/demo-app:jma-2.0
        """


    }
}