# ![jenkins](https://github.com/user-attachments/assets/0a854b64-7e42-4941-af78-bea35ccd2f6f) Creating a CI Pipeline with Jenkins  

## Description

This demo project is part of **Module 8: Build Automation & CI/CD with Jenkins** from the **Nana DevOps Bootcamp**. It focuses on creating a Jenkins Shared Library using Groovy to centralize and reuse common CI/CD logic across multiple Jenkins pipelines.  

## 🚀 Technologies Used

- <b>Docker: Docker for containerization.</b>
- <b>Jenkins: Automation for CI/CD.</b>
- <b>Linux: Ubuntu for Server configuration and management.</b>
- <b>Digital Ocean: Cloud provider for hosting Jenkins server.</b>
- <b>GitHub: Source control for Jenkins pipelines and shared libraries.</b>
- <b>DockerHub: private docker repository.</b>
- <b>Maven: Build tool for Java application.</b>
- <b>Nana's Java Application: Java application developed by Nana from the Bootcamp.</b>
- <b>Groovy: Language for pipeline scripting and shared libraries.</b>


## 🎯 Features

- <b>Create a separate Git repository for Jenkins Shared Library project.</b>
- <b>Create functions in the Jenkins Shared Library to use in the Jenkins pipeline.</b>
- <b>Integrate the Global library in jenkins pipeline</b>
- <b>Integrate the shared library for a specific project</b>
- [Integrating Jenkins Webhooks](#-jenkins-webhooks-integration)

  

## 📝 Prerequisites
- Ensure that the Jenkins is running.


## 🏗 Project Architecture

<img src=""/>


## ⚙️ Project Configuration:

### Creating a Git repository for the Jenkins Shared Library project.

1. Go to your Github Account and create a new repository.


### Creating functions in the Jenkins Shared Library to use in the Jenkins pipeline.

1. Open IntelliJ and create a new Groovy project named jenkins-shared-library.
   
2. Create a new folder named Vars.
   
3. Inside the Vars folder, create a Groovy file named buildJar.groovy. Each file in the Vars folder contains a function to be executed by the Jenkinsfile, and the file name must match the function name called from the Jenkinsfile.
   
4. Ensure buildJar.groovy includes the shebang and function definition, as shown:<br>
   buildJar.groovy:

   ```bash
   #!/user/bin/env groovy

     def call() {
       echo "building the application for branch $BRANCH_NAME"
       dir('java-maven-app') {
                sh 'mvn package'
       }
   }
   ```
   
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/BuildJar%20SharedLibrary%20func.PNG" width=800/>
   
5. Create a separate Groovy file for each function you want to reuse in the Jenkins shared library.<br>

   buildImage.groovy file:
   
   ```bash
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
   ```
   
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/buildImage%20sharedlibrary%20func.PNG" width=800 />
   
7. Create a new branch for the java-maven-app repository named Jenkins-shared-library.
   
8. Edit the Jenkinsfile in the java-maven-app repository add the groovy shebang

   ```bash
   #!/user/bin/env groovy
   ```
   
9. Add the steps to call the buildJar.groovy and buildImage.groovy functions in the Jenkinsfile.

   ```bash
    stage("build jar"){
             steps{
                 script{
                     buildJar()
                 }
              }
        }
   
  
         stage("build Image"){
                     steps{
                         script{
                             buildImage()
                         }
                     }
         }
   ```

   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/jenkinsFile%20To%20Call%20SharedLibrary.png" width=800 />  




### Integrating the Global library in the Jenkins pipeline
1. Open the Jenkins server, navigate to Manage Jenkins, and select System.
   
2. Scroll to Global Trusted Pipelines Libraries.
   
3. Add the name of the shared library and the default version.
   
4. Select Modern SCM as the Retrieval Method.
   
5. Select Git under Source Code Management.
   
6. Enter the Git repository URL for the shared library.
   
7. Add the credentials for Git access and click Save.
   
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/configuring%20sharedlibrary%20jenkins.png" width=800/>
   
8. Edit the Jenkinsfile in the java-maven-app repository to reference the shared library as shown below:
   'jenkins-shared-library'-> The name of the library defined in the Jenkins-->System 
   ```bash
   @Library('jenkins-shared-library')
   ```
   <details><summary><strong> 💡 Calling the Library  </strong></summary>
   Since the Groovy script is loaded using the global variable gv, an underscore (_) is not required at the end of @Library('jenkins-shared-library'). If the gv variable is not defined, you must use @Library('jenkins-shared-library')_ instead.
 
   </details>

9. Run the Pipeline



### Parameterizing the buildImage.groovy
Currently, the ImageName and Tag values are hardcoded in the Groovy files. To make them configurable, you must parameterize the buildImage function.
1. Update the buildImage.groovy: Add the ImageName parameter

    buildImage.groovy file:
   
   ```bash
      #!/user/bin/env groovy
   
      def call(String imageName) {
           echo "building the docker Image..."
           withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PWD', usernameVariable: 'USER')]) {
           sh """
                 cd java-maven-app/
                 docker build -t $imageName .
                 echo $PWD | docker login -u $USER --password-stdin
                 docker push $imageName
            """
           }
   } 
   ```
   
2. Modify the Jenkinsfile to pass the ImageName argument to the buildImage function.

       ```bash
        stage("build jar"){
          steps{
             script{
                 buildJar()
             }
           }
         }
       
         stage("build Image"){
           steps{
              script{
                 buildImage 'lala011/demo-app:jma-3.0'
               }
           }
         }
       ``` 



### Creating a Docker Class
1. Navigate to the shared Library repository under src/com/example and  create a Docker.groovy file.
   
2. Edit the file to import the com.example package.
   
   ```bash
   #!/user/bin/env groovy
   ```
   
3. Define the logic within the groovy class

   ```bash
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
   ```
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/DockerClass.png" width=800 />
   
4. Update the buildImage.groovy file in the vars folder to use the Docker Class.

      ```bash
          #!/user/bin/env groovy
    
          import com.example.Docker
          
          def call(String imageName) { 
              return new Docker(this).buildDockerImage(imageName)
          }   
      ```
      <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/buildImagegroovi%20with%20docker%20class.png" width=800/>
      
5. Split docker functions and create two new files in the Vars folder: dockerLogin.groovy and dockerPush.groovy<br>

      dockerLogin.groovy:
      ```bash
        #!/user/bin/env groovy        
        import com.example.Docker
        
        def call() {
            return new Docker(this).dockerLogin()
        }
      ```
      
      dockerPush.groovy:
      ```bash
        #!/user/bin/env groovy        
        import com.example.Docker
        
        def call(String imageName) {
            return new Docker(this).dockerPush(imageName)
        }
      ```
      
6. Update the Jenkinsfile in the java-maven-app repository to call the functions buildImage, dockerLogin, and dockerPush individually:

   ```bash
         #!/user/bin/env groovy
        @Library('jenkins-shared-library')
        def gv
        
        pipeline {   
            agent any
            tools{
                maven 'maven-3.9'
            }
        
            stages{ 
        
               stage("init"){
                     steps{
                         script{
                             gv = load "java-maven-app/script.groovy"    
                         }
                      }
                }
        
                  stage("build jar"){
                     steps{
                         script{
                             buildJar()
                         }
                      }
                }
        
                 stage("build and Push Image"){
                             steps{
                                 script{
                                     buildImage 'lala011/demo-app:jma-3.0'
                                     dockerLogin()
                                     dockerPush 'lala011/demo-app:jma-3.0'
                                 }
                             }
                 }
        
        
                stage("deploy") {
        
                     when {
                        expression{
                            BRANCH_NAME == "main"                    
                        }
                        
                    }
                    steps {
                        script {
                            gv.deployApp()
                        }
                    }
                }              
            }
        } 
   ```
7. Execute the Pipeline
      
8. Verify that the docker Image was successfully pushed to DockerHub

      <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/using%20name%20and%20tag%20as%20a%20parameter.PNG" width=800 />
  
  
  
### Integrating the shared library for a specific project
1. Open the Jenkins server, navigate to Manage Jenkins, and select System.
   
2. Scroll to Global Trusted Pipelines Libraries and delete the Global Trusted Pipelines Libraries details.
   
3. Update the Jenkinsfile in the maven-java-app (Shared library branch) and add the shared library reference at the beginning.
   
   ```bash
     library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
      [$class: 'GitSCMSource',
       remote: 'https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary.git',
       credentialsId: 'github-credentials2'])
   ```
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/Adding%20shared%20library%20but%20not%20a%20global%20library.PNG" width=800/>

4. Execute the pipeline only for this branch
   <img src="https://github.com/lala-la-flaca/DevOpsBootcamp_8_Jenkins_SharedLibrary/blob/main/Img/JenkinsShared%20library%20defined%20i%20jenkins%20file%20no%20global.PNG" width=800 />


## Integrating Jenkins Webhooks

This section covers how to configure **webhooks** to trigger Jenkins jobs on code changes. It provides detailed steps for integrating with **GitHub** and **GitLab**.
