/*
 * ms2_RFID_SM.c
 *
 * Created: 5/13/2020 5:56:17 PM
 * Author : aahun
 */ 

#include <avr/io.h>
#include <string.h>
#include "timer.h"
#include "usart_ATmega1284.h"
#include "lcd.h"
#include "bit.h"
#include <util/delay.h>

char RFID[16];
char outID[11];
char card0[11] = {"0015807557"};
char card1[11] = {"0008731353"};


void scan_RFID() {
	for(int i = 0; i < 16; i++) {
		RFID[i] = 0;
	}
	
	for(int i = 0; i < 10; i++) {
		outID[i] = 0;
	}
	
	USART_Flush(0);
	// RFID scanner
	for (int i = 0; i < 16; i++) {
		RFID[i] = USART_Receive(0);
	}
	
	for (int i = 0; i < 10; i++) {
		outID[i] = RFID[i];
	}
	
	LCD_DisplayString(17, outID);
}

enum States1 {init1, wait, scanRFID, nextState} state1;
	
void rfid() {
	switch(state1) {
		case init1:
			state1 = wait;
			break;
		case wait:
			if(GetBit(PINA, 1) == 0) {
				state1 = scanRFID;
			}
			else {
				state1 = wait;
			}
			break;
		case scanRFID:
			if(strcmp(outID, card0) == 0 || strcmp(outID, card1) == 0 ) {
				state1 = nextState;
			}
			else {
				state1 = scanRFID;
			}
			break;
		case nextState:
			state1 = wait;
			break;
		default:
			state1 = init1;
			break;
	}
	
	switch(state1) {
		case init1:
			PORTB &= ~(1 << PB1);
			break;
		case wait:
			PORTB &= ~(1 << PB1);
			LCD_ClearScreen();
			LCD_DisplayString(1, "Waiting");
			//delay_ms(100);
			break;
		case scanRFID:
			LCD_DisplayString(1, "Scan Card Twice");
			scan_RFID();
			//delay_ms(1000);
			break;
		case nextState:
			LCD_ClearScreen();
			LCD_DisplayString(1, "Next Step");
			PORTB |= (1 << PB1);
			delay_ms(5000);
			break;
		default:
			break;
	}
}

int main(void)
{
	
	DDRA = 0x00;	PORTA = 0xFE;
	DDRB = 0xFF;	PORTB = 0x00;
	DDRC = 0xFF;	PORTC = 0x00;
	DDRD = 0xFE;	PORTD = 0x01;
	
	TimerSet(50);
	TimerOn();
	
	state1 = init1;
	
	LCD_init();
	LCD_ClearScreen();
	initUSART(0);
	//initUSART(1);
	
    /* Replace with your application code */
	int lock = 0;
    while (1) 
    {
		
		//_delay_ms(10);
		
		//if(GetBit(PINA, 1) == 0) {
		//	lock = 1;
		//}
		//if(lock == 1) {
			// Fingerprint scanner
			//alarm();
			//_delay_ms(1000);
			rfid();
			//read_finger_1();  //scans and stores in char_buffer1
			//read_finger_2();  //scans and stores in char_buffer2
			//make_template();  //makes the template with info in char_buffer1 & char_buffer2 and stores it in char_buffer1
			//check_finger();   //checks for the finger authentication
			//    store(0);   //stores the scanned value to the given parametric location in flash library
			//    empty();    //empties the flash library
		//	lock = 0;
		//}
		//delay_ms(1000);
		//LCD_ClearScreen();
		
		
		
		while (!TimerFlag){}
		TimerFlag = 0;
    }
}