#!groovy​
pipeline {

    agent none

    environment{
        MAJOR_VERSION = 1
    }

    options {
        buildDiscarder(logRotator(numToKeepStr:'5', artifactNumToKeepStr: '3'))
    }

 //TODO - TF stages; get source for this infra (pull)
 //Build the environment
 //packer image with caddy installed and configured via ansible
 //Deploy image


    stages{
         stage('CHECK-UPDATE - Check/Update IaaC tools'){
            agent any
            steps{
                script{
                    //def tfexit = sh returnStatus: true, script: 'terraform --version'
                    //echo "return code TF: ${tfexit}"
                    def tfout = sh returnStdout: true, script: 'terraform --version'
                    echo "tf output: ${tfout}"
                }
            }
        }

        stage('GET LATEST - Get Latest From Caddy Repo'){
            agent any
            environment{
                GOPATH="${env.WORKSPACE + '/go'}"
            }
            
            steps{
                echo "Go Get Caddy code"
                sh "go get -u github.com/mholt/caddy/caddy"  
                sh "go get -u github.com/caddyserver/builds"
            }
        }

        stage('BUILD - Build Caddy Web Server'){
            agent any
            environment{
                GOPATH="${env.WORKSPACE + '/go'}"
            }
            steps{
                echo "Build Caddy web server binary"
                //echo "gopath ${env.GOPATH}"
                //echo "path ${env.PATH}"
                sh 'cd "${GOPATH}"/src/github.com/mholt/caddy/caddy/ && go build && ls -alh | grep "caddy"'
            }
        }

        // stage('GIT INFORMATION'){
        //     agent any
        //     steps{
        //         echo "My branch name: ${env.BRANCH_NAME}"
        //         script {
        //             def myLib = new thenuclei.git.gitStuff();
        //             echo "My Commit: ${myLib.gitCommit("${env.WORKSPACE}/.git")}"
        //         }
        //     }
        // }
        // stage('UNIT TESTS'){
        //     agent {
        //         label 'apache'
        //     }
        //     steps {
        //         sh 'ant -f test.xml -v'
        //         junit 'reports/result.xml'
        //     }
        // }
        // stage('BUILD'){
        //     agent {
        //         label 'apache'
        //     }
        //     steps {
        //         sh 'ant -f build.xml -v'
        //     }
        //     post{
        //         success {
        //             archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true 
        //         }
        //     }
        // }
        // stage('DEPLOY'){
        //     agent {
        //         label 'apache'
        //     }
        //     steps {
        //         sh "if ![ -d '/var/www/html/rectangles/all/${env.BRANCH_NAME}' ]; then mkdir /var/www/html/rectangles/all/${env.BRANCH_NAME}; fi"
        //         sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}"
        //     }
        // }
        // stage('CentOS RUN'){
        //     agent {
        //         label 'CentOS'
        //     }
        //     steps{
        //         sh "wget http://jenkins.thenuclei.org/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        //         sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
        //     }
        // }
        // stage('Debian/Ubuntu RUN'){
        //     agent {
        //         docker 'openjdk:8u141-jre'
        //     }
        //      steps{
        //         sh "wget http://jenkins.thenuclei.org/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        //         sh "/usr/bin/java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
        //     }
        // }
        // stage('PROMOTE TO GREEN'){
        //     agent {
        //         label 'apache'
        //     }
        //     when {
        //         branch 'master'
        //     }
        //     steps {
        //         sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        //     }
        // }
        // stage('PROMOTE DEV BRANCH TO MASTER'){
        //     agent {
        //         label 'apache'
        //     }
        //     when {
        //         branch 'development'
        //     }
        //     steps {
        //         echo "Stashing any local changes"
        //         sh "git stash"
        //         echo "Checking out development"
        //         sh "git checkout development"
        //         echo "Checking out the master"
        //         sh "git pull origin"
        //         sh "git checkout master"
        //         echo "Merging dev into master"
        //         sh "git merge development"
        //         echo "Pushing to origin master"
        //         sh "git push origin master"
        //         echo "Tagging the release"
        //         sh "git tag rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        //         sh "git push origin rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        //     }
        //     post{
        //         success {
        //             emailext(
        //                 subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] Development Promoted To Master!",
        //                 body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Development Promoted To Master!":</p>
        //                 <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        //                 to: "bproven@gmail.com"
        //             )
        //         }
        //     }
        // }
    }
    //post{
      //  failure {
        //    emailext(
        //        subject: "${env.JOB_NAME} [${env.BUILD_NUMBER}] failed!",
        //        body: """<p>'${env.JOB_NAME} [${env.BUILD_NUMBER}]' Failed!":</p>
        //        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        //        to: "bproven@gmail.com"
        //    )
        //}
    //}
}