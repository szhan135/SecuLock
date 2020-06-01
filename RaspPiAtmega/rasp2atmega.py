#from gpiozero import LED, Button
import spidev
import time

def createSPI(device):
	spi = spidev.SpiDev()
	spi.open(0, device)
	spi.max_speed_hz = 1000000
	spi.mode = 0
	return spi

#atmegaOut = LED(2)
#atmegaOut2 = LED(4)
#atmegaIn  = Button(3)
send = [3]

if __name__ == '__main__' :
	try:
		spi0 = createSPI(0)

		while 1 == 1:
			recieve = spi0.xfer(send)
			send = [3]
			print (recieve)
			if recieve == [0]:
				print ("System is Initilaizing")
				#val = input("System is Locked")
				#if(val == "1"):
					#atmegaOut.on()
					#print("Begin Unlocking")
				#elif(val == "2"):
					#atmegaOut2.on()
					#print("output low")
			elif recieve == [1] :
				print ("System is Locked")
			elif recieve == [2]:
				print ("Waiting for RFID card")
			elif recieve == [3]:
				print ("Waiting for Fingerprint")
			elif recieve == [4]:
				print ("System is Unlocked")

			else:
				print ("ERROR")
				#val = input("System is Unlocked")
				#if(val == "2"):
					#atmegaOut2.on()
					#print("Locking System")
			time.sleep(.5)
			#atmegaOut.off()
			#atmegaOut2.off()
			#time.sleep(5)
	except KeyboardInterrupt:
		spi0.close()
		exit()
