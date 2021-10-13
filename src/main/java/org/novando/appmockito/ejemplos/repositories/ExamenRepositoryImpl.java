package org.novando.appmockito.ejemplos.repositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.novando.appmockito.ejemplos.models.Examen;
import org.novando.appmockito.ejemplos.services.Datos;

public class ExamenRepositoryImpl implements ExamenRepository{

	@Override
	public List<Examen> findAll() {
		System.out.println("ExamenRepositoryImpl.findAll");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Datos.EXAMENES;
	}

	@Override
	public Examen guardar(Examen examen) {
		System.out.println("ExamenRepositoryImpl.guardar");
		return Datos.EXAMEN;
	}
}
