public enum Command {
    HELP,
    ADD,
    REMOVE,
    STOCK,
    CATALOG,
    EDIT,
    ORDER,
    SAVE,
    LOAD,
    PATH,
    EXIT;

    public static Command fromString(String command) {
        try {
            return Command.valueOf(command.toUpperCase());
        } 
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
