public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Test");
        if (!(command.getName().equalsIgnoreCase(label))) {
            sender.sendMessage("Ran test command with alias: \"" + label + "!\"");
        }
        return true;
    }
}