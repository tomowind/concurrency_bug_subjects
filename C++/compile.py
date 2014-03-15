#!/usr/bin/python
import os
import subprocess

def main():
    fout = open("compile.log", "w")
    for name in os.listdir("."):
        if name.startswith("DEPRECATED"):
            continue
        abscurr = os.path.abspath(os.curdir)
        abspath = os.path.join(".", name)
        if os.path.isdir(abspath) and not abspath.endswith(".svn"):
            print "Compiling "+abspath+"..."
            os.chdir(abspath)
            args = ["make", "llvmerr1"]
            exit = subprocess.call(args, stdout = fout, stderr = fout)
            args = ["make", "llvm"]
            exit = subprocess.call(args, stdout = fout, stderr = fout)
            args = ["make"]
            exit = subprocess.call(args, stdout = fout, stderr = fout)
            # assert exit == 0
            os.chdir(abscurr)
    fout.close()

if __name__ == "__main__":
    main()
