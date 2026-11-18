// Define o pacote dos controladores
package com.assistencia.controller;

// Importa as entidades Cliente e Equipamento
import com.assistencia.model.Cliente;
import com.assistencia.model.Equipamento;

// Importa os repositórios de acesso aos dados
import com.assistencia.repository.ClienteRepository;
import com.assistencia.repository.EquipamentoRepository;

// Importa as anotações e classes do Spring MVC
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Define a classe como controlador web MVC
@Controller
// Define o caminho base "/equipamentos" para as rotas deste controlador
@RequestMapping("/equipamentos")
public class EquipamentoController {

    // Repositórios necessários para gerir equipamentos e associá-los a clientes
    private final EquipamentoRepository equipamentoRepository;
    private final ClienteRepository clienteRepository;

    // Injeção de dependências via construtor
    public EquipamentoController(EquipamentoRepository equipamentoRepository, ClienteRepository clienteRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.clienteRepository = clienteRepository;
    }

    // Mapeia pedidos HTTP GET para "/equipamentos"
    @GetMapping
    public String listar(Model model) {
        // Carrega todos os equipamentos registados para exibição na tabela
        model.addAttribute("equipamentos", equipamentoRepository.findAll());
        // Carrega todos os clientes para preencher o seletor de proprietário no formulário
        model.addAttribute("clientes", clienteRepository.findAll());
        // Renderiza o template "equipamentos.html"
        return "equipamentos";
    }

    // RN07: Obrigatoriamente associar o equipamento a um cliente previamente cadastrado
    // Mapeia pedidos HTTP POST para "/equipamentos/salvar"
    @PostMapping("/salvar")
    public String salvar(
            // Captura o ID obrigatório do cliente proprietário
            @RequestParam("clienteId") Long clienteId,
            // Captura os dados técnicos obrigatórios do equipamento
            @RequestParam("tipo") String tipo,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            // Captura campos opcionais (número de série e descrição do defeito)
            @RequestParam(value = "numeroSerie", required = false) String numeroSerie,
            @RequestParam(value = "defeitoDeclarado", required = false) String defeitoDeclarado) {

        // Valida a existência do cliente na base de dados; impede o registo caso o ID seja inválido
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente obrigatório não encontrado: " + clienteId));

        // Instancia o novo equipamento já associado ao cliente validado
        Equipamento equipamento = new Equipamento(cliente, tipo, marca, modelo, numeroSerie, defeitoDeclarado);
        // Persiste o equipamento na base de dados
        equipamentoRepository.save(equipamento);

        // Redireciona para a listagem de equipamentos com indicação de sucesso
        return "redirect:/equipamentos?sucesso=true";
    }
}