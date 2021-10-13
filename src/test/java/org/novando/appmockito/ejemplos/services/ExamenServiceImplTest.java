package org.novando.appmockito.ejemplos.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import org.mockito.stubbing.OngoingStubbing;
import org.novando.appmockito.ejemplos.models.Examen;
import org.novando.appmockito.ejemplos.repositories.ExamenRepository;
import org.novando.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.novando.appmockito.ejemplos.repositories.PreguntaRepository;
import org.novando.appmockito.ejemplos.repositories.PreguntaRespositoryImpl;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
	@Mock
	PreguntaRespositoryImpl preguntaRepository;
	@Mock
	ExamenRepositoryImpl repository;

	@InjectMocks
	ExamenServiceImpl service;
	private ExamenRepositoryImpl examenRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		//repository = mock(ExamenRepositoryImpl.class);
		//preguntaRepository = mock(PreguntaRepositoryImpl.class);
		//service = new ExamenServiceImpl(repository, preguntaRepository);
	}

	@Captor
	ArgumentCaptor<Long> captor;

	@Test
	void findExamenPorNombre() {

		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

		assertTrue(examen.isPresent());
		assertEquals(5L, examen.get().getId());
		assertEquals("Matematicas", examen.get().getNombre());
	}

	@Test
	void findExamenPorNombreListaVacia() {

		List<Examen> datos = Collections.emptyList();


		when(repository.findAll()).thenReturn(datos);
		Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

		assertFalse(examen.isPresent());
	}

	@Test
	void testPreguntasExamen() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));
	}

	@Test
	void testPreguntasExamenVerify() {
		//Given

		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		//when
		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

		// then
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(5L);

	}

	@Test
	void testGuardarExamen() {
		//Given
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);
		when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {
			Long secuencia = 8L;

			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}

			;
		});

		//When
		Examen examen = service.guardar(newExamen);

		//Then

		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Fisica", examen.getNombre());
		verify(repository).guardar(any(examen.getClass()));
		verify(preguntaRepository).guardarVarias(anyList());
	}

	@Test
	void testManejoException() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			service.findExamenPorNombreConPreguntas("Matematicas");
		});
		assertEquals(IllegalArgumentException.class, exception.getClass());
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(null);
	}

	@Test
	void testArgumentMatchers() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		service.findExamenPorNombreConPreguntas("Matematicas");

		verify(repository).findAll();
		//verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(arg->arg!=null && arg.equals(5L)));
		verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
	}

	@Test
	void testArgumentMatchers2() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		service.findExamenPorNombreConPreguntas("Matematicas");
		verify(repository).findAll();
		//verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(arg->arg!=null && arg.equals(5L)));
		//verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
	}

	@Test
	void testArgumentMatchers3() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		service.findExamenPorNombreConPreguntas("Matematicas");
		verify(repository).findAll();
		//verify(preguntaRepository).findPreguntasPorExamenId(Mockito.argThat(arg->arg!=null && arg.equals(5L)));
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg > 0));
	}

	public static class MiArgsMatchers implements ArgumentMatcher<Long> {

		private Long argument;

		@Override
		public boolean matches(Long argument) {
			this.argument = argument;
			return argument != null && argument > 0;
		}

		@Override
		public String toString() {
			return "es para un mensaje personalizado de error que " +
					"imprime mockito en caso de que falle el test " +
					argument + " debe ser un entero positivo";
		}
	}

	@Test
	void testArgumentCaptor() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		service.findExamenPorNombreConPreguntas("Matematicas");

		//ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

		assertEquals(5L, captor.getValue());

	}

	@Test
	void testDoThrow() {
		Examen examen = Datos.EXAMEN;
		examen.setPreguntas(Datos.PREGUNTAS);
		doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

		assertThrows(IllegalArgumentException.class, () -> {
			service.guardar(examen);
		});
	}

	@Test
	void testDoAnswer() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return id == 5 ? Datos.PREGUNTAS : Collections.emptyList();
		}).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
		assertEquals(5, examen.getPreguntas().size());
		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());

		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void doAnswerGuardarExamen() {
		//Given
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);

		doAnswer(new Answer<Examen>() {
			Long secuencia = 8L;

			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}

			;
		}).when(repository).guardar(any(Examen.class));

		//When
		Examen examen = service.guardar(newExamen);

		//Then

		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Fisica", examen.getNombre());
		verify(repository).guardar(any(examen.getClass()));
		verify(preguntaRepository).guardarVarias(anyList());
	}

	@Test
	void testDoCallRealMethod() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

		doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

		assertEquals(5L, examen.getPreguntas().size());
		assertEquals("Matematicas", examen.getNombre());
	}

	/*
		@Test
		void testSpy(){
			ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
			PreguntaRepository preguntaRepository = spy(PreguntaRespositoryImpl.class);
			ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
			List<String> preguntas = Arrays.asList("aritmetica");
			doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
			//when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

			Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

			assertEquals(5L, examen.getId());
			assertEquals("Matematicas", examen.getNombre());
			assertEquals(1L, examen.getPreguntas().size());
			assertTrue(examen.getPreguntas().contains("aritmetica"));

			verify(examenRepository).findAll();
			verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
		}
	*/
	@Test
	void testOrdenDeInvocaciones() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		service.findExamenPorNombreConPreguntas("Matematicas");
		service.findExamenPorNombreConPreguntas("Lenguaje");

		InOrder inOrder = inOrder(repository, preguntaRepository);
		inOrder.verify(repository).findAll();
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
		inOrder.verify(repository).findAll();
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);
	}

	@Test
	void testNumeroDeInvocaciones() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		service.findExamenPorNombreConPreguntas("Matematicas");

		verify(preguntaRepository).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, times(1)).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atMost(7)).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);

	}

	@Test
	void testNumeroDeInvocaciones2() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		service.findExamenPorNombreConPreguntas("Matematicas");

		//verify(preguntaRepository).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, times(2)).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atLeast(2)).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
		verify(preguntaRepository, atMost(29)).findPreguntasPorExamenId(5L);
		//verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
	}

	@Test
	void testNumeroDeInvocaciones3() {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		service.findExamenPorNombreConPreguntas("Matematicas");

		verify(preguntaRepository, never()).findPreguntasPorExamenId(5L);
		verifyNoInteractions(preguntaRepository);

		verify(repository).findAll();
		verify(repository, times(1)).findAll();
		verify(repository, atLeast(1)).findAll();
		verify(repository, atLeastOnce()).findAll();
		verify(repository, atMost(10)).findAll();
		verify(repository, atMostOnce()).findAll();
	}
}