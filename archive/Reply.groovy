class Reply{
        String dropdownChoice = "Blue"; // @Bindable 
        Map checkbox = [:];    // @Bindable 
        Map radiobutton = [:]; // @Bindable 
        BigDecimal bd = 0;
                
        public String toString() {
            def sb = """dropdownChoice=${dropdownChoice}\n""";
            checkbox.each{k,v-> sb += "checkbox[${k}]=${v} "; }
            if (checkbox.size() > 0 ) sb+= "\n";
            radiobutton.each{k,v-> sb+= "radiobutton[${k}]=${v} "; }
            if (radiobutton.size() > 0 ) sb += "\n"
            return sb.toString();
        } // end of method
                
} // end of class