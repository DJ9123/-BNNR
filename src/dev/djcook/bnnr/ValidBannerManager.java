package dev.djcook.bnnr;

import java.io.*;

import datatypes.$BNNR;

import java.util.HashMap;
import java.util.UUID;

public final class ValidBannerManager {
    private final HashMap<UUID, $BNNR> bannerHash  = new HashMap<>();
    private final File bannersFile;
    private static final Object fileWritingLock = new Object();

    protected ValidBannerManager() {
        bannersFile = new File(Bnnr.getBannersFilePath());
    }

    public void newBanner($BNNR banner) {
        BufferedWriter out = null;
        synchronized (fileWritingLock) {
            try {
                out = new BufferedWriter(new FileWriter(Bnnr.getBannersFilePath(), true));

                out.append(banner.BannerID.toString()).append(":");
                out.append(banner.OriginalOwnerName).append(":");
                out.append(banner.OriginalOwnerID.toString()).append(":");
                out.append(Boolean.toString(banner.Spent)).append(":");
                out.append(banner.SpentOn).append(":");
                out.append(banner.SpenderOwnerID.toString()).append(":");
                out.append(banner.SpenderOwnerName);

                out.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                tryClose(out);
            }
        }
    }

    private void tryClose(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
