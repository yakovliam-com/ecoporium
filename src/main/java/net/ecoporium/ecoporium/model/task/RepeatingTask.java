package net.ecoporium.ecoporium.model.task;

import net.ecoporium.ecoporium.EcoporiumPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class RepeatingTask implements Runnable {

    /**
     * Default start delay
     */
    private static final long DEFAULT_DELAY = 0L;

    /**
     * Ecoporium plugin
     */
    protected final EcoporiumPlugin plugin;

    /**
     * Period, in ticks
     */
    private final long period;

    /**
     * Current task state
     */
    private TaskState state;

    /**
     * Asynchronous?
     */
    private final boolean async;

    /**
     * The given task id
     */
    private int taskId;

    /**
     * Repeating task
     *
     * @param plugin plugin
     * @param period period
     */
    public RepeatingTask(EcoporiumPlugin plugin, long period, boolean async) {
        this.plugin = plugin;
        this.period = period;
        this.state = TaskState.IDLE;
        this.async = async;
    }

    /**
     * Run method
     * <p>
     * Implemented by children
     */
    public abstract void run();

    /**
     * Starts the task
     */
    public void start() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        // if running
        if (this.state == TaskState.RUNNING) {
            // cancel self
            scheduler.cancelTask(taskId);
        }

        // register
        if (async) {
            this.taskId = scheduler.runTaskTimerAsynchronously(plugin, this, DEFAULT_DELAY, this.period).getTaskId();
        } else {
            this.taskId = scheduler.runTaskTimer(plugin, this, DEFAULT_DELAY, this.period).getTaskId();
        }

        // set state to running
        this.state = TaskState.RUNNING;
    }

    /**
     * Stops the task
     */
    public void stop() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        // if running
        if (this.state == TaskState.RUNNING) {
            // cancel self
            scheduler.cancelTask(taskId);
        }
    }
}