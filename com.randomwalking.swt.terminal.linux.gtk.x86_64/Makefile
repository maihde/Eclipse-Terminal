all: libvte_wrap.so

vte_wrap.h: bin/com/randomwalking/swt/terminal/internal/Vte.class
	javah -o vte_wrap.h -classpath ./bin com.randomwalking.swt.terminal.internal.Vte
	
libvte_wrap.so: vte_wrap.h vte_wrap.c
	gcc -Wall -shared -fpic -o libvte_wrap.so \
		-I/usr/lib/jvm/java-6-sun/include \
		-I/usr/lib/jvm/java-6-sun/include/linux vte_wrap.c \
		`pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` \
		`pkg-config --static --cflags vte` `pkg-config --static --libs vte`
	
	
clean:
	rm -f *.so *.o