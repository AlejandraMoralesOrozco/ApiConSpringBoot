package med.voll.api.infra.errores;

public class ValidacionDeIntegridad extends RuntimeException {

    public ValidacionDeIntegridad(String e) {
        super(e);
    }
}
