// Define o pacote das entidades do modelo
package com.assistencia.model;

// Importa as anotações JPA e a classe LocalDateTime para registar datas/horas
import jakarta.persistence.*;
import java.time.LocalDateTime;

// Declara a classe como entidade JPA
@Entity
// Associa a entidade à tabela "usuario"
@Table(name = "usuario")
public class Usuario {

    // Chave primária gerada automaticamente pela base de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome completo do utilizador apresentado no painel principal (Dashboard)
    @Column(nullable = false, length = 100)
    private String nome;

    // Nome de utilizador para início de sessão; unique = true impede logins duplicados
    @Column(nullable = false, unique = true, length = 50)
    private String login;

    // Palavra-passe armazenada sob a forma de hash seguro (ex.: BCrypt), nunca em texto simples
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    // Registo opcional da data e hora da última autenticação efetuada pelo utilizador
    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    // Construtor padrão sem parâmetros exigido pelo JPA
    public Usuario() {
    }

    // Construtor para criar um novo utilizador com nome, login e hash da palavra-passe
    public Usuario(String nome, String login, String senhaHash) {
        this.nome = nome;
        this.login = login;
        this.senhaHash = senhaHash;
    }

    // Métodos Getters e Setters da entidade Usuario

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}