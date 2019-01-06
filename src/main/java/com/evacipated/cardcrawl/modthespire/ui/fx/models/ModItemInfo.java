package com.evacipated.cardcrawl.modthespire.ui.fx.models;

public class ModItemInfo {

    private String modName;
    private String modVersion;
    private String modIcon = "assets/workshop.gif";

    public ModItemInfo(String modName, String modVersion) {
        this.modName = modName;
        this.modVersion = modVersion;

    }

    public ModItemInfo(String modName, String modVersion, String modIcon) {
        this(modName, modVersion);
        this.modIcon = modIcon;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getModVersion() {
        return modVersion;
    }

    public void setModVersion(String modVersion) {
        this.modVersion = modVersion;
    }

    public String getModIcon() {
        return modIcon;
    }

    public void setModIcon(String modIcon) {
        this.modIcon = modIcon;
    }
}
