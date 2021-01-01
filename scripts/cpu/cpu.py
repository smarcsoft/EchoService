from datetime import datetime
from datetime import timedelta
import sys

if len(sys.argv) != 2:
    print("Usage: cpu <seconds>")
    print("  Maximizes the cpu for <seconds> seconds")
    exit(1)
target = datetime.now() + timedelta(seconds=int(sys.argv[1]))

i = 0

while(datetime.now() < target):
    i+=1
print(str(i))


