j:
cd J:\Dockers\IOTCloudMQTT
docker-machine start IOTCloudMQTT
docker-machine env IOTCloudMQTT
docker-machine env --shell cmd IOTCloudMQTT
@FOR /f "tokens=*" %%i IN ('docker-machine env --shell cmd IOTCloudMQTT') DO @%%i
docker-machine ssh IOTCloudMQTT ln -s /mnt/hgfs/IOTCloudMQTT /home/docker/
docker-compose up -d
docker-machine ip IOTCloudMQTT