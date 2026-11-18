// Define o pacote dos controladores da aplicação
package com.assistencia.controller;

// Importa as anotações do Spring MVC para definição de controlador e mapeamento de rotas GET
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Marca a classe como um controlador web do Spring
@Controller
public class AuthController {

    // Mapeia pedidos HTTP GET para "/login"
    @GetMapping("/login")
    public String login() {
        // Retorna o nome da vista "login" (templates/login.html);
        // o processamento do formulário POST de autenticação é gerido pelo Spring Security
        return "login";
    }
}