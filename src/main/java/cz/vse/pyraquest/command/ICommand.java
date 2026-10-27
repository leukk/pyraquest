package cz.vse.pyraquest.command;

public interface ICommand {
    String getName();

    String execute(String... parameters);
}
