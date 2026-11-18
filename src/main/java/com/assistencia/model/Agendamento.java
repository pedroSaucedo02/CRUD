// Define o pacote das entidades (modelos de domínio) do sistema
package com.assistencia.model;

// Importa as anotações de persistência do Jakarta Persistence (JPA)
import jakarta.persistence.*;
// Importa a classe para manipulação de data e hora
import java.time.LocalDateTime;

// Indica ao JPA que esta classe é uma entidade mapeada para uma tabela na base de dados
@Entity
// Especifica explicitamente o nome da tabela correspondente ("agendamento")
@Table(name = "agendamento")
public class Agendamento {

    // Define o atributo 'id' como a chave primária da tabela
    @Id
    // Configura a geração automática do ID pelo motor da base de dados (auto-incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RN06: Exibindo todas as informações relacionadas ao cliente e ao equipamento
    // Define um relacionamento Muitos-para-Um (vários agendamentos podem pertencer ao mesmo cliente);
    // optional = false impede agendamentos sem cliente e FetchType.EAGER carrega os dados do cliente de imediato
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    // Define a coluna de chave estrangeira ("cliente_id") na tabela 'agendamento', não permitindo valores nulos
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Define o relacionamento Muitos-para-Um com o equipamento a reparar, também obrigatório e de carregamento imediato
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    // Mapeia a chave estrangeira "equipamento_id" na tabela 'agendamento'
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    // Mapeia o atributo para a coluna "data_agendamento", exigindo preenchimento obrigatório
    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    // Mapeia a coluna "status" (estado do agendamento), obrigatória e com limite máximo de 30 caracteres
    @Column(nullable = false, length = 30)
    private String status;

    // Mapeia a coluna "observacoes" como tipo TEXT na base de dados para suportar textos longos
    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Construtor vazio obrigatório exigido pela especificação JPA para instanciar a entidade via reflexão
    public Agendamento() {
    }

    // Construtor parametrizado utilizado para criar novos agendamentos antes de os persistir
    public Agendamento(Cliente cliente, Equipamento equipamento, LocalDateTime dataAgendamento, String status, String observacoes) {
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataAgendamento = dataAgendamento;
        this.status = status;
        this.observacoes = observacoes;
    }

    // Métodos Getters e Setters para leitura e alteração controlada dos atributos da entidade

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}