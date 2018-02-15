package com.edesur;

import servicios.ProcesaRetoSRV;

public class RetornosSolic {

	public static void main(String[] args) {
		
		ProcesaRetoSRV miSrv = new ProcesaRetoSRV();
		
		if(!miSrv.LeeSolicitudes()) {
			System.out.println("Falló Retornos - Proceso Abortado");
		}
		
		System.out.println("Retornos OK");

	}

}
