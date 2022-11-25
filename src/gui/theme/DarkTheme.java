package gui.theme;

import com.formdev.flatlaf.FlatDarculaLaf;

public class DarkTheme extends FlatDarculaLaf {
    public static boolean setup() {
        return setup(new DarkTheme());
    }

    @Override
    public String getName() {
        return "DarkTheme";
    }
}
