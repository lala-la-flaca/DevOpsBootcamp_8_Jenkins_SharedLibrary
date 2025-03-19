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
- <b>Groovy: Language for pipeline scripting and shared libraries.


## 🎯 Features

- <b>Create a separate Git repository for Jenkins Shared Library project.</b>
- <b>Create functions in the Jenkins Shared Library to use in the Jenkins pipeline.</b>
- <b>Integrate the Global library in jenkins pipeline</b>
- <b>Integrate the shared lifrary for a specific project:</b>
  

## 📝 Prerequisites
- <b>Ensure that the Jenkins is running.</b>


## 🏗 Project Architecture

<img src=""/>


## ⚙️ Project Configuration:

### Creating a Git repository for the Jenkins Shared Library project.
1. Go to your Github Account and create a new repository.

### Creating functions in the Jenkins Shared Library to use in the Jenkins pipeline.
1. Go to IntelliJ and create a new Groovy project, called jenkins-shared-library.
2. Create a New folder called Vars.
3. Create a new groovy file called buildJar.groovy. Each file inside the Vars folder contains a function to be executed by the Jenkinsfile and the name of the file must match the function we are going to call from Jenkinsfile.
4. The buildJar.groovy contains the shebang and the call function, as follows:

   ```bash
   #!/user/bin/env groovy

     def call() {
       echo "building the application for branch $BRANCH_NAME"
       dir('java-maven-app') {
                sh 'mvn package'
       }
   }
   ```
5. We create a different file for each function we want to reuse using the shared library.
   buildImage.groovy file:
   ```bash
      #!/user/bin/env groovy
   
      def call(String imageName) {
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
   ```
6. Create a new branch for the java-maven-app repository called jenkins-shared-library.
7. Edit the JenkinsFile from the java-maven-app to call the buildJar.groovy and buildImage.groovy,  as follows:

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


### Integrating the Global library in the Jenkins pipeline
1. Open the Jenkins server, navigate to Manage Jenkins, and select System.
2. Go to the Global Trusted Pipelines Libraries
3. Add the name of the shared library and the default version.
4. Select the Modern SCM as the Retrieval Method.
5. Add the Source Code Management and select Git.
6. On the project repository add the link to the git shared library repository.
7. Add the credentials to access git and save.
8. Edit the Jenkinsfile in the java-maven-app repository and called the shared library as follows:
   'jenkins-shared-library'-> The name of the library defined in the Jenkins-->System 
   ```bash
   @Library('jenkins-shared-library')
   ```
   <details><summary><strong> 💡 Calling the Library  </strong></summary>
    as we are using a gloval variable called gv which loads the groovy script, we do not need to add an _ at the end of the @Library('jenkins-shared-library'), in case that the global variable gv is not defined then we must use @Library('jenkins-shared-library')_ instead
   </details>


### Enabling Docker in Jenkins


### Creating Jenkins Credentials to Access Git Using a Job




### Adding or Modifying Credentials in Jenkins Security Settings
The credentials can also be added or modified from the Security section under Credentials as follows:




### Creating a Freestyle Job for a Java Maven Application


### Pushing image to Docker Nexus Repository




### Creating a Pipeline Job for a Java Maven Application 

   
   <details><summary><strong> ❌ Issue  </strong></summary>
     <ul>
        <li>The shell session does not change the directory for the subsequent commands.</li>
        <li>The Docker login fails with variables because of incorrect string interpolation.</li>
    </ul>
   </details>
   



### Creating a Mutibranch Job for a Java Maven Application

