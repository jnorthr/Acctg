brackets = 0;
list=[]; 
String projectDir ="/Users/jimnorthrop/Dropbox/Projects/Acctg";

def jsonText = """
    ${projectDir} { 
        README.md()
        .gitignore
        .travis.yml
        src: {
            main{
                groovy{
                    Tools.groovy
                }
                resources{}
            }
            test {
                ToolsTest.groovy
            }
        }
        
        'images'{}
    }       
""".toString();

def see()
{
    print "[${brackets}]"
    //print tfField;
    tfField="";
} // end of see


boolean tf = false;
tfField = "";

boolean flag = false;
String flagfield="";

boolean flag2 = false;
String flag2field="";

see();
jsonText.trim().each{e->
    e.each{ea->
        switch(ea)
        {
            case '"' :  see();
                        if (flag) { flagfield+='"'; print flagfield; flagfield=""; flag=false; } else { flag=true; flagfield='"'; }
                        break;

            case "'" :  see();
                        if (flag2) { flag2field+="'"; print flag2field; flag2field=""; flag2=false; } else { flag2=true; flag2field="'"; }
                        break;

            case '{' :  brackets++;
                        print '{';
                        see();                        
                        break;
                        
            case '}' :  brackets--;
                        print '}';
                        see();                        
                        break;
            case ':' :  break;

/*
            case ':' :  see();
                        print ':';
                        break;
*/                        
                        
            case ' ' :  if (flag) {flagfield+=' ';}
                        else
                        if (flag2) {flag2field+=' ';}
                        break;
            
            //case '\n' : break;
            
            default:    if (flag) 
                        { 
                            flagfield+=ea; 
                        }
                        else
                        if (flag2) 
                        { 
                            flag2field+=ea; 
                        }
                        else
                        {
                            tfField += ea;
                            print ea;
                        }
                        break;               
        }
    } // end of each
} // end of each

println "\n--- the end ==="