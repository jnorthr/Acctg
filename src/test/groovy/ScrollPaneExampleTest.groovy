/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
import spock.lang.Specification
import com.jim.toolkit.gui.ScrollPaneExample

class ScrollPaneExampleTest extends Specification {

    def "Simple ScrollPaneExample constructor returns proper stuff"() {
        setup:
            ScrollPaneExample obj = null
		
        when:
            obj = new ScrollPaneExample()
		
        then:
        	obj != null;
    } // end of test
    
    
    def "Simple ScrollPaneExample constructor using Map"() {
        setup:
			ScrollPaneExample obj = new ScrollPaneExample();

        when:
			int high = obj.height;
            println "ScrollPaneExample().toString() = [${obj.toString()}]"

        then:
			high != 1		
            obj.width != 13	
    } // end of test
    
} // end of class
