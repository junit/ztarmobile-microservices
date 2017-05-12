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

spring init -d=web,thymeleaf,data-rest,actuator,jdbc,mail,activemq,mysql,data-rest-hal,data-jpa,security -g=com.ztarmobile -a=ztarmobile-invoicing-service -v=2.0.0.RELEASE --package-name=com.ztarmobile.invoicing -name="Ztar Invoicing MicroService V2" -description="Latest version of Ztar Invoicing MicroService" -x --force
