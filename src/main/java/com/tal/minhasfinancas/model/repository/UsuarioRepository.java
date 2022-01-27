package com.tal.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tal.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
