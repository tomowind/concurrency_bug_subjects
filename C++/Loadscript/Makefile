include ../common.mk

SRCFILE=load_script.cpp
OUT=load_script

uninst: src/$(SRCFILE)
	mkdir -p obj
	mkdir -p bin
	g++ src/$(SRCFILE) -o bin/$(OUT) -lpthread

# load_script with instrumented memory operations
llvm: src/load_script.cpp
	mkdir -p obj 
	mkdir -p bin 
	$(LLVM_GPP_PATH) -c -emit-llvm -g -Wall -Werror src/$(SRCFILE) -o bin/tmp.bc 
	$(LLVM_OPT_PATH) -load $(LLVM_PASS_PATH) -loadstore -f -o bin/tmppass.bc bin/tmp.bc
	$(LLVM_LLC_PATH) bin/tmppass.bc
	g++ bin/tmppass.s -o bin/$(OUT) -L$(THRILLE_ROOT)/bin/ -ldummy -lpthread 
	rm iiddump bin/*.s bin/*.bc 
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)
	mv thrille-randomactive bin

llvmerr1: 
	mkdir -p obj 
	mkdir -p bin 
	$(LLVM_GPP_PATH) -c -DERR1 -emit-llvm -g -Wall -Werror src/$(SRCFILE) -o bin/tmp.bc 
	$(LLVM_OPT_PATH) -load $(LLVM_PASS_PATH) -loadstore -f -o bin/tmppass.bc bin/tmp.bc
	$(LLVM_LLC_PATH) bin/tmppass.bc
	g++ bin/tmppass.s -o bin/$(OUT)-e1 -L$(THRILLE_ROOT)/bin/ -ldummy -lpthread 
	rm iiddump bin/*.s bin/*.bc 
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)-e1
	LD_PRELOAD=$(LIB_LOCK_RACE) bin/$(OUT)-e1
	mv thrille-randomactive bin

test:
	cp bin/thrille-randomactive .
	LD_PRELOAD=$(LIB_RAND_ACT) bin/$(OUT)-e1

clean:
	rm -f bin/*.s iiddump obj/*.bc bin/$(OUT) bin/$(OUT)-e1
	rm memoryaccess my-schedule thrille-randomactive
