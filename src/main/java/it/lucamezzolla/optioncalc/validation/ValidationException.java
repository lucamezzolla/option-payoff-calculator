package it.lucamezzolla.optioncalc.validation;

public final class ValidationException extends IllegalArgumentException {

    private final String messageKey;
    private final Object[] arguments;

    public ValidationException(String messageKey, Object... arguments) {
        super(messageKey);
        this.messageKey = messageKey;
        this.arguments = arguments == null ? new Object[0] : arguments.clone();
    }

    public String messageKey() {
        return messageKey;
    }

    public Object[] arguments() {
        return arguments.clone();
    }
}
