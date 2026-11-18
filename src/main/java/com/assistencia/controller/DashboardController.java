// Define o pacote dos controladores
package com.assistencia.controller;

// Importa todos os repositórios necessários para recolher estatísticas e listagens
import com.assistencia.repository.AgendamentoRepository;
import com.assistencia.repository.ClienteRepository;
import com.assistencia.repository.EquipamentoRepository;
import com.assistencia.repository.UsuarioRepository;

// Importa classes do Spring Security para aceder aos dados do utilizador autenticado
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

// Importa classes do Spring MVC
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Marca a classe como controlador MVC
@Controller
public class DashboardController {

    // Repositórios injetados para consultar contagens e dados do painel
    private final ClienteRepository clienteRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor para injeção de dependências de todos os repositórios
    public DashboardController(ClienteRepository clienteRepository,
                               EquipamentoRepository equipamentoRepository,
                               AgendamentoRepository agendamentoRepository,
                               UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Mapeia tanto a raiz "/" como "/dashboard" para este método
    @GetMapping({"/", "/dashboard"})
    public String dashboard(
            // Injeta o objeto UserDetails do utilizador atualmente autenticado na sessão
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        // RN04: Exibir o nome do usuário logado e dados gerais
        // Obtém o login do utilizador autenticado; usa "Usuário" caso não exista sessão ativa
        String login = userDetails != null ? userDetails.getUsername() : "Usuário";
        // Pesquisa o utilizador na base de dados pelo login para obter o nome completo;
        // se não encontrar, utiliza o próprio login como alternativa
        String nomeCompleto = usuarioRepository.findByLogin(login)
                .map(u -> u.getNome())
                .orElse(login);

        // Adiciona o nome do utilizador ao modelo para exibição na interface
        model.addAttribute("nomeUsuario", nomeCompleto);
        // Adiciona os totais registados em cada tabela para os cartões de resumo do painel
        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalEquipamentos", equipamentoRepository.count());
        model.addAttribute("totalAgendamentos", agendamentoRepository.count());
        // Adiciona a lista ordenada dos próximos agendamentos para consulta rápida
        model.addAttribute("proximosAgendamentos", agendamentoRepository.findAllOrderByDataAgendamentoAsc());

        // Renderiza o template "dashboard.html"
        return "dashboard";
    }
}