import os
import glob
import random

def generate(timestamp):
    ##events = ["temperature", "humidity", "occupancy", "activity"]
    activities = ["dance", "sing", "acting", "teaching", "presentation", "meeting"]
    aux = []
    room = 1
    while room <= 80:
        l1 = str(room)+","+str(random.uniform(25.5, 40.5))+","+str(random.uniform(5.5, 99.5))+","\
             +str(random.randint(5, 100))+","+str(activities[random.randint(0,5)])+","+str(timestamp)+"\n"
        aux.append(l1)
        room += 1

    return aux

direc = glob.glob("Events/*.txt")
counter = 1
INTERVAL = 5

for file in direc:
	timestamp = 0
	count = 0
	lines = []
	aux = []
	with open(file,'r') as f:
		line = f.readline()
		while line:
			text = line.split()
			if int(text[len(text) - 1]) != timestamp and count < INTERVAL:
				timestamp = int(text[len(text) - 1])
				count += 1
				lines.append(aux)
				aux = []
			if int(text[len(text)-1]) != timestamp and count >= INTERVAL:
				timestamp = int(text[len(text) - 1])
				lines.pop(0)
				lines.append(aux)
				aux = []
				wr = open("DividedFiles/" + str(counter) + ".txt", "w")
				for l in lines:
					for j in l:
						wr.write(j)
				count += 1
				counter += 1
			aux.append(line)
			line = f.readline()
		wr = open("DividedFiles/" + str(counter) + ".txt", "w")
		for l in lines:
			for j in l:
				wr.write(j)
		counter += 1
