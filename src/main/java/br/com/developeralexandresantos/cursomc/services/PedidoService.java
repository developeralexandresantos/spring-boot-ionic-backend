package br.com.developeralexandresantos.cursomc.services;

import br.com.developeralexandresantos.cursomc.domain.ItemPedido;
import br.com.developeralexandresantos.cursomc.domain.PagamentoComBoleto;
import br.com.developeralexandresantos.cursomc.domain.Pedido;
import br.com.developeralexandresantos.cursomc.domain.enums.EstadoPagamento;
import br.com.developeralexandresantos.cursomc.repositories.ItemPedidoRepository;
import br.com.developeralexandresantos.cursomc.repositories.PagamentoRepository;
import br.com.developeralexandresantos.cursomc.repositories.PedidoRepository;
import br.com.developeralexandresantos.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

	private final PedidoRepository repo;

	private final BoletoService boletoService;

	private final ProdutoService produtoService;

	private final PagamentoRepository pagamentoRepository;

	private final ItemPedidoRepository itemPedidoRepository;

	private final ClienteService clienteService;
	
	private final EmailService emailService;

	public PedidoService(PedidoRepository repo, BoletoService boletoService, ProdutoService produtoService, PagamentoRepository pagamentoRepository, ItemPedidoRepository itemPedidoRepository, ClienteService clienteService, EmailService emailService) {
		this.repo = repo;
		this.boletoService = boletoService;
		this.produtoService = produtoService;
		this.pagamentoRepository = pagamentoRepository;
		this.itemPedidoRepository = itemPedidoRepository;
		this.clienteService = clienteService;
		this.emailService = emailService;
	}

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		System.out.println(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
}
