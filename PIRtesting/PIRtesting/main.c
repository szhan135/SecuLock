/*
 * PIRtesting.c
 *
 * Created: 4/22/2020 5:19:57 PM
 * Author : aahun
 */ 

#include <avr/io.h>
#include <util/delay.h>

//#define LED_OUTPUT		DDRB
//#define PIR_Input		PINA0

void ADC_init() {

	ADCSRA |= (1 << ADEN) | (1 << ADSC) | (1 << ADATE);
	// ADEN: setting this bit enables analog-to-digital conversion.
	// ADSC: setting this bit starts the first conversion.
	// ADATE: setting this bit enables auto-triggering. Since we are
	//        in Free Running Mode, a new conversion will trigger whenever
	//        the previous conversion completes.

}

int main(void)
{
    /* Replace with your application code */
    DDRA = 0x00;	PORTA = 0xFF;	/* Set the PIR port as input port */
    DDRB = 0xFF;	PORTB = 0x00;	/* Set the LED port as output port */

	ADC_init();
	//_delay_ms(5000);
	
    while(1)
    {
		if (PORTA == 0x01) {
			PORTB = 0x01;
		}
		else {
			PORTB = 0x00;
		}
    }
}

