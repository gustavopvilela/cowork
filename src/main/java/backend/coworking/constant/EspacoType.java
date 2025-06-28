package backend.coworking.constant;

public enum EspacoType {
    MESA_INDIVIDUAL("Mesa individual"),
    SALA_REUNIAO("Sala de reunião"),
    ESCRITORIO("Escritório"),
    CABINE_PARA_CHAMADAS("Cabine para chamadas"),
    SALA_COM_COMPUTADORES("Sala com computadores");

    private String description;

    private EspacoType(String description) {
        this.description = description;
    }
}
