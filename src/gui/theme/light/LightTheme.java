package gui.theme.light;

import com.formdev.flatlaf.FlatLightLaf;

public class LightTheme extends FlatLightLaf {
    public static boolean setup() {
        return setup(new LightTheme());
    }

    @Override
    public String getName() {
        return "LightTheme";
    }
}
