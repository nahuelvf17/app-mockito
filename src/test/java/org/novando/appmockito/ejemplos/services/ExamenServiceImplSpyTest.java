package org.novando.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.novando.appmockito.ejemplos.models.Examen;
import org.novando.appmockito.ejemplos.repositories.ExamenRepository;
import org.novando.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.novando.appmockito.ejemplos.repositories.PreguntaRepository;
import org.novando.appmockito.ejemplos.repositories.PreguntaRespositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {
	@Spy
	PreguntaRespositoryImpl preguntaRepository;
	@Spy
	ExamenRepositoryImpl repository;
	
	@InjectMocks
	ExamenServiceImpl service;


	@Test
	void testSpy(){
		List<String> preguntas = Arrays.asList("aritmetica");

		doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(1L, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}
}
