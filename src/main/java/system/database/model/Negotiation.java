package system.database.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Negotiation {

    public enum Campos {
        NEGOTIATION_ID,
        TYPE,
        STATUS,
        ROUNDS,
        CREATED_BY,
        UPDATED_BY,
        CREATED_DATE,
        UPDATED_DATE,
        HAS_CONVOCATION,
        COUNT_EMAIL,
        OPENING_DATE,
        CLOSING_DATE,
        CAN_IMPORT_FILE,
        ACCEPT_SEND_DATE
    }

    public static final String NOME_TABELA = "NEGOTIATION";

    private String negotiationId;
    private String type;
    private String status;
    private String rounds;
    private String createdBy;
    private String updatedBy;
    private String createdDate;
    private String updatedDate;
    private String hasConvocation;
    private String countEmail;
    private String openingDate;
    private String closingDate;
    private String canImportFile;
    private String acceptSendDate;

    public String[] getCampos() {
        String[] campos = new String[14];
        campos[0] = negotiationId;
        campos[1] = type;
        campos[2] = status;
        campos[3] = rounds;
        campos[4] = createdBy;
        campos[5] = updatedBy;
        campos[6] = createdDate;
        campos[7] = updatedDate;
        campos[8] = hasConvocation;
        campos[9] = countEmail;
        campos[10] = openingDate;
        campos[11] = closingDate;
        campos[12] = canImportFile;
        campos[13] = acceptSendDate;
        return campos;
    }
}
