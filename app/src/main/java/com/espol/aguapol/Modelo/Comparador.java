package com.espol.aguapol.Modelo;

import java.util.Comparator;
import java.util.Date;

public class Comparador implements Comparator<Alarma> {
    @Override
    public int compare(Alarma o1, Alarma o2) {
        Herramientas herramientas=new Herramientas();
        Date fecha1= herramientas.obtenerFecha("HH:mm : dd/MM/yy",o1.getFechaHora());
        Date fecha2= herramientas.obtenerFecha("HH:mm : dd/MM/yy",o2.getFechaHora());
        long dif=herramientas.diferenciaFechas(fecha1,fecha2);
        if(dif>0){
            return -1;
        }
        else if(dif<0){
            return 1;
        }
        else{
            return 0;
        }

    }
}
