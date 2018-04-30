# Version: 0.0.1
FROM jenkins/jenkins:lts
LABEL org.thenuclei.creator="Brian Provenzano" \
      org.thenuclei.email="bproven@example.com"
USER root
RUN echo "deb http://deb.debian.org/debian testing main" >> /etc/apt/sources.list
RUN apt-get update && apt-get install -y \
python3.6 \
python3-pip \
golang \
&& rm -fr /var/lib/apt/lists/*
RUN sed -i '$ d' /etc/apt/sources.list
RUN pip3 install requests flask pytest pytest-runner
# ENV PATH="$PATH:/root/.local/bin"
# RUN echo $PATH
# RUN echo $PYTHON_BIN_PATH
RUN echo "America/Los_Angeles" | tee /etc/timezone
RUN dpkg-reconfigure --frontend noninteractive tzdata
ADD hashicorp-get /bin/hashicorp-get
RUN chmod +x /bin/hashicorp-get
RUN hashicorp-get terraform latest /bin/ -y -q && hashicorp-get packer latest /bin/ -y -q
COPY --chown=jenkins:jenkins basic-security.groovy /var/jenkins_home/init.groovy.d/basic-security.groovy
#COPY --chown=jenkins:jenkins jenkins.install.UpgradeWizard.state /var/jenkins_home/
RUN echo 2 > /var/jenkins_home/jenkins.install.UpgradeWizard.state
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
USER jenkins