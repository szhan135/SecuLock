from gpiozero import LED


#atmegaOut = LED(17)
val = input("Enter 1 or 2: ")
#print (val)

if(val == "1"):
#       atmegaOut.on()
        print("output high")
elif(val == "2"):
#       atmegaOut.off()
        print("output low")
