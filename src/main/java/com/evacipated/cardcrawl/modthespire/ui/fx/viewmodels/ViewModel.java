package com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels;

import com.evacipated.cardcrawl.modthespire.steam.SteamSearch;
import com.evacipated.cardcrawl.modthespire.ui.fx.models.ModInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ViewModel {

    private static String MOD_DIR = "mods/";
    private ObservableList<ModInfo> modItems = FXCollections.observableArrayList();
    private ArrayList<SteamSearch.WorkshopInfo> workshopInfos;

    public ViewModel(){
        //initModItems();
    }

    private void initModItems() {
        workshopInfos = initWorkshopInfos();
        modItems.addAll(getAllMods(workshopInfos)); //TODO make this asynchronous?
    }

    public ObservableList<ModInfo> getModItems(){
        return this.modItems;
    }

    public void launchSTS(Boolean debug) {

        if(debug) {
            //TODO launch STS with mods patched and loaded with debug window
        } else {
            //TODO launch STS with mods patched and loaded without debug window
        }
    }

    public void searchForUpdates() {
        //TODO search for updates
    }

    public void openWorkshopUrl(){
        //TODO launch and open workshop in the browser? Steam?
    }

    private ArrayList<SteamSearch.WorkshopInfo> initWorkshopInfos() {

        //TODO convert how its done in Loader to here.
        return null;
    }

    // getAllModFiles - returns a File array containing all of the JAR files in the mods directory
    private File[] getAllModFiles(String directory)
    {
        File file = new File(directory);
        if (!file.exists() || !file.isDirectory()) {
            return new File[0];
        }

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar");
            }
        });

        if (files == null || files.length == 0) {
            return new File[0];
        }
        return files;
    }

    private ModInfo[] getAllMods(List<SteamSearch.WorkshopInfo> workshopInfos)
    {
        List<ModInfo> modInfos = new ArrayList<>();

        // "mods/" directory
        for (File f : getAllModFiles(MOD_DIR)) {
            ModInfo info = ModInfo.ReadModInfo(f);
            if (info != null) {
                if (modInfos.stream().noneMatch(i -> i.ID == null || i.ID.equals(info.ID))) {
                    modInfos.add(info);
                }
            }
        }

        // Workshop content
        for (SteamSearch.WorkshopInfo workshopInfo : workshopInfos) {
            for (File f : getAllModFiles(workshopInfo.getInstallPath().toString())) {
                ModInfo info = ModInfo.ReadModInfo(f);
                if (info != null) {
                    // Disable the update json url for workshop content
                    info.UpdateJSON = null;
                    info.isWorkshop = true;

                    // If the workshop item is a newer version, use it instead of the local mod
                    boolean doAdd = true;
                    Iterator<ModInfo> it = modInfos.iterator();
                    while (it.hasNext()) {
                        ModInfo modInfo = it.next();
                        if (modInfo.ID != null && modInfo.ID.equals(info.ID)) {
                            if (modInfo.ModVersion == null || info.ModVersion == null) {
                                doAdd = false;
                                break;
                            }
                            if (info.ModVersion.isGreaterThan(modInfo.ModVersion)) {
                                it.remove();
                            } else {
                                doAdd = false;
                                break;
                            }
                        }
                    }
                    if (doAdd) {
                        modInfos.add(info);
                    }
                }
            }
        }

        modInfos.sort(Comparator.comparing(m -> m.Name));

        return modInfos.toArray(new ModInfo[0]);
    }
}
