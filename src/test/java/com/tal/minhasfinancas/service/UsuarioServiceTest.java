package com.tal.minhasfinancas.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.tal.minhasfinancas.exception.RegraNegocioException;
import com.tal.minhasfinancas.model.entity.Usuario;
import com.tal.minhasfinancas.model.repository.UsuarioRepository;
import com.tal.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@Autowired
	UsuarioService service;
	
//	@Autowired
	@MockBean
	UsuarioRepository repository;
	
	@Before
	public void setUp() {
		
//		repository = Mockito.mock(UsuarioRepository.class);
		service = new UsuarioServiceImpl(repository);
	}

	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
//		repository.deleteAll();
		
		//ação
		service.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
//		Usuario usuario = Usuario.builder().nome("usurio").email("email@email.com").build();
//		repository.save(usuario);
		
		//ação
		service.validarEmail("email@email.com");
	}
	
}
