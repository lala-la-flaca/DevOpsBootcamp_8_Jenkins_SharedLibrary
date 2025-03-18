#!/user/bin/env groovy

def call(String ImageName) {

    echo "building the docker Image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
        sh """
            cd java-maven-app/
            docker build -t $ImageName .
            echo $PWD | docker login -u $USER --password-stdin
            docker push $ImageName
        """


    }
}
