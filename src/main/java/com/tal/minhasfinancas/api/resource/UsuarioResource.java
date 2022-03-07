package com.tal.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tal.minhasfinancas.api.dto.UsuarioDTO;
import com.tal.minhasfinancas.exception.RegraNegocioException;
import com.tal.minhasfinancas.model.entity.Usuario;
import com.tal.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	
	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.email(dto.getEmail())
				.nome(dto.getNome())
				.senha(dto.getSenha())
				.build();
		
		try {
			
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			
		}catch (RegraNegocioException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
