import json
import glob

def getTimestamp(tstamp):
	return tstamp.second + 60*tstamp.minute + 3600*tstamp.hour + 86400*tstamp.day + 2678400*tstamp.month

direc = glob.glob("*.txt")

wr = open("Results\\result.txt", "w")

for f in direc:
	with open(f,"r") as file:
		line = file.readline()
		while line:
			text = line.replace('\n','')
			session = json.loads(text)
			ke = session.keys()
			for k in ke:
				wr.write(f.replace(".txt","") + "," + k.replace('T',' ') + "," + session[k] + "\n")
			line = file.readline()


