// Most simple implementation of toString.
public Date cvt(String s) 
{
    Date dat = Date.parse('yyy-MM-dd',s);
}

def cells=[]
Date dt = cvt('2017-01-01');

Cell c = new Cell([d:dt, type:0, client:0, purpose:'Start',amount:-123.45])
cells+=c;
def tx2 = c.toOutput();
print tx2;
def tx2s = tx2.split(';')
println " tx2s.size()="+tx2s.size();


dt = cvt('2017-05-24');
c = new Cell([d:dt, type:2,client:0, purpose:'fuel',amount:423.45])
cells+=c;


dt = cvt('2017-04-25');
c = new Cell([d:dt, type:1,client:0,purpose:'pension',amount:121.55])
cells+=c;


dt +=7;
c = new Cell([d:dt, type:1,client:0,purpose:'pension',amount:121.55])
cells+=c;

def sortedByDate = cells.toSorted { a, b -> a.d <=> b.d }
def balance=0;
def file3 = new File('Acctg.txt')

// Or a writer object:
file3.withWriter('UTF-8') { writer ->
  sortedByDate.each{e->
        switch(e.type)
        {
            case  2: balance -= e.amount;
                     break;
            case  0: balance = e.amount;
                     break;
            case  1: balance += e.amount;
                     break;
        } // end of switch
        println e.toString()+" = "+balance;
        writer.write(e.toOutput()+'\n')
  } // end of each
} // end of file3

println "--- the end ---"