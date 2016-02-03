/*
 * Uart1_recieve_test.c
 *
 *  Created on: 18 Jan 2016
 *      Author: Aonghus
 */

#include "fsl_device_registers.h"
#include "board.h"
#include "fsl_debug_console.h"
#include "fsl_uart_driver.h"

char ch;

/*******************************************************************************
 * Prototypes
 ******************************************************************************/
extern void UART_DRV_IRQHandler(uint32_t instance);
/*******************************************************************************
 * Code
 ******************************************************************************/

/* Implementation of UART0 handler named in startup code. */
void UART1_IRQHandler(void)
{
	UART_DRV_IRQHandler(1);
}

int main(void)
{
	uart_state_t uartState; // user provides memory for the driver state structure
	uart_user_config_t uartConfig;
	long int x;

	char AT[] = "\nAT";
	char rxBuff[10];
	uint32_t byteCountBuff = 0;
    //Initialise the FRDM-KL26Z Board
	hardware_init();
	configure_uart_pins(0);	//instance 0 is UART1???
	// Call OSA_Init to setup LP Timer for timeout
	OSA_Init();


	uartConfig.baudRate = 9600;
	uartConfig.bitCountPerChar = kUart8BitsPerChar;
	uartConfig.parityMode = kUartParityDisabled;
	uartConfig.stopBitCount = kUartOneStopBit;

	UART_DRV_Init(1, &uartState,&uartConfig);

	//Print message to serial terminal
	PRINTF("First Embedded Systems Lab_Aonghus\r\n");
	PRINTF("Type a character and it will be echoed back\r\n\n");
	int i = 0;
	byteCountBuff = sizeof(AT);
    while(1) {
    	//UART_DRV_SendDataBlocking(1, AT, byteCountBuff, 16000u);
    	 while(UART_DRV_ReceiveDataBlocking ( 1, rxBuff, sizeof(rxBuff),16000u) == kStatus_UART_Success )
    	 {
    		 for(i=0;i<sizeof(rxBuff);i++)
    		 PRINTF("%c",rxBuff[i]);
    	 }
    	// if(UART_DRV_GetTransmitStatus (1,sizeof(rxBuff)) == kStatus_UART_Success ){

    	// }


    	for(x=0;x<10000000;x++);
    }
    /* Never leave main */
    return 0;
}
////////////////////////////////////////////////////////////////////////////////
// EOF
////////////////////////////////////////////////////////////////////////////////



