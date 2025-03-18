#!/user/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker Image..."
        script.sh """
            cd java-maven-app/
            docker build -t $imageName .
        """
    }

   def dockerLogin() {

       script.echo "Login the docker repository..."
        script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
            script.sh "echo '${script.PWD}' | docker login -u '${script.USER}' --password-stdin"
        }
    }

    def dockerPush(String imageName) {
        script.echo "Pushing the docker image..."
        script.sh "docker push $imageName"
    }
}
