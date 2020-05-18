/*
 * testing.c
 *
 * Created: 4/22/2020 12:43:56 PM
 * Author : aahun
 */ 

#include <avr/io.h>
#include <string.h>
#include "timer.h"
#include "usart_ATmega1284.h"
#include "lcd.h"
#include <util/delay.h>

/*
#define PIRinput PINA0
#define PIRoutput PORTB;

void read_finger_1()  {        //for char_buffer1
	int i = 0;
	char k = 1, ch = 1;
	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 3);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 5);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);
		if(i == 9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);

			if(ch == 0x00) {
				PORTB |= (1<<0);
				k = 1;
				USART_Send(1, 239);
				USART_Send(1, 1);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 1);
				USART_Send(1, 0);
				USART_Send(1, 4);
				USART_Send(1, 2);
				USART_Send(1, 1);
				USART_Send(1, 0);
				USART_Send(1, 8);
				i=0;

				for(i = 0; i < 10; i++) {
					k = USART_Receive(1);

					if(i == 9) {
						ch = k;
						k = USART_Receive(1);
						k = USART_Receive(1);

						if(ch == 0x00) {
							PORTB |= (1<<1);
						}
					}
				}
			}
		}
	}
}

void read_finger_2() {         //for char_buffer2
	int i = 0;
	char k = 1, ch = 1;
	
	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 3);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 5);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);
		if(i == 9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);
			if(ch == 0x00) {
				PORTB |= (1<<2);
				k = 1;
				USART_Send(1, 239);
				USART_Send(1, 1);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 255);
				USART_Send(1, 1);
				USART_Send(1, 0);
				USART_Send(1, 4);
				USART_Send(1, 2);
				USART_Send(1, 2);
				USART_Send(1, 0);
				USART_Send(1, 9);
				i = 0;
				for(i = 0; i < 10; i++) {
					k = USART_Receive(1); 
					if(i == 9) {
						ch = k;
						k = USART_Receive(1);
						k = USART_Receive(1);
						if(ch == 0x00) {
							PORTB |= (1<<3);
						}
					}
				}
			}
		}
	}
}


void make_template() {
	int i = 0;
	char k = 1, ch = 1;

	k = 1;
	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 3);
	USART_Send(1, 5);
	USART_Send(1, 0);
	USART_Send(1, 9);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);

		if(i==9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);

			if(ch == 0x00)
			PORTB |= (1<<4);
		}
	}
}


void check_finger() {
	int i = 0;
	char k = 1, ch = 1;

	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 8);
	USART_Send(1, 4);
	USART_Send(1, 1);
	USART_Send(1, 0); 
	USART_Send(1, 0);
	USART_Send(1, 0);
	USART_Send(1, 10);
	USART_Send(1, 0);
	USART_Send(1, 24);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);

		if(i == 9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);
			k = USART_Receive(1);
			k = USART_Receive(1);
			k = USART_Receive(1);
			k = USART_Receive(1);
			//LCDclr();

			if(ch == 0x00) {
				PORTB |= (1<<5);
				//LCDdisplay("FINGER FOUND");
				LCD_DisplayString(17, "Finger Found");
			} else {
				LCD_DisplayString(17, "Finger NOT Found");
			}
		}
	}
}

void store(int ID) {
	int i = 0, sum = 14 + ID;
	char k = 1, ch = 1;

	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 6);
	USART_Send(1, 6);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, ID);
	USART_Send(1, 0);//C
	USART_Send(1, sum);//C

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);
		
		if(i == 9) {
			ch = k;
			k=USART_Receive(1);
			k=USART_Receive(1);
			if(ch == 0x00)
			PORTB |= (1<<6);
		}
	}
}

void empty() {
	int i = 0;
	char k = 1, ch = 1;
	USART_Send(1, 239);
	USART_Send(1, 1);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 255);
	USART_Send(1, 1);
	USART_Send(1, 0);
	USART_Send(1, 3);
	USART_Send(1, 13);
	USART_Send(1, 0);
	USART_Send(1, 17);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);

		if(i == 9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);

			if(ch==0x00)
			PORTB |= (1<<7);
		}
	}
}

*/

int main(void)
{
	
	DDRA = 0x00;	PORTA = 0xFF;
	DDRB = 0xFF;	PORTB = 0x00;
	DDRC = 0xFF;	PORTC = 0x00;
	DDRD = 0xFE;	PORTD = 0x01;
	
	LCD_init();
	LCD_ClearScreen();
	initUSART(0);
	//initUSART(1);
	
	//RFID variables
	char RFID[16];
	memset(RFID, 0, 16);
	char outID[9];
	memset(outID, 0, 9);

	
    /* Replace with your application code */
    while (1) 
    {
		_delay_ms(2000);
		LCD_ClearScreen();
		
		/*
		// PIR sensor
		if (PORTA == 1) {
			DDRB = 1;
		}
		else {
			DDRB = 0;
		}
		*/
		
		
		// RFID scanner
		for (int i = 0; i < 12; i++) {
			RFID[i] = USART_Receive(0);
		}
		
		for (int i = 0; i < 10; i++) {
			outID[i] = RFID[i];
		}
		_delay_us(10);
		
		LCD_DisplayString(0, outID);
		
		
		
		/*
		
		// Fingerprint scanner
		LCD_DisplayString(17, "Scanning.....");
		read_finger_1();  //scans and stores in char_buffer1
		read_finger_2();  //scans and stores in char_buffer2
		make_template();  //makes the template with info in char_buffer1 & char_buffer2 and stores it in char_buffer1
		check_finger();   //checks for the finger authentication
		//    store(0);   //stores the scanned value to the given parametric location in flash library
		//    empty();    //empties the flash library
		*/
    }
}

