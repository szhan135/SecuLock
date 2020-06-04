from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import json
import spidev
import time
from os import system


LOCK = 0
UNLOCK = 0

def customCallback(client, userdata, message):
	print (message.payload)
	if message.payload == "Lock":
		global LOCK
		LOCK = 1
	elif message.payload == "Unlock" :
		global UNLOCK
		UNLOCK = 1




def createSPI(device):
	spi = spidev.SpiDev()
	spi.open(0, device)
	spi.max_speed_hz = 1000000
	spi.mode = 0
	return spi

myMQTTClient = AWSIoTMQTTClient("basicPubSub")
myMQTTClient.configureEndpoint("a295j2x23de6tu-ats.iot.us-west-2.amazonaws.com", 8883)
#myMQTTClient.configureCredentials("/home/pi/IOT2/IOT_TEST/root-CA.crt","/home/pi/IOT2/IOT_TEST/pi.private.key","/home/pi/IOT2/IOT_TEST/pi.cert.pem")
myMQTTClient.configureCredentials("root-CA.crt","pi.private.key","pi.cert.pem")
myMQTTClient.configureOfflinePublishQueueing(-1)
myMQTTClient.configureDrainingFrequency(2)
myMQTTClient.configureConnectDisconnectTimeout(30)
myMQTTClient.configureMQTTOperationTimeout(5)


myMQTTClient.connect()
print ("Connected")
myMQTTClient.subscribe("topic_1", 1, customCallback)
print ("continue")
time.sleep(2)



send = [3]

if __name__ == '__main__' :
	try:
		spi0 = createSPI(0)

		while 1 == 1:
			recieve = spi0.xfer(send)
			send = [3]
			#_ = system('clear')
			print (recieve)
			if recieve == [0]:
				print ("System is Initilaizing")
			elif recieve == [1] :
				print ("System is Locked")
				message = {}
				message['message'] = "Locked"
				messageJson = json.dumps(message)
				myMQTTClient.publish("topic_2", messageJson, 1)
				if UNLOCK == 1:
					send = [1]
					LOCK = 0
					print ("Begin Unlocking")
			elif recieve == [2]:
				print ("Waiting for RFID card")
				message = {}
				message['message'] = "Waiting for RFID"
				messageJson = json.dumps(message)
				myMQTTClient.publish("topic_2", messageJson, 1)
			elif recieve == [3]:
				print ("Waiting for Fingerprint")
				message = {}
				message['message'] = "Waiting for Fingerprint"
				messageJson = json.dumps(message)
				myMQTTClient.publish("topic_2", messageJson, 1)
			elif recieve == [4]:
				print ("System is Unlocked")
				message = {}
				message['message'] = "UnLocked"
				messageJson = json.dumps(message)
				myMQTTClient.publish("topic_2", messageJson, 1)
				#print (LOCK)
				if LOCK == 1:
					print ("Locking the system")
					send = [2]
					UNLOCK = 0
			else:
				print ("ERROR")
			time.sleep(.05)
	except KeyboardInterrupt:
		spi0.close()
		exit()
