// Define o pacote dos repositórios
package com.assistencia.repository;

// Importa a entidade Equipamento
import com.assistencia.model.Equipamento;
// Importa a interface JpaRepository e a anotação @Repository do Spring
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Importa a interface List
import java.util.List;

// Indica ao Spring que esta interface é um repositório de dados
@Repository
// Estende JpaRepository para gerir objetos Equipamento cuja chave primária é do tipo Long
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Método de consulta derivada que procura todos os equipamentos associados ao ID de um cliente específico
    // (corresponde a "SELECT e FROM Equipamento e WHERE e.cliente.id = :clienteId")
    List<Equipamento> findByClienteId(Long clienteId);
}