package org.novando.appmockito.ejemplos.repositories;

import org.novando.appmockito.ejemplos.services.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRespositoryImpl implements PreguntaRepository{

    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println();
    }
}
