#!/user/bin/env groovy

def call() {
    echo "building the application for branch $BRANCH_NAME"
    dir('java-maven-app') {
          sh 'mvn package'
          }
}
