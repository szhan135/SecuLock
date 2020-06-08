/*
 * mockPi.c
 *
 * Created: 6/3/2020 6:45:47 PM
 * Author : aahun
 */ 


#include <avr/io.h>
#include "timer.h"
#include "bit.h"

#define SS       DDB4
#define MOSI     DDB5
#define MISO     DDB6
#define SCK      DDB7

unsigned char receivedData;
unsigned char sendData;

void SPI_MasterInit(void)
{
	/* Set MOSI and SCK output, all others input */
	DDRB = (1<<DDB5)|(1<<DDB7)|(1<<DDB4);
	/* Enable SPI, Master, set clock rate fck/16 */
	SPCR = (1<<SPE)|(1<<MSTR)|(1<<SPR0);
}

void SPI_MasterTransmit(unsigned char send) {
	/* Start transmission */
	SPDR = send;
}

char SPI_MasterReceive() {
	while(!(SPSR & (1<<SPIF)))
	;
	return SPDR;

}


int main(void)
{
	DDRA = 0xFF; PORTA = 0x00;
	DDRB = 0x00; PORTB = 0xFF;
	DDRC = 0xFF; PORTC = 0x00;
	DDRD = 0xFF; PORTD = 0xFF;
    /* Replace with your application code */
	
	TimerSet(50);
	TimerOn();
	
	//SPI_MasterInit();
	
	sendData = 0;
	receivedData = 0;
	
	//PORTD &= ~(1 << PD0); 
	//PORTD &= ~(1 << PD1);  
	//PORTD &= ~(1 << PD2);  
	//PORTD &= ~(1 << PD3);
	//PORTD &= ~(1 << PD4);     
	while (1) 
    {
		if(GetBit(PINB, 0) == 0) {
			sendData = 1;
			PORTD |= (1 << PD4);
		}
		else {
			sendData = 0;
			PORTD &= ~(1 << PD4);	
		}
		/*
		SPI_MasterTransmit(sendData);
		receivedData = SPI_MasterReceive();
		if(receivedData == 1) {
			PORTD |= (1 << PD0);
			PORTD &= ~(1 << PD1);
			PORTD &= ~(1 << PD2);
			PORTD &= ~(1 << PD3);
			PORTD &= ~(1 << PD4);
		}
		else if(receivedData == 2) {
			PORTD |= (1 << PD0);
			PORTD |= (1 << PD1);
			PORTD &= ~(1 << PD2);
			PORTD &= ~(1 << PD3);
			PORTD &= ~(1 << PD4);
		}
		else if(receivedData == 3) {
			PORTD |= (1 << PD0);
			PORTD |= (1 << PD1);
			PORTD |= (1 << PD2);
			PORTD &= ~(1 << PD3);
			PORTD &= ~(1 << PD4);
		}
		else if(receivedData == 4) {
			PORTD &= ~(1 << PD0);
			PORTD &= ~(1 << PD1);
			PORTD &= ~(1 << PD2);
			PORTD |= (1 << PD3);
			PORTD &= ~(1 << PD4);
		}
		else if(receivedData == 5) {
			PORTD &= ~(1 << PD0);
			PORTD &= ~(1 << PD1);
			PORTD &= ~(1 << PD2);
			PORTD |= (1 << PD3);
			PORTD |= (1 << PD4);
		}
		else {
			PORTD &= ~(1 << PD0);
			PORTD &= ~(1 << PD1);
			PORTD &= ~(1 << PD2);
			PORTD &= ~(1 << PD3);
			PORTD &= ~(1 << PD4);
		}
		*/
		
		while (!TimerFlag){}
		TimerFlag = 0;
    }
}


