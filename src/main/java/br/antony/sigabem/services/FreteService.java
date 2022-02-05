package br.antony.sigabem.services;

import br.antony.sigabem.entities.FreteEntity;
import br.antony.sigabem.entities.ViacepEntity;
import br.antony.sigabem.repositories.FreteRepository;
import br.antony.sigabem.services.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class FreteService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private FreteRepository freteRepository;

    public FreteEntity consultar(double peso, String cepOrigem, String cepDestino, String nomeDestinatario) {
        Mono<ViacepEntity> monoViacepOrigim = webClient
                .method(HttpMethod.GET)
                .uri("https://viacep.com.br/ws/{cep}/json/", cepOrigem)
                .retrieve()
                .onStatus(HttpStatus::isError, reponse ->{
                    return Mono.error(new BadRequestException("cepOrigem inválido"));
                })
                .bodyToMono(ViacepEntity.class);

        Mono<ViacepEntity> monoViacepDestino = webClient
                .method(HttpMethod.GET)
                .uri("https://viacep.com.br/ws/{cep}/json/", cepDestino)
                .retrieve()
                .onStatus(HttpStatus::isError, reponse ->{
                    return Mono.error(new BadRequestException("cepDestino inválido"));
                })
                .bodyToMono(ViacepEntity.class);

        ViacepEntity viacepOrigem = monoViacepOrigim.block();
        ViacepEntity viacepDestino = monoViacepDestino.block();

        if(viacepOrigem.isErro()){
            throw new BadRequestException("cepOrigem inválido");
        }

        if(viacepDestino.isErro()){
            throw new BadRequestException("cepDestino inválido");
        }

        FreteEntity frete = new FreteEntity();
        frete.setNomeDestinatario(nomeDestinatario);
        frete.setCepOrigem(cepOrigem);
        frete.setCepDestino(cepDestino);
        frete.setDataConsulta(LocalDate.now());


        if(viacepOrigem.getUf().equalsIgnoreCase(viacepDestino.getUf())){
            frete.setDataPrevistaEntrega(LocalDate.now().plusDays(3));
            frete.setVlTotalFrete(peso - ( (peso * 75) /100) );
        }

        if(viacepOrigem.getDdd().equalsIgnoreCase(viacepDestino.getDdd())){
            frete.setDataPrevistaEntrega(LocalDate.now().plusDays(1));
            frete.setVlTotalFrete(peso - ( (peso * 50) /100) );
        }

        if(!viacepOrigem.getUf().equalsIgnoreCase(viacepDestino.getUf())){
            frete.setDataPrevistaEntrega(LocalDate.now().plusDays(10));
            frete.setVlTotalFrete(peso);
        }

        return freteRepository.save(frete);
    }
}
