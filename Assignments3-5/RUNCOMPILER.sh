#! /bin/sh
#  Location of directory containing  dist/compiler488.jar
WHERE=.
#  Compiler reads one source file from command line argument
#  Output to standard output 

for file in `ls $WHERE/test/passing`
do
    echo $file
    java -jar $WHERE/dist/compiler488.jar $WHERE/test/passing/$file
    echo "---------"
done
echo "PASSING"
echo "================================"

for file in `ls $WHERE/test/failing`
do
    echo $file
    java -jar $WHERE/dist/compiler488.jar $WHERE/test/failing/$file
    echo "---------"
done
echo "FAILING"


exit 0
