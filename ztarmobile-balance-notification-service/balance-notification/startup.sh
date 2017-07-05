nohup java -Dspring.datasource-cdrs.username=xxxxx \
           -Dspring.datasource-cdrs.password=xxxxx \
           -Dspring.datasource-ztar.username=xxxxx \
           -Dspring.datasource-ztar.password=xxxxx \
           -jar ztarmobile-balance-notification-service-1.0.0.RELEASE.jar > log/ztarmobile-balance-notification-service.log &
