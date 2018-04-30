
build:
	@docker build -t warpigg/jenkins-lts:$(version) .
run:
	@docker run -p 8080:8080 --name=jenkins-lts-master -d -v jenkins_home_notalpine:/var/jenkins_home warpigg/jenkins-lts:$(version)
start:
	@docker container start jenkins-lts-master
stop:
	@docker container stop jenkins-lts-master
show:
	@docker container ls
logs: 
	@docker logs jenkins-lts-master
cli:
	@docker container exec -it -u root jenkins-lts-master /bin/bash
prune:
	@docker volume prune
clean:	stop
	@docker container rm jenkins-lts-master
cleanall: clean prune
