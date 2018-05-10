# Custom Docker Container For Jenkins
Custom Docker image that utilizes jenkins/jenkins:lts as the base image 

## Details
Contains a custom docker image for jenkins with default plugins preconfigured, hardened instance configuration and a default admin user with matrix security setup.  Also includes env for python, golang, hashicorp IaaC tooling (terraform, packer)

 - Utilizes my custom Python hashicorp-get utility located [here](https://github.com/brian-provenzano/hashicorp-get)
