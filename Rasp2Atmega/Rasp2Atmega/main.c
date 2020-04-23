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


int main(void)
{
    DDRA = 0x00; PORTA = 0xFF;
	SPI_Init();
	N5110_init();
	N5110_clear();
	lcd_setXY(0x9F ,0x42);
	lcd_setXY(0x80, 0x40 +2);
	
    while (1) 
    {
		if (!GetBit(PINA, 0))
		{
			lcd_setXY(0x80, 0x40 +2);
			N5110_Data("RaspPi says 1");;
		}else{
			lcd_setXY(0x80, 0x40 +2);
			N5110_Data("RaspPi says 2");
		}
    }
}

