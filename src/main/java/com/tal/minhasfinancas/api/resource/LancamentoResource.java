package com.tal.minhasfinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tal.minhasfinancas.api.dto.LancamentoDTO;
import com.tal.minhasfinancas.exception.RegraNegocioException;
import com.tal.minhasfinancas.model.entity.Lancamento;
import com.tal.minhasfinancas.model.entity.Usuario;
import com.tal.minhasfinancas.model.enums.StatusLancamento;
import com.tal.minhasfinancas.model.enums.TipoLancamento;
import com.tal.minhasfinancas.service.LancamentoService;
import com.tal.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	private LancamentoService service;
	private UsuarioService uService;

	public LancamentoResource(LancamentoService service) {
		
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<?> salvar( @RequestBody LancamentoDTO dto){
		return null;
		
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = uService
		.obterPorId(dto.getUsuario())
		.orElseThrow( () -> new RegraNegocioException("Usuario não encontrado com o ID informado."));
		
		lancamento.setUsuario(usuario);
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
	}
}
