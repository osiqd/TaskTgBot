package app.oprosnik.TelegramBot;

import io.github.exortions.dotenv.DotEnv;
import io.github.exortions.dotenv.EnvParameterNotFoundException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;

public class StartApp {
    public static void main(String[] args) throws TelegramApiException, EnvParameterNotFoundException {
        DotEnv dotEnv = new DotEnv(new File(".env"));
        dotEnv.loadParams();
        TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
        botApi.registerBot(new Bot(dotEnv.getParameter("BOT_TOKEN")));
    }
}