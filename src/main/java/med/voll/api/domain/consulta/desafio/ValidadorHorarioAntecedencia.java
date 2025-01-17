package med.voll.api.domain.consulta.desafio;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("validadorHorarioAntecedenciaCancelamiento")
public class ValidadorHorarioAntecedencia implements ValidadorCancelamientoDeConsulta{

    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DatosCancelamientoConsulta datos){
        var consulta = repository.getReferenceById(datos.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaEnHoras = Duration.between(ahora, consulta.getFecha()).toHours();

        if(diferenciaEnHoras < 24){
            throw new ValidationException("La consulta solo puede cancelarce con 24 horas de anticipacion");
        }
    }
}
