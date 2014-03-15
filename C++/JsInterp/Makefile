include ../common.mk

SRCFILE=js_interp.cpp
OUT=js_interp

CC=llvm-g++ -use-gold-plugin
CFLAGS=-O4 -g
LDFLAGS=-L$(THRILLE_ROOT)/bin/ -ldummy -lpthread

# js_interp with instrumented memory operations
gold: src/js_interp.cpp
	mkdir -p obj 
	mkdir -p bin 
	$(CC) $(CFLAGS) $? $(LDFLAGS) -o bin/$(OUT)

# js_interp with instrumented memory operations
llvm: src/js_interp.cpp
	mkdir -p obj 
	mkdir -p bin 
	$(LLVM_GPP_PATH) -c -emit-llvm -g -Wall -Werror src/$(SRCFILE) -o bin/tmp.bc 
	$(LLVM_OPT_PATH) -load $(LLVM_PASS_PATH) -loadstore -f -o bin/tmppass.bc bin/tmp.bc
	$(LLVM_LLC_PATH) -f bin/tmppass.bc
	g++ bin/tmppass.s -o bin/$(OUT) -L$(THRILLE_ROOT)/bin/ -ldummy -lpthread 
	rm iiddump bin/*.s bin/*.bc 
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)
	mv thrille-randomactive bin

llvmerr1: 
	mkdir -p obj 
	mkdir -p bin 
	$(LLVM_GPP_PATH) -c -DERR1 -emit-llvm -g -Wall -Werror src/$(SRCFILE) -o bin/tmp.bc 
	$(LLVM_OPT_PATH) -load $(LLVM_PASS_PATH) -loadstore -f -o bin/tmppass.bc bin/tmp.bc
	$(LLVM_LLC_PATH) -f bin/tmppass.bc
	g++ bin/tmppass.s -o bin/$(OUT)-e1 -L$(THRILLE_ROOT)/bin/ -ldummy -lpthread 
	rm iiddump bin/*.s bin/*.bc 
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)-e1
	mv thrille-randomactive bin

test:
	cp bin/thrille-randomactive .
	LD_PRELOAD=$(LIB_RAND_ACT) bin/$(OUT)-e1

clean:
	rm -f bin/*.s iiddump obj/*.bc bin/$(OUT) bin/$(OUT)-e1
	rm memoryaccess my-schedule thrille-randomactive
