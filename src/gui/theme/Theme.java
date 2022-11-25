package gui.theme;

import com.formdev.flatlaf.FlatDarkLaf;

public class Theme extends FlatDarkLaf {
    public static boolean setup() {
        return setup(new Theme());
    }

    @Override
    public String getName() {
        return "Theme";
    }
}
