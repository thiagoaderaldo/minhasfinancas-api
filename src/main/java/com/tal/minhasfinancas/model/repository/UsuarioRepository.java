package com.tal.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tal.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

//	Optional<Usuario> findByEmail(String email);
	
	boolean existsByEmail(String email);
}
