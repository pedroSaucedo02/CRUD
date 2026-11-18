// Define o pacote das entidades do modelo
package com.assistencia.model;

// Importa o conversor personalizado responsável por encriptar e desencriptar o CPF
import com.assistencia.converter.CpfCryptoConverter;
// Importa as anotações de mapeamento objeto-relacional (ORM) do JPA
import jakarta.persistence.*;

// Importa as classes de coleção para armazenar os equipamentos do cliente
import java.util.ArrayList;
import java.util.List;

// Marca a classe como entidade JPA
@Entity
// Associa a entidade à tabela "cliente"
@Table(name = "cliente")
public class Cliente {

    // Chave primária gerada automaticamente por auto-incremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coluna obrigatória para o nome do cliente (até 150 caracteres)
    @Column(nullable = false, length = 150)
    private String nome;

    // RN03: CPF armazenado de forma criptografada no banco de dados
    // Aplica automaticamente o CpfCryptoConverter antes de gravar e após ler o valor na base de dados
    @Convert(converter = CpfCryptoConverter.class)
    // Define tamanho de 255 caracteres para acomodar o texto cifrado (maior que os 11/14 dígitos originais)
    @Column(nullable = false, length = 255)
    private String cpf;

    // Coluna opcional para o telefone de contacto (até 30 caracteres)
    @Column(length = 30)
    private String telefone;

    // Coluna opcional para o endereço de correio eletrónico (até 100 caracteres)
    @Column(length = 100)
    private String email;

    // Coluna opcional para a morada do cliente (até 255 caracteres)
    @Column(length = 255)
    private String endereco;

    // Define o lado inverso do relacionamento Um-para-Muitos com Equipamento:
    // mappedBy = "cliente" indica que a chave estrangeira está na classe Equipamento;
    // cascade = CascadeType.ALL propaga operações (ex.: remoção) aos equipamentos;
    // orphanRemoval = true elimina da base de dados equipamentos removidos desta lista
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipamento> equipamentos = new ArrayList<>();

    // Construtor padrão sem argumentos exigido pelo JPA
    public Cliente() {
    }

    // Construtor para inicializar um cliente com os seus dados cadastrais
    public Cliente(String nome, String cpf, String telefone, String email, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    // Métodos Getters e Setters de acesso e modificação dos atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(List<Equipamento> equipamentos) {
        this.equipamentos = equipamentos;
    }
}