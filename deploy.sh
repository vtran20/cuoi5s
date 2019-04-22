sudo /usr/local/tomcat7-01/bin/shutdown.sh

sudo cp /usr/local/sites/cuoi5s/target/cuoi5s.war /usr/local/tomcat7-01/webapps/ROOT.war
sudo rm -fr /usr/local/tomcat7-01/webapps/ROOT
sudo rm -fr /usr/local/tomcat7-01/work
sudo rm -fr /usr/local/tomcat7-01/temp

sleep 10

sudo /usr/local/tomcat7-01/bin/startup.sh

