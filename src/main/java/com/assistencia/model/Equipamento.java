// Define o pacote das entidades do modelo
package com.assistencia.model;

// Importa as anotações de persistência JPA
import jakarta.persistence.*;

// Marca a classe como entidade JPA
@Entity
// Mapeia a entidade para a tabela "equipamento"
@Table(name = "equipamento")
public class Equipamento {

    // Chave primária gerada automaticamente por auto-incremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RN07: Obrigatoriamente associado a um cliente previamente cadastrado
    // Relacionamento Muitos-para-Um obrigatório com Cliente e carregamento imediato (EAGER)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    // Define a coluna de chave estrangeira "cliente_id" na tabela 'equipamento'
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Categoria ou tipo do aparelho (ex.: Portátil, Smartphone, Impressora), obrigatório até 50 caracteres
    @Column(nullable = false, length = 50)
    private String tipo;

    // Marca do fabricante, obrigatória até 50 caracteres
    @Column(nullable = false, length = 50)
    private String marca;

    // Modelo específico do equipamento, obrigatório até 100 caracteres
    @Column(nullable = false, length = 100)
    private String modelo;

    // Número de série opcional, mapeado para a coluna "numero_serie"
    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    // Descrição detalhada do problema relatado pelo cliente, armazenada como tipo TEXT
    @Column(name = "defeito_declarado", columnDefinition = "TEXT")
    private String defeitoDeclarado;

    // Construtor padrão sem argumentos exigido pelo JPA
    public Equipamento() {
    }

    // Construtor completo para criação de um novo equipamento associado a um cliente
    public Equipamento(Cliente cliente, String tipo, String marca, String modelo, String numeroSerie, String defeitoDeclarado) {
        this.cliente = cliente;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.defeitoDeclarado = defeitoDeclarado;
    }

    // Métodos Getters e Setters para manipulação dos campos do equipamento

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getDefeitoDeclarado() {
        return defeitoDeclarado;
    }

    public void setDefeitoDeclarado(String defeitoDeclarado) {
        this.defeitoDeclarado = defeitoDeclarado;
    }
}