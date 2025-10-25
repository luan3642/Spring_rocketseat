package br.com.alldirect.luan.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// estrutura que criamos quando queremos paginas, retorna template
@Controller
// estamos construindo nossa API
@RestController
// annotation para dizer qual rota queremos mapear
// http://localhost:8080/minha-rota
@RequestMapping("/primeiraRota")
public class MinhaPrimeiraController {

    /*
    - GET: buscar informacao
    - POST: adicionar um dado ou uma informacao
    - PUT: alterar uma informacao
    - DELETE: remover um dado
    - PATCH: alterar somente uma parte do info/dado
     */
    @GetMapping("/")
    public String primeiraMensagem(){
        return "funcionou";
    }
}
