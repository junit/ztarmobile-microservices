mvn -f ../ clean install -Dmaven.test.skip=true -Pprod;
cp ../target/ztarmobile-user-profile-service*.RELEASE.jar ../user-profile/

