package gui.theme.dark;

import com.formdev.flatlaf.FlatDarkLaf;

public class DarkTheme extends FlatDarkLaf {
    public static boolean setup() {
        return setup(new DarkTheme());
    }

    @Override
    public String getName() {
        return "DarkTheme";
    }
}
