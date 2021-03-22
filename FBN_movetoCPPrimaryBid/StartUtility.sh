echo "\033]0;FBN_movetoCPPrimarybid_Utility\007"


# ************************************************************************************************
# Modify these variables to match your environment
clear
export JAVA_HOME7=/opt/IBM/WebSphere/AppServer/java/bin
export FBNCPPRIMARYBID_PATH=/opt/IBM/WebSphere/Utilities/FBN_movetoCPPrimaryBid
export NEWGENJARSPATH=$FBNCPPRIMARYBID_PATH/lib
export MYCLASSPATH=$FBNCPPRIMARYBID_PATH/bin

export JARPATH=./:$NEWGENJARSPATH/dmsapi.jar:$NEWGENJARSPATH/log4j-1.2.14.jar:$NEWGENJARSPATH/ISPack.jar:



# ************************************************************************************************
cd $FBNCPPRIMARYBID_PATH/
# ************************************************************************************************
$JAVA_HOME7/java -Xms2048m -Xmx2048m -classpath $FBNCPPRIMARYBID_PATH com.newgen.util.Start.StartUtility



