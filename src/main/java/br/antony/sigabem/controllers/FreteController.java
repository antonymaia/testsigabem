package br.antony.sigabem.controllers;

import br.antony.sigabem.entities.FreteEntity;
import br.antony.sigabem.services.FreteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/frete")
@Api(value="API REST Frete")
@CrossOrigin(origins = "*")
public class FreteController {

    @Autowired
    private FreteService freteService;

    @RequestMapping(value = "/consultar",method = RequestMethod.GET)
    @ApiOperation(value = "Retorna o frete calculado")
    public ResponseEntity<FreteEntity> consultar(@RequestParam(required = true) double peso,
                                                 @RequestParam(required = true) String cepOrigem,
                                                 @RequestParam(required = true) String cepDestino,
                                                 @RequestParam(required = true) String nomeDestinatario){
        return ResponseEntity.ok().body(freteService.consultar(peso, cepOrigem, cepDestino, nomeDestinatario));
    }
}
