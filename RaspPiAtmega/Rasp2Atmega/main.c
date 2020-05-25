/*
 * Rasp2Atmega.c
 *
 * Created: 4/22/2020 6:12:20 PM
 * Author : Marios
 */ 

#include <avr/io.h>
#include "bit.h"
#include "ADC.h"
#include <avr/interrupt.h>
#include <avr/eeprom.h>
#include <timer.h>
#include <io.c>
#define F_CPU 8000000UL
#include <util/delay.h>
#include <string.h>
#include "SPI_Master_C_file.c"
#include "Font.h"
#include "timer.h"
#include "Nokia_5110.h" 


#define SS       DDB4
#define MOSI     DDB5
#define MISO     DDB6
#define SCK      DDB7

void SPI_ServantInit(void) {
	/* Set MISO output, all others input */
	DDRB &= ~(1<<MISO) | (1<<SCK) | (1<<SS);
	DDRB |= (1<<MISO);
	/* Enable SPI */
	SPCR = ((1<<SPE)); //| (1<<SPIE));
}

char SPI_SlaveReceive(short data) {
	SPDR = data;
	while(!(SPSR & (1<<SPIF)))
	;
	return SPDR;
}


unsigned short Lock_Info = 3;
char curr_state = 0;
enum LCD_States {Init, Locked, RFID, FPscan, Unlocked, Locking} LCDstate;
void LCD_Tick(){
	switch(LCDstate){
		case Init:
			if (!GetBit(PIND, 0))
			{
				//N5110_clear();
				LCDstate = Unlocked;
			}else{
				//N5110_clear();
				LCDstate = Locked;
			}
			break;
		case Locked:
			if (Lock_Info == 1)
			{
				Lock_Info = 3;
				//N5110_clear();
				LCDstate = RFID;
			}else{
				LCDstate = Locked;
			}
			break;
		case RFID:
			if (!GetBit(PINA, 2))
			{
				//N5110_clear();
				LCDstate = FPscan;
			}else{
				LCDstate = RFID;
			}
			break;
		case FPscan:
			if (!GetBit(PINA, 3))
			{
				//N5110_clear();
				LCDstate = Unlocked;
			}else{
				LCDstate = FPscan;
			}
			break;
		case Unlocked:
			if (Lock_Info == 2)
			{
				Lock_Info = 3;
				//N5110_clear();
				LCDstate = Locked;
			}else{
				LCDstate = Unlocked;
			}
			break;
		default:
			LCDstate = Init;
			break;
	}
	switch(LCDstate){
		case Init:
			curr_state = 0;
			break;
		case Locked:
			PORTD = 0;
			curr_state = 1;
			//lcd_setXY(0x80, 0x40 +2);
			//N5110_Data("LOCKED");
			break;
		case RFID:
			curr_state = 2;
			//lcd_setXY(0x80, 0x40 +2);
			//N5110_Data("SCAN RFID");
			break;
		case FPscan:
			curr_state = 3;
			//lcd_setXY(0x80, 0x40 +2);
			//N5110_Data("SCAN FINGER");
			break;
		case Unlocked:
			curr_state = 4;
			PORTD = 1;
			//lcd_setXY(0x80, 0x40 +2);
			//N5110_Data("UNLOCKED");
			break;
		default:
			break;
	}
};

int main(void)
{
    DDRA = 0x00; PORTA = 0xFF;
	DDRD = 0xFF; PORTD = 0x00;
	SPI_ServantInit();
	//SPI_Init();
	//N5110_init();
	//N5110_clear();
	//lcd_setXY(0x9F ,0x42);
	//lcd_setXY(0x80, 0x40 +2);
	PORTD = 1;
    while (1) 
    {
		LCD_Tick();
		Lock_Info = SPI_SlaveReceive(curr_state);
		//PORTD = 0;
    }
}

