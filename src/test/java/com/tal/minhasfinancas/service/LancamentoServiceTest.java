package com.tal.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tal.minhasfinancas.exception.RegraNegocioException;
import com.tal.minhasfinancas.model.entity.Lancamento;
import com.tal.minhasfinancas.model.entity.Usuario;
import com.tal.minhasfinancas.model.enums.StatusLancamento;
import com.tal.minhasfinancas.model.repository.LancamentoRepository;
import com.tal.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.tal.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execução
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificação
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		assertThat(lancamento.getStatus()).isEqualTo(lancamentoSalvo.getStatus());
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//execucao e verificação
		catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		//cenário
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		

		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execução
		service.atualizar(lancamentoSalvo);
		
		//verificação
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
		//execução e verificação
		catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execução
		service.deletar(lancamento);
		
		//verificação
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//execução
		catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
		
		//verificação
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarLancacmentos() {
		
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//execução
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificações
		Assertions
			.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execução
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificações
		assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		
		//cenário
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execução
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificação
		assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoUmLancamentoNaoExiste() {
		
		//cenário
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execução
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificação
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		
		//cenário
		Lancamento lancamento = new Lancamento();
		
		//Verifica erro da descrição
		Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Insira uma Descrição válida.");
		
		lancamento.setDescricao("");
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Insira uma Descrição válida.");
		//fim da verificação de erro da descrição
		
		//verifica erro do mês
		lancamento.setDescricao("Salário");		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		
		lancamento.setMes(0);		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		
		lancamento.setMes(13);		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		//fim da verificação de erro do mês
		
		//verifica erro do ano
		lancamento.setMes(1);		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(0);		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(13);		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		//fim da verificação de erro de ano
		
		//verifica do erro usuario
		lancamento.setAno(2020);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(new Usuario());
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		//fim da verificação de erro do ano
		
		//verifica erro de valor válido
		lancamento.setUsuario(new Usuario());
		lancamento.getUsuario().setId(1l);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");
		
		lancamento.setValor(BigDecimal.ZERO);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");
		//fim da verificação de erro de valor válido
		
		//verifica erro de tipo
		lancamento.setValor(BigDecimal.ONE);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento");
	}
}
