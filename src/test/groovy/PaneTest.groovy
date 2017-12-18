/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
import spock.lang.Specification
//import com.jim.toolkit.database.LoaderSupport;
import static java.util.Calendar.*
import com.jim.toolkit.gui.Pane;

class PaneTest extends Specification {
    def "Simple Pane constructor returns proper name of class"() {
        setup:
	        Pane obj = new Pane();
		
        when:
        	def result = obj.classname

        then:
        	result == "Pane"
    } // end of method

} // end of class
