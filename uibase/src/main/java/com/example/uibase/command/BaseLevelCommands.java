package com.example.uibase.command;


import com.example.uibase.utils.WebConstants;

public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
    }

    @Override
    int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
