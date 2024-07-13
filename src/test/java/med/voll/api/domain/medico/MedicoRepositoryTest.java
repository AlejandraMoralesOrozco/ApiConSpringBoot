package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //indica que vamos a trabajar con persistencia de base de datos
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //realizar operaciones con un db externa y no vamos a reemplazar las que estamos utilizando
@ActiveProfiles("test") //indicamos el perfil que vamos a utilizar
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deberia retornar nulo cuando el medico se encuentre en consulta con otro paciente")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {
        var proximoLunes10am = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = registrarMedico("Jose", "jose@mail.com", "12345", Especialidad.CARDIOLOGIA);
        var paciente = registraPaciente("Celia", "celia@mail.com", "345678");
        registraConsulta(medico, paciente, proximoLunes10am);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA, proximoLunes10am);

        assertThat(medicoLibre).isNull();
    }

    @Test
    @DisplayName("Deberia retornar un medico cuando realice la consulta en la bd para ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {
        var proximoLunes10am = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = registrarMedico("Jose", "jose@mail.com", "12345", Especialidad.CARDIOLOGIA);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA, proximoLunes10am);

        assertThat(medicoLibre).isEqualTo(medico);
    }

    private void registraConsulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        em.persist(new Consulta(null, medico, paciente, fecha, null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad) {
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        em.persist(medico);
        return medico;
    }

    private Paciente registraPaciente(String nombre, String email, String documento) {
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private DatosRegistroPaciente datosPaciente( String nombre, String email, String documento) {
        return new DatosRegistroPaciente(
                nombre,
                email,
                "3432105678",
                documento,
                datosDireccion()
        );
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad) {
        return new DatosRegistroMedico(
                nombre,
                email,
                "8765456723",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                "calle 1",
                "1",
                "distrito 1",
                "CDMX",
                "A"
        );
    }


}