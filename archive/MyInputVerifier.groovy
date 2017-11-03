import javax.swing.InputVerifier;
import javax.swing.*
// myTextField.setInputVerifier(new MyInputVerifier());

public class MyInputVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        try 
        {
            if (text.trim().size() < 1) return true;
            else
            {
                BigDecimal value = new BigDecimal(text);
                return (value.scale() <= Math.abs(4)); 
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Field must be blank or a number like 123.45", "Invalid Value", JOptionPane.ERROR_MESSAGE);
            return false;
        } //end of catch
    } // end of method
} // end of class