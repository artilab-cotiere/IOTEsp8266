wifi.setmode(wifi.STATION)
wifi.sta.config("Rpi_IOT","IOT_Pass")
wifi.sta.connect()
print("Rpi_IOT - Attente connection")

tmr.alarm(1, 1000, 1, function()
	if wifi.sta.getip()== nil then
		-- print("IP unavaiable, Waiting...")
	else
		print("Rpi_IOT - Connection établie, ip : " .. wifi.sta.getip())
		tmr.stop(1)
		--dofile("mqtt-client_deepSleep.lua")
		dofile("mqtt-client_cyclique.lua")
	end
end)