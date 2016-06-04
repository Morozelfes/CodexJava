package fr.manastria.codexjava;

import Controller.Controller;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <h1>Codex for "reflets d'acide"</h1>
 * <p> The 'reflets d'acide' codex program implements an application that
 * generates a codex window which allows consultation and modification of
 * data based on 'Reflets d'acide' characters.</p>
 */
public class App {

    /**
     * @param args Unused
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Controller control = new Controller();
    }
}
