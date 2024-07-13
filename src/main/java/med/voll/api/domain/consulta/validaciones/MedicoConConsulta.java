package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicoConConsulta implements ValidadorDeConsultas {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosAgendarConsulta datos){
        if (datos.idMedico() == null){
            return;
        }
        var medicoComConsulta = repository.existsByMedicoIdAndFecha(datos.idMedico(), datos.fecha());
        if (medicoComConsulta) {
            throw new ValidationException("El medico ya tiene una consulta en ese horario");
        }
    }
}
