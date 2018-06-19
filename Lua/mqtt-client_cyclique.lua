-- MQTT
equip = "sensor_01"
dsleepTime = 120

MQTT_CLIENTID = equip
MQTT_HOST = "10.3.141.1"
MQTT_PORT = 1883

mqtt = mqtt.Client(MQTT_CLIENTID, 120, "", "")

temp = nil
hum = nil

function round(num, idp)
  local mult = 10^(idp or 0)
  return math.floor(num * mult + 0.5) / mult
end

function get_sensor()
	require('htu21d')
	sda=3 --GPIO0
	scl=4 --GPIO2
	htu21d:init(sda, scl)
	if htu21d:find_dev() == true then
		temp = round(htu21d:readTemp(), 2)
		hum = round(htu21d:readHum(), 2)
		print(equip .. " - SENSOR : Temperature - " .. temp .. "°C, Humidite - " .. hum .."%")
		return true
	else
		print("Device not found")
		return false
	end
end

mqtt:connect(MQTT_HOST, MQTT_PORT, 0, function(conn)
	print("\n" .. equip .. " - MQTT Connect : " .. MQTT_HOST .. ":" .. MQTT_PORT)
	tmr.alarm(2, 10000, tmr.ALARM_AUTO, function(conn)
		if get_sensor() == true then
			topic = "sensors/" .. equip
			message = "{\"id\":\"" .. equip .. "\",\"params\":{\"temperature\":".. temp ..", \"humidite\":" .. hum .. "}}"
			mqtt:publish(topic, message, 0, 1, function(conn)
				print(equip .. " - MQTT Publish : " .. topic .. ", " .. message)
			end)
		else
			topic = "sensors/" .. equip
			message = "{\"id\":\"sensor_01\",\"params\":{\"temperature\":25.9, \"humidite\":12}}"
			mqtt:publish(topic, message, 0, 1, function(conn)
				print(equip .. " - MQTT Publish : " .. topic .. ", " .. message)
			end)
		end
	end)
end)