#I have to secure the Azure keys into a local unix file system since the DrvFS windows file system does not handle chmod properly
echo "Copying Azure keys..."
SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$AZUREDIR/keys/smarcsoft.pem
mkdir ~/keys
cp $KEYS ~/keys
chmod 400 ~/keys/smarcsoft.pem

echo "If not already installed we need to client azure cli with: sudo az aks install-cli"
