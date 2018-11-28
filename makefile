Main.class:
	javac Main.java
clean:
	rm -f *.class
run: Main.class
	java Main
