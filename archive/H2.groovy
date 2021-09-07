//@GrabConfig(systemClassLoader=true)
//@Grab(group='com.h2database', module='h2', version='1.3.176')
import java.util.Date;
import groovy.sql.Sql
import java.util.Random  

Random rand = new Random() 
int max = 10  

Date datex = new Date();
datex+=7;

/** an O/S specific location for the user's home folder name */ 
String home = System.getProperty("user.home");    

/** an O/S specific location for the user's H2 database */ 
String address = "jdbc:h2:file:${home}/Dropbox/Projects/Acctg/.h2data/acctg;AUTOCOMMIT=ON"; 

def sql = Sql.newInstance(address, "sa", "sa", "org.h2.Driver")
sql.execute("drop table IF EXISTS test")

sql.execute("CREATE TABLE IF NOT EXISTS test (id int auto_increment, value text, dt date)")  // IF NOT EXISTS test 

rowNum = rand.nextInt(max)
sql.execute("INSERT INTO test VALUES(:id, :value, :dt)", [value: 'the LAZY turd jumped over the brown dog.',dt:datex])

println "select * from test"
sql.eachRow('SELECT * FROM test') { row ->
  println "id=${row.id} value=${row.value}  dt=${row.dt}  ran=${rowNum}"
}


println '--- the end ---'
//=============================
