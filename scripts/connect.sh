SCRIPTDIR=`dirname "$0"`
AZUREDIR=$SCRIPTDIR/../azure
KEYS=$HOME/keys/smarcsoft.pem

#checking for key existence
if [ ! -f "$KEYS" ]; then
    echo "Setting up connection keys..."
    . setup.sh
fi

echo "Connecting to Azure..."
ssh -i $KEYS smarcsoft@51.144.83.211
