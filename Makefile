compile: 
	rm -rf ./bin
	mkdir -p ./bin
	javac -d ./bin -sourcepath ./src ./src/**/*.java
