package net.ecoporium.ecoporium.session.screen;

import net.ecoporium.ecoporium.session.Session;
import net.ecoporium.ecoporium.ticker.TickerScreen;
import net.ecoporium.ecoporium.ticker.info.ScreenPositionalInfo;

import java.util.UUID;

public class TickerScreenCreatorSession extends Session<TickerScreen> {

    /**
     * Positional info
     */
    private ScreenPositionalInfo screenPositionalInfo;

    /**
     * Session
     *
     * @param uuid uuid
     */
    protected TickerScreenCreatorSession(UUID uuid) {
        super(uuid);
    }

    @Override
    public boolean isComplete() {
        return screenPositionalInfo != null &&
                screenPositionalInfo.getCornerOne() != null &&
                screenPositionalInfo.getCornerTwo() != null;
    }

    /**
     * Returns positional info
     *
     * @return position info
     */
    public ScreenPositionalInfo getScreenPositionalInfo() {
        return screenPositionalInfo;
    }

    /**
     * Sets positional info
     *
     * @param screenPositionalInfo positional info
     */
    public void setScreenPositionalInfo(ScreenPositionalInfo screenPositionalInfo) {
        this.screenPositionalInfo = screenPositionalInfo;
    }
}
