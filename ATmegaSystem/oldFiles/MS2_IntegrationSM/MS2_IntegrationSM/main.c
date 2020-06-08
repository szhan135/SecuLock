/*
 * MS2_IntegrationSM.c
 *
 * Created: 5/13/2020 9:16:49 PM
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

void read_finger_1()  {        //for char_buffer1
	if(USART_IsSendReady(1)) {
		LCD_DisplayString(1, "Scanning........");
		LCD_DisplayString(17, "Reading finger 1");
		int i = 0;
		char k = 1, ch = 1;
		USART_Flush(1);
		
		USART_Send(239, 1);
		USART_Send(1, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(1, 1);
		USART_Send(0, 1);
		USART_Send(3, 1);
		USART_Send(1, 1);
		USART_Send(0, 1);
		USART_Send(5, 1);

		for(i = 0; i < 10; i++) {
			k = USART_Receive(1);
			if(i == 9) {
				ch = k;
				k = USART_Receive(1);
				k = USART_Receive(1);

				if(ch == 0x00) {
					//PORTA |= (1<<0);
					USART_Flush(1);
					k = 1;
					USART_Send(239, 1);
					USART_Send(1, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(1, 1);
					USART_Send(0, 1);
					USART_Send(4, 1);
					USART_Send(2, 1);
					USART_Send(1, 1);
					USART_Send(0, 1);
					USART_Send(8, 1);
					i = 0;

					for(i = 0; i < 10; i++) {
						k = USART_Receive(1);

						if(i == 9) {
							ch = k;
							k = USART_Receive(1);
							k = USART_Receive(1);

							if(ch == 0x00) {
								//PORTA |= (1<<1);
								
							}
						}
					}
				}
			}
		}
	}
	delay_ms(1000);
}

void read_finger_2() {         //for char_buffer2
	if(USART_IsSendReady(1)) {
		LCD_DisplayString(1, "Scanning........");
		LCD_DisplayString(17, "Reading finger 2");
		int i = 0;
		char k = 1, ch = 1;
		USART_Flush(1);
		
		USART_Send(239, 1);
		USART_Send(1, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(255, 1);
		USART_Send(1, 1);
		USART_Send(0, 1);
		USART_Send(3, 1);
		USART_Send(1, 1);
		USART_Send(0, 1);
		USART_Send(5, 1);

		for(i = 0; i < 10; i++) {
			k = USART_Receive(1);
			if(i == 9) {
				ch = k;
				k = USART_Receive(1);
				k = USART_Receive(1);
				if(ch == 0x00) {
					//PORTA |= (1<<2);
					k = 1;
					USART_Flush(1);
					USART_Send(239, 1);
					USART_Send(1, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(255, 1);
					USART_Send(1, 1);
					USART_Send(0, 1);
					USART_Send(4, 1);
					USART_Send(2, 1);
					USART_Send(2, 1);
					USART_Send(0, 1);
					USART_Send(9, 1);
					i = 0;
					for(i = 0; i < 10; i++) {
						k = USART_Receive(1);
						if(i == 9) {
							ch = k;
							k = USART_Receive(1);
							k = USART_Receive(1);
							if(ch == 0x00) {
								//PORTA |= (1<<3);
							}
						}
					}
				}
			}
		}
	}
	delay_ms(1000);
}


void make_template() {
	LCD_DisplayString(17, "Making template");
	int i = 0;
	char k = 1, ch = 1;
	USART_Flush(1);
	k = 1;
	USART_Send(239, 1);
	USART_Send(1, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(1, 1);
	USART_Send(0, 1);
	USART_Send(3, 1);
	USART_Send(5, 1);
	USART_Send(0, 1);
	USART_Send(9, 1);

	for(i = 0; i < 10; i++) {
		k = USART_Receive(1);

		if(i==9) {
			ch = k;
			k = USART_Receive(1);
			k = USART_Receive(1);

			if(ch == 0x00) {
				//PORTA |= (1<<4);
			}
		}
	}
	//delay_ms(100);
}


void check_finger() {
	LCD_DisplayString(1, "Checking finger");
	int i = 0;
	char k = 1, ch = 1;
	USART_Flush(1);

	USART_Send(239, 1);
	USART_Send(1, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(255, 1);
	USART_Send(1, 1);
	USART_Send(0, 1);
	USART_Send(8, 1);
	USART_Send(4, 1);
	USART_Send(1, 1);
	USART_Send(0, 1);
	USART_Send(0, 1);
	USART_Send(0, 1);
	USART_Send(10, 1);
	USART_Send(0, 1);
	USART_Send(24, 1);

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
				//PORTA |= (1<<5);
				//LCDdisplay("FINGER FOUND");
				//LCD_ClearScreen();
				LCD_DisplayString(1, "UNLOCKED        ");
				LCD_DisplayString(17, "Finger FOUND!!    ");
				LCD_DisplayString(28, k);
				PORTB |= (1 << PB2);
				} else {
				LCD_DisplayString(1, "                ");
				LCD_DisplayString(17, "Finger NOT Found   ");
			}
		}
	}
	delay_ms(1000);
}


enum States1 {init1, wait, scanRFID, nextState, readFinger1, readFinger2, makeTemplate, checkFinger} state1;
	
void unlock() {
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
			state1 = readFinger1;
			break;
		case readFinger1:
			state1 = readFinger2;
			break;
		case readFinger2:
			state1 = makeTemplate;
			break;
		case makeTemplate:
			state1 = checkFinger;
			break;
		case checkFinger:
			state1 = wait;
			break;	
		default:
			state1 = init1;
			break;
	}
	
	switch(state1) {
		case init1:
			PORTB &= ~(1 << PB1);
			PORTB &= ~(1 << PB2);
			break;
		case wait:
			PORTB &= ~(1 << PB1);
			PORTB &= ~(1 << PB2);
			LCD_ClearScreen();
			LCD_DisplayString(1, "Locked: Waiting");
			//delay_ms(100);
			break;
		case scanRFID:
			LCD_DisplayString(1, "Scan Card Twice");
			scan_RFID();
			//delay_ms(1000);
			break;
		case nextState:
			LCD_ClearScreen();
			LCD_DisplayString(1, "Place Finger");
			PORTB |= (1 << PB1);
			delay_ms(5000);
			break;
		case readFinger1:
			read_finger_1();
			delay_ms(1000);
			break;
		case readFinger2:
			read_finger_2();
			delay_ms(1000);
			break;
		case makeTemplate:
			make_template();
			delay_ms(500);
			break;
		case checkFinger:
			check_finger();
			delay_ms(10000);
			break;
		default:
			break;
	}
}

enum States2 {init2, alarmOff, alarmOn} state2;

void alarm() {
	switch(state2) {
		case init2:
			state2 = alarmOff;
			break;
		case alarmOff:
			if(GetBit(PINA, 0) == 1) {
				state2 = alarmOn;
			}
			else {
				state2 = alarmOff;
			}
			break;
		case alarmOn:
			if(GetBit(PINA, 0) == 1) {
				state2 = alarmOn;
			}
			else {
				state2 = alarmOff;
			}
			break;
		default:
			state2 = init2;
			break;
	}
	
	switch(state2) {
		case init2:
			PORTB &= ~(1 << PB0);
			break;
		case alarmOff:
			PORTB &= ~(1 << PB0);
			break;
		case alarmOn:
			PORTB |= (1 << PB0);
			break;
		default:
			PORTB &= ~(1 << PB0);
			break;
	}
}

int main(void) {
	DDRA = 0x00;	PORTA = 0xFE;
	DDRB = 0xFF;	PORTB = 0x00;
	DDRC = 0xFF;	PORTC = 0x00;
	DDRD = 0xFE;	PORTD = 0x01;
	
	TimerSet(50);
	TimerOn();
	
	state1 = init1;
	state2 = init2;
	
	LCD_init();
	LCD_ClearScreen();
	initUSART(0);
	initUSART(1);
	
    /* Replace with your application code */

    while (1) {
		
		unlock();
		alarm();
		
		while (!TimerFlag){}
		TimerFlag = 0;
    }
}

