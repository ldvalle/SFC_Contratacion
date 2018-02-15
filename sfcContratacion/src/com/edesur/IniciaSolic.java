package com.edesur;
import servicios.ProcesaSolSRV;

public class IniciaSolic {

	public static void main(String[] args) {
		ProcesaSolSRV miSrv = new ProcesaSolSRV();


		if(! miSrv.LeeSolicitudes()) {
			System.out.println("Error al cargar la solicitud.");
			return;
		}
		
		System.out.println("Operación Realizada OK");
	}
}
