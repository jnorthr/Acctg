import javax.swing.*;

boolean ask()
{
    int choice = JOptionPane.showOptionDialog(null,
        "ya wanna quit ?",
        "Quit",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null, null, null);

        if (choice == JOptionPane.YES_OPTION)
        {
            println "yes"
        }
        else
        {
            println "no"
        }
} // end of ask
