package org.firedragon91245.automaton;

import java.io.File;

public class GameSettingsSaveEventArgs {
    private Exception ex;
    private String msg;
    private GameSettings settingsInstance;
    private File f;

    public GameSettingsSaveEventArgs(Status status) {
    }

    public GameSettingsSaveEventArgs setException(Exception e) {
        this.ex = e;
        return this;
    }

    public GameSettingsSaveEventArgs setMessage(String message) {
        this.msg = message;
        return this;
    }

    public GameSettingsSaveEventArgs setSettingsInstance(GameSettings gameSettings) {
        this.settingsInstance = gameSettings;
        return this;
    }

    public GameSettingsSaveEventArgs setFile(File f) {
        this.f = f;
        return this;
    }

    public enum Status {
        ERROR,
        SUCCESS
    }
}
