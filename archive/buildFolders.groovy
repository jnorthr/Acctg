tmpDir = File.createTempDir()
def fileTreeBuilder = new FileTreeBuilder(tmpDir)
fileTreeBuilder.jimbo{
src {
    main {
       groovy {
          'Foo.groovy'('println "Hello"')
       }
    }
    test {
       groovy {
          'FooTest.groovy'('class FooTest extends GroovyTestCase {}')
       }
    }
 }
}