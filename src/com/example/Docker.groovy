#!/user/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker Image..."
        script.withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
        script.sh """
            cd java-maven-app/
            docker build -t $imageName .
            echo $PWD | docker login -u $USER --password-stdin
            docker push $imageName
        """
        }
    }

   // def dockerLogin() {
   //     script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    //        script.sh "echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin"
      //  }
   // }

  //  def dockerPush(String imageName) {
    //    script.sh "docker push $imageName"
    //}
}
