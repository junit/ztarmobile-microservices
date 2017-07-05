mvn -f ../ clean install -Dmaven.test.skip=true -Pdenali;
cp ../target/ztarmobile-balance-notification-service*.RELEASE.jar ../balance-notification/
