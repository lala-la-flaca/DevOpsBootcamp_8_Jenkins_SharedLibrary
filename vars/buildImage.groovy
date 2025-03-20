#!/user/bin/env groovy

import com.example.Docker

def call(String imageName) {
    return new Docker(this).buildDockerImage(imageName)
}



       // echo "building the docker Image..."
        //withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
       // sh """
       //          cd java-maven-app/
       //          docker build -t $ImageName .
        //         echo $PWD | docker login -u $USER} --password-stdin
         //        docker push $ImageName
         //   """
       // }

//}