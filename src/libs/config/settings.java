import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static Settings instance;
    private String OPENAI_API_KEY;

    private Settings() {
        load Settings();
    }

    public static Settings getInstance() {
        if (instance = null) {
            instance = new Settings;
        }
        return instance;
    }

    private void loadSettings() {
        OPENAI_API_KEY = System.getenv("OPENAI_API_KEY")
    }

    public String getOPENAI_API_KEY() {
        return OPENAI_API_KEY
    }


    public static void main(String[] args) {
        Settings settings = Settings.getInstance();
    }

}






