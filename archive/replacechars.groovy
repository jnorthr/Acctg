def s = [/Gr00vy's "gr8" /,/Hi Kid's/]
 
def replacement = {
    // Change 8 to eat
    if (it == /"/) {
        /'/
    // Change 0 to o
    } else if (it == /'/) {
        /`/
    // Do not transform
    } else {
        null
    }
}
s.each{e-> 
    println "answer:"+e.collectReplacements(replacement)
}
//assert s.collectReplacements(replacement) == 'Groovy is great'
println "--- the end ---"