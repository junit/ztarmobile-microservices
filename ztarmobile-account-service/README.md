# Installing Spring Boot CL
---
$ curl -s get.sdkman.io | bash
After it finishes, you can execute the following line to run the sdk command:

$ source "$HOME/.sdkman/bin/sdkman-init.sh"
Then make sure that the sdk command is working by executing this line:

$ sdk version
SDKMAN 3.2.4
Next, it’s time to install the Spring Boot CLI, which you do by executing this command:

$ sdk install springboot
$ spring --version

spring init -d=web,thymeleaf,data-rest,actuator,jdbc,mail,mysql,data-rest-hal,data-jpa -g=com.ztarmobile -a=ztarmobile-account-service -v=3.0.0.RELEASE --package-name=com.ztarmobile.account -name="Ztar Account Management MicroService V3" -description="Account Management" -x --force
