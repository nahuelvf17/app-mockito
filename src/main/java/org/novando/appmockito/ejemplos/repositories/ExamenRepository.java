package org.novando.appmockito.ejemplos.repositories;

import java.util.List;

import org.novando.appmockito.ejemplos.models.Examen;

public interface ExamenRepository {
	Examen guardar(Examen examen);
	
	List<Examen> findAll();
	
}
