mvn -f ../ clean install -Dmaven.test.skip=true -Psolo;
cp ../target/ztarmobile-balance-notification-service*.RELEASE.jar ../balance-notification/
