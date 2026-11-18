// Define o pacote ao qual esta classe pertence na arquitetura do projeto
package com.assistencia.controller;

// Importa as entidades (modelos) manipuladas por este controlador
import com.assistencia.model.Agendamento;
import com.assistencia.model.Cliente;
import com.assistencia.model.Equipamento;

// Importa os repositórios responsáveis pelo acesso à base de dados
import com.assistencia.repository.AgendamentoRepository;
import com.assistencia.repository.ClienteRepository;
import com.assistencia.repository.EquipamentoRepository;

// Importa as anotações e classes do Spring Framework para controlo web e formatação de datas
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Importa a classe do Java para representar data e hora sem fuso horário
import java.time.LocalDateTime;

// Indica ao Spring que esta classe é um controlador MVC (retorna vistas/páginas HTML)
@Controller
// Define o prefixo "/agendamentos" para todas as rotas mapeadas nesta classe
@RequestMapping("/agendamentos")
public class AgendamentoController {

    // Dependências dos repositórios declaradas como 'final' para garantir imutabilidade
    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final EquipamentoRepository equipamentoRepository;

    // Construtor utilizado pelo Spring para realizar a injeção de dependências automaticamente
    public AgendamentoController(AgendamentoRepository agendamentoRepository,
                                 ClienteRepository clienteRepository,
                                 EquipamentoRepository equipamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    // RN06: Listagem dos atendimentos ordenados por data de agendamento com dados de cliente e equipamento
    // Mapeia pedidos HTTP GET para "/agendamentos"
    @GetMapping
    public String listar(Model model) {
        // Adiciona ao modelo a lista de agendamentos ordenados por data crescente
        model.addAttribute("agendamentos", agendamentoRepository.findAllOrderByDataAgendamentoAsc());
        // Adiciona todos os clientes para preencher o seletor (dropdown) no formulário de cadastro
        model.addAttribute("clientes", clienteRepository.findAll());
        // Adiciona todos os equipamentos para preencher o seletor no formulário de cadastro
        model.addAttribute("equipamentos", equipamentoRepository.findAll());
        // Retorna o nome do template HTML ("agendamentos.html") a ser renderizado
        return "agendamentos";
    }

    // Cadastro de novo agendamento
    // Mapeia pedidos HTTP POST enviados para "/agendamentos/salvar"
    @PostMapping("/salvar")
    public String salvar(
            // Captura o ID do cliente enviado pelo formulário
            @RequestParam("clienteId") Long clienteId,
            // Captura o ID do equipamento enviado pelo formulário
            @RequestParam("equipamentoId") Long equipamentoId,
            // Captura a data/hora e converte a String no padrão ISO (ex.: 2026-11-17T14:30) para LocalDateTime
            @RequestParam("dataAgendamento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataAgendamento,
            // Captura o estado do atendimento; se não for enviado, assume "AGENDADO" por omissão
            @RequestParam(value = "status", defaultValue = "AGENDADO") String status,
            // Captura as observações como campo opcional (required = false)
            @RequestParam(value = "observacoes", required = false) String observacoes) {

        // Procura o cliente pelo ID na base de dados; lança exceção caso não exista
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente inválido: " + clienteId));
        // Procura o equipamento pelo ID na base de dados; lança exceção caso não exista
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Equipamento inválido: " + equipamentoId));

        // Cria uma nova instância de Agendamento com os dados validados
        Agendamento agendamento = new Agendamento(cliente, equipamento, dataAgendamento, status, observacoes);
        // Persiste o novo agendamento na base de dados
        agendamentoRepository.save(agendamento);

        // Redireciona para a listagem (padrão Post-Redirect-Get) com parâmetro de confirmação
        return "redirect:/agendamentos?sucesso=cadastrado";
    }

    // RN06: Atualizar a data de um agendamento existente, salvando no banco de dados
    // Mapeia pedidos HTTP POST enviados para "/agendamentos/reagendar"
    @PostMapping("/reagendar")
    public String reagendar(
            // Captura o ID do agendamento a alterar
            @RequestParam("id") Long id,
            // Captura a nova data/hora no formato ISO DATE_TIME
            @RequestParam("novaData") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaData) {

        // Procura o agendamento existente; lança exceção se o ID não for encontrado
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado: " + id));

        // Atualiza a data do agendamento no objeto
        agendamento.setDataAgendamento(novaData);
        // Guarda a alteração na base de dados (como o objeto já tem ID, o JPA executa um UPDATE)
        agendamentoRepository.save(agendamento);

        // Redireciona para a página de agendamentos indicando sucesso no reagendamento
        return "redirect:/agendamentos?sucesso=reagendado";
    }
}