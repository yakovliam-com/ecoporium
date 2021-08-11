package net.ecoporium.ecoporium.config;

import net.ecoporium.ecoporium.api.config.generic.KeyedConfiguration;
import net.ecoporium.ecoporium.api.config.generic.key.ConfigKey;
import net.ecoporium.ecoporium.api.config.generic.key.SimpleConfigKey;
import net.ecoporium.ecoporium.api.wrapper.Pair;
import net.ecoporium.ecoporium.util.ColorUtil;

import java.awt.*;
import java.util.List;

import static net.ecoporium.ecoporium.api.config.generic.key.ConfigKeyFactory.key;

/**
 * All of the {@link ConfigKey}s used by Space[Plugin Name Here].
 *
 * <p>The {@link #getKeys()} method and associated behaviour allows this class
 * to function a bit like an enum, but with generics.</p>
 */
public final class EcoporiumConfigKeys {

    public static ConfigKey<String> CHART_TITLE = key(c -> c.getString("chart.title", null));
    public static ConfigKey<String> CHART_CATEGORY_AXIS_LABEL = key(c -> c.getString("chart.category-axis-label", null));
    public static ConfigKey<String> CHART_VALUE_AXIS_LABEL = key(c -> c.getString("chart.value-axis-label", null));
    public static ConfigKey<Boolean> CHART_DOMAIN_AXIS_VISIBLE = key(c -> c.getBoolean("chart.domain-axis.visible", false));
    public static ConfigKey<Pair<Double, Double>> CHART_RANGE_LOWER_HIGHER = key(c -> new Pair<>(c.getDouble("chart.range.lower", 0.99), c.getDouble("chart.range.higher", 1.01)));
    public static ConfigKey<Color> CHART_CHART_BACKGROUND_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.chart-background-paint", "")));
    public static ConfigKey<Color> CHART_CHART_TITLE_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.chart-title-paint", "")));
    public static ConfigKey<Color> CHART_CHART_LEGEND_BACKGROUND_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.chart-legend-background-paint", "")));
    public static ConfigKey<Color> CHART_CHART_LEGEND_TEXT_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.chart-legend-text-paint", "")));
    public static ConfigKey<Color> CHART_PLOT_BACKGROUND_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.plot-background-paint", "")));
    public static ConfigKey<Color> CHART_PLOT_DOMAIN_GRIDLINE_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.plot-domain-gridline-paint", "")));
    public static ConfigKey<Color> CHART_PLOT_RANGE_GRIDLINE_PAINT = key(c -> ColorUtil.parseColor(c.getString("chart.plot-range-gridline-paint", "")));
    public static ConfigKey<Color> CHART_STOCK_GOING_UP_COLOR = key(c -> ColorUtil.parseColor(c.getString("chart.stock-going-up-color", "")));
    public static ConfigKey<Color> CHART_STOCK_GOING_DOWN_COLOR = key(c -> ColorUtil.parseColor(c.getString("chart.stock-going-down-color", "")));
    public static ConfigKey<Color> CHART_STOCK_NEUTRAL_COLOR = key(c -> ColorUtil.parseColor(c.getString("chart.stock-neutral-color", "")));
    public static ConfigKey<Double> CHART_STOCK_LINE_THICKNESS = key(c -> c.getDouble("stock-line-thickness", 5.0));


    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(EcoporiumConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
