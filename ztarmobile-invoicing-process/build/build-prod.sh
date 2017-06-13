mvn -f ../ clean install -Dmaven.test.skip=true -Pprod;
cp ../target/ztarmobile-invoicing-process*.RELEASE.jar ../invoicing/

