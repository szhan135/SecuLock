#include <SPI.h>

const int buttonPin = 2;     // the number of the pushbutton pin
int buttonState = 0;

void setup (void)
{

  digitalWrite(SS, HIGH);  // ensure SS stays high for now

  // Put SCK, MOSI, SS pins into output mode
  // also put SCK, MOSI into LOW state, and SS into HIGH state.
  // Then put SPI hardware into Master mode and turn SPI on
  SPI.begin ();

  // Slow down the master a bit
  SPI.setClockDivider(SPI_CLOCK_DIV8);

  // initialize the pushbutton pin as an input:
  pinMode(buttonPin, INPUT);

}  // end of setup


void loop (void)
{

  char c;
  
  // read the state of the pushbutton value:
  buttonState = digitalRead(buttonPin);
  
  // enable Slave Select
  digitalWrite(SS, LOW);    // SS is pin 10

  // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
  if (buttonState == LOW) {
    c = 1;
    SPI.transfer (c);
  } else {
    c = 0;
    SPI.transfer (c);
  }
    SPI.transfer (c);

  // disable Slave Select
  digitalWrite(SS, HIGH);

  delay (50);  // 50 ms delay
}  // end of loop
