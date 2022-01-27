package com.tal.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tal.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
