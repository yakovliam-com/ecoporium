package net.ecoporium.ecoporium.screen.renderer;

import com.github.johnnyjayjay.spigotmaps.rendering.RenderContext;
import com.github.johnnyjayjay.spigotmaps.util.Checks;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public abstract class AbstractMapImageRenderer extends MapRenderer {

    protected Point startingPoint;
    private final Set<RenderContext> alreadyReceived;
    private final boolean renderForAllPlayers;
    private final boolean renderOnce;
    private final Set<Player> receivers;
    private final Predicate<RenderContext> precondition;
    private boolean stop;

    /**
     * If the image should render
     * <p>
     * Prevents lag by rendering once until the image is actually updated
     */
    protected boolean shouldRenderImage;

    protected AbstractMapImageRenderer(Point startingPoint, Set<Player> receivers, boolean renderOnce) {
        super(!receivers.isEmpty());
        this.startingPoint = startingPoint;
        this.renderForAllPlayers = receivers.isEmpty();
        this.receivers = receivers;
        this.renderOnce = renderOnce;
        this.precondition = c -> this.shouldRenderImage;
        this.alreadyReceived = new HashSet<>();
        this.stop = false;
        this.shouldRenderImage = false;
    }

    public final void render(MapView map, MapCanvas canvas, Player player) {
        RenderContext context = RenderContext.create(map, canvas, player);
        if (this.mayRender(context)) {
            this.render(context);
            if (this.renderOnce) {
                this.alreadyReceived.add(context);
            }

            // set should render to false now that we've rendered the update
            this.shouldRenderImage = false;
        }

    }

    private boolean mayRender(RenderContext context) {
        return !this.stop && (this.renderForAllPlayers || this.receivers.contains(context.getPlayer())) && (!this.renderOnce || !this.alreadyReceived.contains(context)) && this.precondition.test(context);
    }

    public void addReceiver(Player receiver) {
        Checks.checkNotNull(receiver, "Receiver");
        this.receivers.add(receiver);
    }

    public boolean removeReceiver(Player receiver) {
        Checks.checkNotNull(receiver, "Receiver");
        return this.receivers.remove(receiver);
    }

    public Set<Player> getReceivers() {
        return Collections.unmodifiableSet(this.receivers);
    }

    public Point getStartingPoint() {
        return new Point(this.startingPoint);
    }

    public void setStartingPoint(Point startingPoint) {
        Checks.checkStartingPoint(startingPoint);
        this.startingPoint = new Point(startingPoint);
    }

    public boolean isRenderOnce() {
        return this.renderOnce;
    }

    public void stopRendering() {
        this.stop = true;
    }

    public boolean isStopped() {
        return this.stop;
    }

    protected abstract void render(RenderContext var1);

    protected abstract static class Builder<T extends AbstractMapImageRenderer, U extends AbstractMapImageRenderer.Builder<T, U>> {
        protected final Set<Player> receivers = new HashSet<>();
        protected boolean renderOnce = true;
        protected Point startingPoint = new Point();

        protected Builder() {
        }

        public abstract T build();

        protected void check() {
            Checks.checkStartingPoint(this.startingPoint);
        }

        public U addPlayers(Collection<Player> players) {
            this.receivers.addAll(players);
            return (U) this;
        }

        public U addPlayers(Player... players) {
            return (U) this.addPlayers((Collection<Player>) Arrays.asList(players));
        }

        public U renderOnce(boolean renderOnce) {
            this.renderOnce = renderOnce;
            return (U) this;
        }

        public U startingPoint(Point point) {
            this.startingPoint = new Point(point);
            return (U) this;
        }
    }
}