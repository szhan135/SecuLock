/*
 * PIRtesting.c
 *
 * Created: 5/13/2020 5:19:57 PM
 * Author : aahun
 */ 

#include<avr/io.h>
#include <avr/interrupt.h>
#include "timer.h"
#include "bit.h"
//#define F_CPU 8000000UL
//#include<util/delay.h>

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
	}
}

int main() {
	DDRA=0x00; //configuring PortC pin 0 as input
	PORTA=0x00;
	DDRB=0x01; // configuring PortB as output
	PORTB=0x00; // buzzer/LED off
	
	TimerSet(50);
	TimerOn();
	
	while(1)
	{
		//if((PINA&(1<<0)))            // check for sensor pin PC.0 using bit
		//if((PINA&0x02) == 0x02)
		//{
		//	PORTB=0x01;           // buzzer /LED on
			//_delay_ms(1000);
			//PORTB=0x00;
		//}
		//else
		//PORTB=0x00;  // buzzer/LED off
		
		//if((PINA&0x01) == 0x01) {
		//	PORTB |= (1 << PB0);
		//}
		//else {
		//	PORTB &= ~(1 << PB0);
		//}
		
		//if(GetBit(PINA, 0) == 1) {
		//	PORTB |= (1 << PB0);
		//}
		//else {
		//	PORTB &= ~(1 << PB0);
		//}
		alarm();
		while (!TimerFlag){}
		TimerFlag = 0;	
	}
	
	return 0;
}
