package datatypes;

import java.util.UUID;

public class $BNNR {
    public UUID BannerID;
    public String OriginalOwnerName;
    public UUID OriginalOwnerID;
    public boolean Spent;
    public String SpentOn;
    public UUID SpenderOwnerID;
    public String SpenderOwnerName;

    public $BNNR() {
        this.BannerID = new UUID(0L, 0L);
        this.OriginalOwnerName = "Notch";
        this.OriginalOwnerID = new UUID(0L, 0L);
        this.Spent = false;
        this.SpentOn = "";
        this.SpenderOwnerID = new UUID(0L, 0L);
        this.SpenderOwnerName = "";
    }

    public $BNNR(UUID BannerID, String OriginalOwnerName, UUID OriginalOwnerID, boolean Spent, String SpentOn, UUID SpenderOwnerID, String SpenderOwnerName) {
        this.BannerID = BannerID;
        this.OriginalOwnerName = OriginalOwnerName;
        this.OriginalOwnerID = OriginalOwnerID;
        this.Spent = Spent;
        this.SpentOn = SpentOn;
        this.SpenderOwnerID = SpenderOwnerID;
        this.SpenderOwnerName = SpenderOwnerName;
    }
}
