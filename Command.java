public enum Command {
    HELP,
    ADD,
    REMOVE,
    STOCK,
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

//Commands to add:
//LOAD - loads config file with products
//SAVE - saves products to config file. Will also do this automatically every time the program shuts down
