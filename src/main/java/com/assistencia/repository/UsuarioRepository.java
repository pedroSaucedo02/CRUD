// Define o pacote dos repositórios
package com.assistencia.repository;

// Importa a entidade Usuario
import com.assistencia.model.Usuario;
// Importa a interface JpaRepository e a anotação @Repository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Importa Optional para encapsular o resultado de forma segura caso o utilizador não exista
import java.util.Optional;

// Regista a interface como repositório no contentor de injeção de dependências do Spring
@Repository
// Estende JpaRepository para a entidade Usuario com chave primária Long
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método de consulta derivada que procura um utilizador pelo campo único 'login';
    // devolve um Optional<Usuario> para obrigar o tratamento seguro quando o registo não é encontrado
    Optional<Usuario> findByLogin(String login);
}