# IOTEsp8266
Un exemple de capteur iot basé sur une puce ESP8266-12 avec le firmware nodemcu
- Lecture de la température et de l'humidité sur un bus i2c
- Connexion au broker mqtt et émission du message
- Docker Debian pour une mise en place des différents logiciel (mosquitto/nodered/grafana/influxdb)
- Docker RPi (nodered/grafana/influxdb) mosquitto hébergé chez CloudMQTT
- Une appli android pour publier le niveau de luminosité sur le broker