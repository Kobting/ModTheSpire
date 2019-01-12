package com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels;

import com.evacipated.cardcrawl.modthespire.LoadOrder;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.steam.SteamSearch;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.ui.fx.views.ModItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class ViewModel {

    private static String MOD_DIR = "mods/";
    private ModInfo[] modInfo;
    private ObservableList<ModItem> modItems = FXCollections.observableArrayList();
    private ArrayList<SteamSearch.WorkshopInfo> workshopInfos;
    private ArrayList<File> modJars = new ArrayList<>();

    public ViewModel(){
        initModItems();
    }

    private void initModItems() {
        modInfo = Loader.getAllMods();
        ArrayList<LoadOrder.ModDescriptor> modDescriptors = LoadOrder.getModsInOrder(modInfo);
        modDescriptors.forEach(modDescriptor -> {
            ModItem item = new ModItem();
            item.setModName(modDescriptor.info.Name);
            item.setModVersion(modDescriptor.info.ModVersion.getValue());
            item.setActive(modDescriptor.checked);
            modItems.add(item);
        });
    }

    public ObservableList<ModItem> getModItems(){
        return this.modItems;
    }

    public void launchSTS(Boolean debug) {
        Loader.DEBUG = debug;
        ArrayList<File> modFiles = new ArrayList<>();
        LoadOrder.getModsInOrder(modInfo).forEach(modDescriptor -> {
            if(modDescriptor.checked) {
                modFiles.add(modDescriptor.mod);
            }
        });
        Loader.runMods(modFiles.toArray(new File[0]));
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
