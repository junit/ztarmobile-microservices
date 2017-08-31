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

spring init -d=thymeleaf,actuator,jdbc,mail,mysql -g=com.ztarmobile -a=ztarmobile-payment-transaction-service -v=1.0.0.RELEASE --package-name=com.ztarmobile.notification -name="Ztar Usage Notification MicroService V1" -description="Usage Notification" -x --force
