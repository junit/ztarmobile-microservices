mvn -f ../ clean install -Dmaven.test.skip=true -Psolo;
cp ../target/ztarmobile-user-profile-service*.RELEASE.jar ../user-profile/

