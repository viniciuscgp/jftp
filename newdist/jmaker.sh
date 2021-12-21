echo '* -------------------------------------------------------- *'
echo '* -      JarMaker V 2.0.0 - For Netbeans Projects        - *'
echo '* -------------------------------------------------------- *'
echo '1) Creating Temporary Directory'

mkdir classes

echo '2) Copying Library Jars'
cp  -rf ../dist/lib/*.jar ./classes
cd classes

echo '3) Deflating Library Jars (this may take a while)'
for f in `ls *.jar`;do jar xf $f; done
rm *.jar
cd ..

echo '4) Deflating Main Program Jar'
cp -rf ../dist/*.jar ./classes
cd classes
for f in `ls *.jar`;do jar xf $f; done
rm *.jar

echo '5) Creating Unique Jar (this may take a while)'
jar cvfm ../dist.jar META-INF/MANIFEST.MF ./
for f in `ls ../../dist/*.jar`; do mv  ../dist.jar $f;done
cd ..

echo '6) Removing Temporary Files'
rm -rf classes
echo '* -------------------------------------------------------- *'
echo '* -         JarMaker V 2.0.0 - Process Complete          - *'
echo '* -------------------------------------------------------- *'

