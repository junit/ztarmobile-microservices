mvn -f ../ clean install -Dmaven.test.skip=true -Psolo;
cp ../target/ztarmobile-invoicing-process-1.1-SNAPSHOT.jar ../invoicing/
