package bg.sofia.uni.fmi.mjt.burnout.exception;

import javax.swing.table.TableRowSorter;

public class DisappointmentException extends Throwable{
    public DisappointmentException(String message) {
        super(message);
    }

    public DisappointmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
