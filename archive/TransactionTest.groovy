import groovy.swing.SwingBuilder 
import groovy.beans.Bindable 
import static javax.swing.JFrame.EXIT_ON_CLOSE 
import java.awt.*
import javax.swing.JFrame;
import javax.swing.*;          
import java.awt.event.*;
import javax.swing.plaf.metal.*;



    String client, amount, reason
    JFrame frame = new JFrame();
    def swingBuilder = new SwingBuilder() 
    swingBuilder.edt {        // edt method makes sure UI is build on Event Dispatch Thread.
        //lookAndFeel "nimbus"  // Simple change in look and feel.
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }

println "--- the end --"