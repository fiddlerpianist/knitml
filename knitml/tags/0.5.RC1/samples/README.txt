For any of the samples, you can run the following knitml commands.
The following example assumes that you are working with a pattern called 'pattern.kel'
or 'pattern.xml', so substitute whatever the true file name is when you go to use the
command on one of the samples.

Simple Commands:

knitml convert pattern.kel
	converts a pattern from Knitting Expression Language to KnitML, sending the results to STDOUT 

knitml convert pattern.kel -output pattern.xml
	converts a pattern from Knitting Expression Language to KnitML, sending the results to a file named pattern.xml 

knitml validate pattern.xml
	validates and test-knits the KnitML pattern, sending the result to STDOUT

knitml validate pattern.xml -output pattern-validated.xml
	validates and test-knits the pattern, sending the result to a file named pattern-validated.xml

knitml render pattern-validated.xml
	renders a validated pattern to STDOUT

knitml render pattern-validated.xml -output pattern.txt 
	renders a validated pattern to a file called pattern.txt

All-In-One Commands:   
    
knitml validateAndRender pattern.xml -output pattern.txt
    validates, test-knits and renders the pattern

knitml convertValidateAndRender pattern.kel -output pattern.txt
    converts a pattern from Knitting Expression Language to KnitML, validates, test-knits and renders the pattern to pattern.txt

knitml convertAndValidate pattern.kel -output pattern.txt
    converts a pattern from Knitting Expression Language to KnitML, validates and test-knits the pattern
