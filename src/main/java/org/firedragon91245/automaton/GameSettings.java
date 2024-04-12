package org.firedragon91245.automaton;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameSettings {

    private final ArrayList<SaveOperationCallback> saveOperationsCallback = new ArrayList<>();

    public void SafeGameSettings(File f)
    {
        if(f.canWrite())
        {
            Gson gson = new Gson();

            FileWriter writer = null;

            try {
                writer = new FileWriter(f, false);
            } catch (IOException e) {
                invokeSaveOperationCallback(new GameSettingsSaveEventArgs(GameSettingsSaveEventArgs.Status.ERROR).setException(e).setMessage("Failed to save game settings").setSettingsInstance(this).setFile(f));
                return;
            }

            try {
                gson.toJson(this, writer);
            }
            catch (JsonIOException e)
            {
                invokeSaveOperationCallback(new GameSettingsSaveEventArgs(GameSettingsSaveEventArgs.Status.ERROR).setException(e).setMessage("Failed to save game settings").setSettingsInstance(this).setFile(f));
                return;
            }

            try {
                writer.close();
            } catch (IOException e) {
                invokeSaveOperationCallback(new GameSettingsSaveEventArgs(GameSettingsSaveEventArgs.Status.ERROR).setException(e).setMessage("Failed to save game settings").setSettingsInstance(this).setFile(f));
            }

            invokeSaveOperationCallback(new GameSettingsSaveEventArgs(GameSettingsSaveEventArgs.Status.SUCCESS));
        }
        else
        {
            invokeSaveOperationCallback(new GameSettingsSaveEventArgs(GameSettingsSaveEventArgs.Status.ERROR).setMessage("Cannot write to file").setSettingsInstance(this).setFile(f));
        }
    }

    private void invokeSaveOperationCallback(GameSettingsSaveEventArgs args) {
        for(SaveOperationCallback r : saveOperationsCallback)
        {
            try {
                r.onSave(args);
            } catch (Exception ignored)
            {

            }
        }
    }

    public void addSaveOperationCallback(SaveOperationCallback r)
    {
        saveOperationsCallback.add(r);
    }

}
