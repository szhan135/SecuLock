from gpiozero import LED, Button
import time


atmegaOut = LED(2)
atmegaOut2 = LED(4)
atmegaIn  = Button(3)
while 1 == 1:
	print (atmegaIn.value)
	if atmegaIn.value == 1:
		val = input("System is Locked")
		if(val == "1"):
			atmegaOut.on()
			print("Begin Unlocking")
		#elif(val == "2"):
			#atmegaOut2.on()
			#print("output low")
	else:
		val = input("System is Unlocked")
		if(val == "2"):
			atmegaOut2.on()
			print("Locking System")
	time.sleep(.5)
	atmegaOut.off()
	atmegaOut2.off()
	time.sleep(5)

