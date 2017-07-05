mvn -f ../ clean install -Dmaven.test.skip=true -Pprod;
cp ../target/ztarmobile-balance-notification-service*.RELEASE.jar ../balance-notification/
