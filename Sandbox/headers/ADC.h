
#ifndef ADC_H
#define ADC_H
void ADC_init() {
	
	ADMUX = (1<<REFS1) | (1<<REFS0) ;
	ADCSRA|= (1<<ADEN) | (1<<ADPS2) | (1<<ADPS1) | (1<<ADPS0);
	// ADEN: setting this bit enables analog-to-digital conversion.
	// ADSC: setting this bit starts the first conversion.
	// ADATE: setting this bit enables auto-triggering. Since we are
	//        in Free Running Mode, a new conversion will trigger whenever
	//        the previous conversion completes.
}
uint16_t analog_read(uint8_t channel)
{
	ADMUX = (ADMUX & 0xF8) | (channel & 7);
	
	ADCSRA|=(1<<ADSC);
	while ( !(ADCSRA & (1<<ADIF)));
	ADCSRA|=(1<<ADIF);
	
	
	return (ADC);
}


#endif //ADC_H