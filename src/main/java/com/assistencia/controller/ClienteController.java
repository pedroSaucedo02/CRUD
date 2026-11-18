// Define o pacote dos controladores
package com.assistencia.controller;

// Importa a entidade Cliente e o respetivo repositório
import com.assistencia.model.Cliente;
import com.assistencia.repository.ClienteRepository;

// Importa as anotações e classes do Spring MVC
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Importa a interface List para manipular coleções de clientes
import java.util.List;

// Regista a classe como controlador MVC
@Controller
// Define "/clientes" como rota base para todos os endpoints deste controlador
@RequestMapping("/clientes")
public class ClienteController {

    // Repositório para operações de persistência da entidade Cliente
    private final ClienteRepository clienteRepository;

    // Injeção de dependência via construtor
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // RN05: Busca por confirmação de termo e listagem dos correspondentes
    // Mapeia pedidos HTTP GET para "/clientes"
    @GetMapping
    public String listar(
            // Recebe o parâmetro opcional "termo" da query string (ex.: /clientes?termo=João)
            @RequestParam(value = "termo", required = false) String termo,
            Model model) {

        List<Cliente> clientes;
        // Verifica se foi enviado um termo de pesquisa válido (não nulo e não vazio após remover espaços)
        if (termo != null && !termo.trim().isEmpty()) {
            // Executa a pesquisa filtrada no repositório
            clientes = clienteRepository.buscarPorTermo(termo.trim());
        } else {
            // Caso contrário, lista todos os clientes registados
            clientes = clienteRepository.findAll();
        }

        // Envia a lista de clientes resultante para a vista
        model.addAttribute("clientes", clientes);
        // Mantém o termo pesquisado no modelo para preencher novamente o campo de busca no HTML
        model.addAttribute("termoBusca", termo);
        // Renderiza o template "clientes.html"
        return "clientes";
    }

    // Cadastro de novo cliente
    // Mapeia pedidos HTTP POST para "/clientes/salvar"
    @PostMapping("/salvar")
    public String salvar(
            // @ModelAttribute preenche automaticamente os atributos do objeto Cliente com os campos do formulário
            @ModelAttribute Cliente cliente) {
        // Guarda o novo cliente na base de dados
        clienteRepository.save(cliente);
        // Redireciona para a lista de clientes com aviso de sucesso
        return "redirect:/clientes?sucesso=cadastrado";
    }

    // RN05: Editar os dados de um cliente já cadastrado, salvando as alterações no banco de dados
    // Mapeia pedidos HTTP POST para "/clientes/editar"
    @PostMapping("/editar")
    public String editar(
            // Captura os campos editados enviados pelo formulário
            @RequestParam("id") Long id,
            @RequestParam("nome") String nome,
            @RequestParam("telefone") String telefone,
            @RequestParam("email") String email,
            @RequestParam(value = "endereco", required = false) String endereco) {

        // Obtém o cliente existente pelo ID ou lança exceção se não for encontrado
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));

        // Atualiza os atributos do cliente com os novos valores
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        // Atualiza a morada apenas se o parâmetro não vier nulo
        if (endereco != null) {
            cliente.setEndereco(endereco);
        }

        // Grava as alterações na base de dados
        clienteRepository.save(cliente);
        // Redireciona para a listagem indicando que a edição foi concluída
        return "redirect:/clientes?sucesso=editado";
    }
}